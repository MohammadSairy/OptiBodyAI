package com.gradproj.optibodyai.main_activity.training_screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.gradproj.optibodyai.components.ScreenHeader
import com.gradproj.optibodyai.main_activity.host_screens.SELECT_PROGRAM_SCREEN
import com.gradproj.optibodyai.ui.theme.SubTitle

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import android.Manifest
import android.content.pm.PackageManager
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.io.IOException
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items

@Composable
fun SelectGymScreen(navController: NavHostController) {
    val context = LocalContext.current
    var nearbyGyms by remember { mutableStateOf(listOf<String>()) }
    var checkedGym: String? by remember { mutableStateOf(null) }

    // Log the initial setup
    Log.d("SelectGymScreen", "Composable Launched")

    LaunchedEffect(Unit) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED) {
           /* getLocation(context) { latitude, longitude ->
                Log.d("SelectGymScreen", "Location received: Latitude=$latitude, Longitude=$longitude")
                fetchNearbyGyms(latitude, longitude, context) { gyms ->
                    Log.d("SelectGymScreen", "Fetched gyms: $gyms")
                    nearbyGyms = gyms
                }
            }*/
        } else {
            Log.d("SelectGymScreen", "Location permission not granted")
            Toast.makeText(context, "Location permission is required to find gyms.", Toast.LENGTH_SHORT).show()
        }
    }

    Column(Modifier.padding(horizontal = 18.dp)) {
        ScreenHeader(title = "Locating a gym near you...")
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Select a location and press “Ready” to begin your training!",
            fontWeight = FontWeight.Bold,
            color = SubTitle
        )
        Spacer(modifier = Modifier.height(32.dp))
        Text(
            text = "Here’s what we found:",
            color = Color.White,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(modifier = Modifier.weight(1f)) {
            items(nearbyGyms) { gym ->
                CheckBoxRow(gym, checkedGym == gym) {
                    checkedGym = it
                    Log.d("SelectGymScreen", "Selected gym: $checkedGym")
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                Log.d("SelectGymScreen", "Ready button clicked. Selected gym: $checkedGym")
                try {
                    navController.navigate(SELECT_PROGRAM_SCREEN)
                    Log.d("SelectGymScreen", "Navigation successful to SelectProgramDestination")
                } catch (e: Exception) {
                    Log.e("SelectGymScreen", "Navigation failed", e)
                    Toast.makeText(context, "Failed to navigate. Please try again.", Toast.LENGTH_SHORT).show()
                }
            },
            Modifier
                .fillMaxWidth(0.75f)
                .align(Alignment.CenterHorizontally)
                .padding(vertical = 16.dp)
        ) {
            Text(
                text = "Next",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}



@Composable
fun CheckBoxRow(text: String, checked: Boolean?, onChecked: (String) -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.clickable(
            indication = null,
            interactionSource = null
        ) { onChecked(text) }) {
        Checkbox(
            modifier = Modifier.clip(CircleShape),
            checked = checked == true,
            onCheckedChange = { onChecked(text) },
            colors = CheckboxDefaults.colors(
                uncheckedColor = MaterialTheme.colorScheme.secondary
            ),
        )
        Text(text = text, color = Color.White)
    }
}

fun getLocation(context: Context, onLocationRetrieved: (Double, Double) -> Unit) {
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    Log.d("getLocation", "Starting location request")

    val locationRequest = com.google.android.gms.location.LocationRequest.create().apply {
        interval = 10000 // Every 10 seconds (adjust based on your needs)
        fastestInterval = 5000
        priority = com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY
    }

    val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val location = locationResult.lastLocation
            if (location != null) {
                Log.d("getLocation", "Location found: Latitude=${location.latitude}, Longitude=${location.longitude}")
                onLocationRetrieved(location.latitude, location.longitude)
                fusedLocationClient.removeLocationUpdates(this) // Stop updates after receiving the location
            } else {
                Log.d("getLocation", "Location is null, possibly due to GPS issues")
                Toast.makeText(context, "Failed to get location. Make sure GPS is enabled.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    try {
        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
    } catch (e: SecurityException) {
        Log.e("getLocation", "Location permission not granted", e)
        Toast.makeText(context, "Location permission not granted.", Toast.LENGTH_SHORT).show()
    }
}

fun fetchNearbyGyms(latitude: Double, longitude: Double, context: Context, onResults: (List<String>) -> Unit) {
    val apiKey = // Replace with your actual API key
    val url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=$latitude,$longitude&radius=2000&type=gym&key=$apiKey"
    Log.d("fetchNearbyGyms", "Request URL: $url")

    val client = OkHttpClient()
    val request = Request.Builder().url(url).build()

    client.newCall(request).enqueue(object : okhttp3.Callback {
        override fun onFailure(call: okhttp3.Call, e: IOException) {
            Log.e("fetchNearbyGyms", "Failed to fetch gyms", e)
            Handler(Looper.getMainLooper()).post {
                Toast.makeText(context, "Failed to fetch gyms", Toast.LENGTH_SHORT).show()
            }
        }

        override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
            response.body?.let { responseBody ->
                val jsonData = responseBody.string()
                Log.d("fetchNearbyGyms", "API Response: $jsonData")

                val gymNames = parseGymNames(jsonData)
                Log.d("fetchNearbyGyms", "Parsed gym names: $gymNames")

                // Pass the list of gym names to the callback
                onResults(gymNames)
            } ?: run {
                Log.d("fetchNearbyGyms", "Response body is null")
                Handler(Looper.getMainLooper()).post {
                    Toast.makeText(context, "Failed to fetch gyms", Toast.LENGTH_SHORT).show()
                }
            }
        }
    })
}

fun parseGymNames(jsonData: String): List<String> {
    val gyms = mutableListOf<String>()
    try {
        val jsonObject = JSONObject(jsonData)
        val resultsArray = jsonObject.getJSONArray("results")

        for (i in 0 until resultsArray.length()) {
            val gymObject = resultsArray.getJSONObject(i)
            val name = gymObject.getString("name")
            val rating = gymObject.optDouble("rating", -1.0).takeIf { it >= 0 }?.toString() ?: "N/A"
            val distance = "2 km" // Placeholder distance

            // Concatenate name, rating, and distance into a single string
            val gymInfo = "$name - Rating: $rating - Distance: $distance"
            gyms.add(gymInfo)
        }
    } catch (e: Exception) {
        Log.e("parseGymNames", "Error parsing JSON", e)
    }
    return gyms
}

