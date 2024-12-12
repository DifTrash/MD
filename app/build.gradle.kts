plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("kotlin-kapt")
    id("kotlin-parcelize")
    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "com.example.diftrash"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.diftrash"
        minSdk = 26
        targetSdk = 34
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

    buildFeatures{
        viewBinding = true
        dataBinding = true
        buildConfig = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.swiperefreshlayout)
    implementation(libs.firebase.auth)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation (libs.okhttp)

    implementation(libs.androidx.datastore.preferences)

    implementation (libs.androidx.fragment.ktx)
    implementation (libs.picasso)
    implementation (libs.glide)
    implementation(libs.glide.v500rc01)
    annotationProcessor (libs.compiler)

    implementation (libs.retrofit)
    implementation (libs.converter.gson)
    implementation(libs.logging.interceptor)
    implementation(libs.androidx.lifecycle.runtime.ktx)
}