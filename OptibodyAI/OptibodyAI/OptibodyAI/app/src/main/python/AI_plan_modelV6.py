import pandas as pd
from sklearn.model_selection import train_test_split
from tensorflow.keras.models import Sequential
from tensorflow.keras.layers import Dense, Dropout
from tensorflow.keras.callbacks import EarlyStopping
import json
import os
import matplotlib.pyplot as plt

# File to store memory
MEMORY_FILE = "memory.json"

# Load memory from file
def load_memory():
    if os.path.exists(MEMORY_FILE):
        with open(MEMORY_FILE, 'r') as file:
            return json.load(file)
    return {}

# Save memory to file
def save_memory(memory):
    with open(MEMORY_FILE, 'w') as file:
        json.dump(memory, file)

# Initialize memory
memory = load_memory()

# Step 1: Load and preprocess the dataset
file_path = os.path.join(os.environ["HOME"], "gym_exercise_dataset_V.2.csv")
gym_data = pd.read_csv(file_path)

# User input (profile)
user_profile = {
    "difficulty_range": (1, 5),  # Difficulty range
    "equipment": ["Dumbbell", "Body Weight", "Barbell", "Cable", "Smith"],  # Available equipment
    "injuries": [],  # User injuries (empty list for no injuries)
}

# Step 2: Filter dataset based on user constraints and memory
def filter_exercises(data, profile):
    # Filter by difficulty range
    filtered_data = data[
        (data["Difficulty (1-5)"] >= profile["difficulty_range"][0]) &
        (data["Difficulty (1-5)"] <= profile["difficulty_range"][1])
    ]

    # Filter by equipment
    filtered_data = filtered_data[
        filtered_data.apply(lambda row: any(eq in row["Equipment"] for eq in profile["equipment"]), axis=1)
    ]

    # Exclude unsuitable exercises for injuries
    if profile["injuries"]:
        for injury in profile["injuries"]:
            filtered_data = filtered_data[filtered_data["Main_muscle"] != injury.capitalize()]

    print(f"Filtered exercises after user profile constraints: {len(filtered_data)}")
    return filtered_data

# Step 3: Prepare data for AI model
# One-hot encode the dataset
categorical_columns = ['Equipment', 'Mechanics', 'Force', 'Main_muscle']
gym_data_encoded = pd.get_dummies(gym_data, columns=categorical_columns, drop_first=True)

# Define features (X) and labels (y)
X = gym_data_encoded.drop(columns=['Exercise Name', 'Difficulty (1-5)'])
y = pd.get_dummies(gym_data['Exercise Name'])  # Multi-label output: one-hot encoded exercise names

# Split into training and testing sets
X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.2, random_state=42)

# Step 4: Define and train the AI model
model = Sequential([
    Dense(512, activation='relu', input_shape=(X_train.shape[1],)),
    Dropout(0.3),
    Dense(256, activation='relu'),
    Dropout(0.2),
    Dense(128, activation='relu'),
    Dropout(0.2),
    Dense(y_train.shape[1], activation='sigmoid')  # Sigmoid for multi-label probabilities
])

model.compile(optimizer='adam', loss='binary_crossentropy', metrics=['accuracy'])

# Early stopping to prevent overfitting
early_stopping = EarlyStopping(monitor='val_loss', patience=5, restore_best_weights=True)

# Train the model
model.fit(X_train, y_train, epochs=50, batch_size=64, validation_split=0.2, callbacks=[early_stopping])

# Step 5: Prepare filtered data for prediction
def prepare_filtered_data(filtered_data, original_columns, categorical_columns):
    # One-hot encode filtered data
    encoded_filtered = pd.get_dummies(filtered_data, columns=categorical_columns, drop_first=True)

    # Align columns with training data
    encoded_filtered = encoded_filtered.reindex(columns=original_columns, fill_value=0)

    # Ensure numeric format
    return encoded_filtered.astype('float32')

# Step 6: Rank exercises with intelligent memory handling
def rank_exercises_with_memory(filtered_data, model, original_columns, memory, user_key, recommended_count=6, compound_count=2):
    if filtered_data.empty:
        print("No exercises left after filtering.")
        return pd.DataFrame(columns=['Exercise Name', 'Suitability', 'Main_muscle'])

    # Prepare the filtered dataset
    encoded_filtered = prepare_filtered_data(filtered_data, original_columns, ['Equipment', 'Mechanics', 'Force', 'Main_muscle'])

    # Predict suitability
    predictions = model.predict(encoded_filtered)

    # Add suitability scores
    filtered_data = filtered_data.copy()
    filtered_data['Suitability'] = predictions.mean(axis=1)

    # Apply memory penalization
    if user_key in memory:
        for exercise_name, row in filtered_data.iterrows():
            if exercise_name in memory[user_key]:
                # Penalize Suitability based on recommendation count
                frequency = memory[user_key][exercise_name]
                penalty = 0.1 * frequency  # Reduce Suitability by 10% per prior recommendation
                filtered_data.at[exercise_name, 'Suitability'] *= (1 - penalty)

    # Sort by Suitability after penalization
    filtered_data = filtered_data.sort_values(by='Suitability', ascending=False)

    # Separate compound and isolation exercises
    compound_exercises = filtered_data[filtered_data['Mechanics'] == 'Compound']
    isolation_exercises = filtered_data[filtered_data['Mechanics'] != 'Compound']

    # Select top diverse compound exercises
    muscle_groups_seen = set()
    selected_compound_exercises = []

    for _, row in compound_exercises.iterrows():
        muscle_group = row['Main_muscle']
        if muscle_group not in muscle_groups_seen:
            selected_compound_exercises.append(row)
            muscle_groups_seen.add(muscle_group)
        if len(selected_compound_exercises) >= compound_count:
            break

    # Fill the rest with diverse isolation exercises
    muscle_groups_seen.update([row['Main_muscle'] for row in selected_compound_exercises])
    final_recommendations = selected_compound_exercises

    for _, row in isolation_exercises.iterrows():
        muscle_group = row['Main_muscle']
        if muscle_group not in muscle_groups_seen:
            final_recommendations.append(row)
            muscle_groups_seen.add(muscle_group)
        if len(final_recommendations) >= recommended_count:
            break

    # Convert back to DataFrame and assign proper column names
    final_recommendations_df = pd.DataFrame(final_recommendations, columns=filtered_data.columns)

    # Update memory with recommended exercises
    if user_key not in memory:
        memory[user_key] = {}
    for exercise_name in final_recommendations_df['Exercise Name']:
        if exercise_name in memory[user_key]:
            memory[user_key][exercise_name] += 1
        else:
            memory[user_key][exercise_name] = 1

    # Debug: Print final recommendations
    print("Final Recommendations (Memory-Adjusted):")
    print(final_recommendations_df[['Exercise Name', 'Main_muscle', 'Mechanics', 'Suitability']])

    return final_recommendations_df

# Step 7: Generate user key and rank exercises
user_key = f"difficulty_range:{user_profile['difficulty_range']}|equipment:{','.join(user_profile['equipment'])}|injuries:{','.join(user_profile['injuries'])}"
filtered_exercises = filter_exercises(gym_data, user_profile)
ranked_exercises = rank_exercises_with_memory(filtered_exercises, model, X.columns, memory, user_key, recommended_count=6, compound_count=2)

# Step 8: Display the recommended training plan
if ranked_exercises.empty:
    print("No recommendations could be generated.")
else:
    print("Recommended Training Plan:")
    print(ranked_exercises[['Exercise Name', 'Main_muscle', 'Mechanics', 'Suitability']])

# Step 9: Save memory to file
save_memory(memory)
