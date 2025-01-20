plugins {

    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)






}



android {
    namespace = "com.gradproj.optibodyai"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.gradproj.optibodyai"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}





dependencies {

    implementation("io.github.jan-tennert.supabase:postgrest-kt:3.0.2")
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("androidx.compose.material3:material3:1.1.0-beta01") // Use the latest stable/beta version
    implementation("androidx.compose.ui:ui:1.4.0") // Ensure this matches your Compose version
    implementation ("androidx.compose.material3:material3:1.1.0-beta01") // Use the latest stable or beta version
    implementation ("androidx.compose.ui:ui:1.4.0") // Ensure this matches your Compose version

    implementation(platform("io.github.jan-tennert.supabase:bom:3.0.2"))
    implementation("io.github.jan-tennert.supabase:postgrest-kt")
    implementation("io.ktor:ktor-client-android:3.0.1")
    implementation("io.github.jan-tennert.supabase:auth-kt")

    implementation(platform("io.github.jan-tennert.supabase:bom:3.0.2"))
    implementation("io.github.jan-tennert.supabase:auth-kt")
    implementation("io.github.jan-tennert.supabase:postgrest-kt")

    implementation("io.github.jan-tennert.supabase:postgrest-kt:3.0.2")
    implementation("io.ktor:ktor-client-cio:3.0.1")



    implementation("com.squareup.okhttp3:okhttp:4.9.0")
    implementation("org.json:json:20201115")
// Jetpack Compose Material
    implementation ("androidx.compose.material:material:1.x.x")

    implementation ("androidx.compose.material3:material3:1.x.x") // Replace with the latest version



    // Jetpack Compose Material3
    implementation ("androidx.compose.material3:material3:1.x.x")
    implementation("com.google.android.gms:play-services-location:21.0.1")
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)


    implementation(libs.navigation.compose)
    implementation(libs.kotlinx.serialization.json)

}

