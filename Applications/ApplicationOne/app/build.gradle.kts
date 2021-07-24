plugins {
    id("com.android.application")
    id("kotlin-android")
}

android {
    compileSdkVersion(30)
    buildToolsVersion("30.0.3")

    defaultConfig {
        applicationId("com.example.code")
        minSdkVersion(16)
        targetSdkVersion(30)
        versionCode(1)
        versionName("1.0")
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
    implementation(Libraries.kotlinx_coroutines_core)
    implementation(Libraries.kotlinx_coroutines_android)
    implementation(Libraries.lifecycle_viewmodel_ktx)
    implementation(Libraries.lifecycle_runtime_ktx)
    implementation(Libraries.navigation_fragment_ktx)
    implementation(Libraries.navigation_ui_ktx)
    implementation(Libraries.google_material)
    testImplementation(UnitTestLibraries.junit4)

    androidTestImplementation(AndroidTestLibraries.junit)
    androidTestImplementation(AndroidTestLibraries.espresso)
}