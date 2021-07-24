plugins {
    id("com.android.application")
    id("kotlin-android")
}

android {
    compileSdkVersion(AndroidSdk.target)
    buildToolsVersion(AndroidSdk.buildTools)

    defaultConfig {
        applicationId("com.example.code")
        minSdkVersion(AndroidSdk.min)
        targetSdkVersion(AndroidSdk.target)
        versionCode(AppVersion.versionCode)
        versionName(AppVersion.versionName)
        // vectorDrawables.useSupportLibrary = true
        testInstrumentationRunner("androidx.test.runner.AndroidJUnitRunner")
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled=false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions { kotlinOptions.jvmTarget = "1.8" }
    buildFeatures { viewBinding=true }
}

dependencies {

    implementation(Libraries.kotlin_stdlib)
    implementation(Libraries.core_ktx)
    implementation(Libraries.appcompat)
    implementation(Libraries.constraint_layout)
    implementation(Libraries.legacy_support_v4)
    implementation(Libraries.activity_ktx)
    implementation(Libraries.fragment_ktx)
    implementation(Libraries.kotlin_x_coroutines_core)
    implementation(Libraries.kotlin_x_coroutines_android)
    implementation(Libraries.lifecycle_view_model_ktx)
    implementation(Libraries.lifecycle_runtime_ktx)
    implementation(Libraries.navigation_fragment_ktx)
    implementation(Libraries.navigation_ui_ktx)
    implementation(Libraries.google_material)
    testImplementation(UnitTestLibraries.junit4)

    androidTestImplementation(AndroidTestLibraries.junit)
    androidTestImplementation(AndroidTestLibraries.espresso)
}