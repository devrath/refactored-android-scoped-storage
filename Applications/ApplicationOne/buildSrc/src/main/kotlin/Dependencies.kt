const val kotlinVersion = "1.3.21"

object BuildPlugins {

    object Versions {
        const val buildToolsVersion = "3.3.1"
    }

    const val androidGradlePlugin = "com.android.tools.build:gradle:${Versions.buildToolsVersion}"
    const val kotlinGradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion"
    const val androidApplication = "com.android.application"
    const val kotlinAndroid = "kotlin-android"
    const val kotlinAndroidExtensions = "kotlin-android-extensions"

}

object AndroidSdk {
    const val min = 15
    const val compile = 28
    const val target = compile
}

object Libraries {
    private object Versions {
        const val google_material = "1.4.0"
        const val navigation_ui_ktx = "2.3.5"
        const val navigation_fragment_ktx = "2.3.5"

        const val lifecycle_runtime_ktx = "2.3.1"
        const val lifecycle_viewmodel_ktx = "2.3.1"
        const val kotlinx_coroutines_android = "1.5.0"
        const val kotlinx_coroutines_core = "1.5.0"

        const val fragment_ktx = "1.3.6"
        const val activity_ktx = "1.2.4"
        const val legacy_support_v4 = "1.0.0"
        const val constraint_layout = "2.0.4"
        const val appcompat = "1.3.1"
        const val core_ktx = "1.6.0"
        const val kotlin_stdlib = "1.5.10"
    }

    const val kotlin_stdlib     = "org.jetbrains.kotlin:kotlin-stdlib:${Versions.kotlin_stdlib}"
    const val core_ktx         = "androidx.core:core-ktx:${Versions.core_ktx}"
    const val appcompat     = "androidx.appcompat:appcompat:${Versions.appcompat}"
    const val constraint_layout     = "androidx.constraintlayout:constraintlayout:${Versions.constraint_layout}"
    const val legacy_support_v4     = "androidx.legacy:legacy-support-v4:${Versions.legacy_support_v4}"
    const val activity_ktx     = "androidx.activity:activity-ktx:${Versions.activity_ktx}"
    const val fragment_ktx     = "androidx.fragment:fragment-ktx:${Versions.fragment_ktx}"
    const val kotlinx_coroutines_core     = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.kotlinx_coroutines_core}"
    const val kotlinx_coroutines_android     = "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.kotlinx_coroutines_android}"
    const val lifecycle_viewmodel_ktx     = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.lifecycle_viewmodel_ktx}"
    const val lifecycle_runtime_ktx     = "androidx.lifecycle:lifecycle-runtime-ktx:${Versions.lifecycle_runtime_ktx}"
    const val navigation_fragment_ktx     = "androidx.navigation:navigation-fragment-ktx:${Versions.navigation_fragment_ktx}"
    const val navigation_ui_ktx     = "androidx.navigation:navigation-ui-ktx:${Versions.navigation_ui_ktx}"
    const val google_material     = "com.google.android.material:material:${Versions.google_material}"
    //const val google_material_new     = "com.google.android.material:material:${Versions.google_material_new}"
}

object UnitTestLibraries {
    private object Versions {
        const val junit4 = "4.12"
    }
    const val junit4     = "junit:junit:${Versions.junit4}"}

object AndroidTestLibraries {
    private object Versions {
        const val junit = "1.1.3"
        const val espresso = "3.4.0"
    }
    const val junit = "androidx.test.ext:junit:${Versions.junit}"
    const val espresso   = "androidx.test.espresso:espresso-core:${Versions.espresso}"
}