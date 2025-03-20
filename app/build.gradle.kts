plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.google.android.libraries.mapsplatform.secrets.gradle.plugin)
    id("kotlin-kapt") // Add this for Room
    id("com.google.dagger.hilt.android")
    id("com.google.devtools.ksp")

}

android {
    namespace = "com.example.chatbot"
    compileSdk = 35

    defaultConfig {
        //buildConfigField("String", "API_KEY", "\"AIzaSyCCBILjmpiTBaQRuJaQi9K98De9f0O8VcQ\"")
        applicationId = "com.example.chatbot"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.generativeai)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // Room components
    implementation(libs.androidx.room.room.runtime)
    annotationProcessor(libs.androidx.room.room.compiler)

    // Kotlin Extensions and Coroutines support for Room
    implementation(libs.androidx.room.room.ktx)

    // Optional - Test helpers
    testImplementation(libs.androidx.room.testing)
    implementation(libs.androidx.room.room.runtime)
    ksp(libs.androidx.room.room.compiler)
    implementation(libs.androidx.room.room.ktx)


    //lifecycle view-model
    implementation(libs.androidx.lifecycle.viewmodel.compose.v270)


    //navigation compose
    implementation(libs.androidx.navigation.compose)

    // Dagger - Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    /*
        implementation ("androidx.hilt:hilt-lifecycle-viewmodel:1.0.0-alpha03")
    */
    ksp(libs.androidx.hilt.compiler) // Or a newer stable version


    // For Compose integration
    implementation(libs.androidx.hilt.navigation.compose)


}