

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.kapt")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.example.template"
    compileSdk = 34

    // buildFeatures.buildConfig = true

    defaultConfig {
        applicationId = "com.example.template"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

        buildConfigField("String", "STRIPE_API_KEY", "\"${project.property("STRIPE_API_KEY")}\"")

        buildConfigField("String", "MAPS_API_KEY", "\"${project.property("MAPS_API_KEY")}\"")
        manifestPlaceholders["MAPS_API_KEY"] = project.property("MAPS_API_KEY") ?: ""

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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.3"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.0")
    implementation("androidx.activity:activity-compose:1.9.0")
    implementation(platform("androidx.compose:compose-bom:2023.03.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    // implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.material3:material3:1.2.0-rc01")
    implementation("androidx.compose.material3:material3-android:1.2.0-rc01")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.03.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    implementation("androidx.navigation:navigation-compose:2.7.7")
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")
    implementation("com.google.dagger:hilt-android:2.49")
    kapt("com.google.dagger:hilt-android-compiler:2.44")

    implementation("androidx.compose.runtime:runtime-livedata:1.6.7")

    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.2")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")

    implementation("com.amazonaws:aws-android-sdk-core:2.75.1")
    implementation("com.amazonaws:aws-android-sdk-cognito:2.20.1")
    implementation("com.amazonaws:aws-android-sdk-mobile-client:2.75.1")
    implementation("com.amazonaws:aws-android-sdk-cognitoidentityprovider:2.75.1")

    // To get images via url
    implementation("com.github.bumptech.glide:glide:4.16.0")

    implementation("androidx.compose.foundation:foundation:1.6.8")

    // To use stripe components to add payment methods
    implementation("com.stripe:stripe-android:20.46.0")

    implementation("com.google.accompanist:accompanist-permissions:0.30.0")
    // Google Maps
    implementation("com.google.maps.android:maps-compose:2.11.0")
    implementation("com.google.android.gms:play-services-maps:18.2.0")
    // To get the last known location
    implementation("com.google.android.gms:play-services-location:21.3.0")
    // To search for places via Google maps
    implementation("com.google.android.libraries.places:places:3.5.0")

    // Azure communication services
    implementation("com.azure.android:azure-communication-calling:2.8.0")


}

kapt {
    correctErrorTypes = true
}