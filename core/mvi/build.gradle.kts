plugins {
    alias(libs.plugins.dzr.android.library)
    alias(libs.plugins.dzr.android.library.compose)
}

android {
    namespace = "com.music.dzr.core.mvi"

    buildTypes {
        release {
            isMinifyEnabled = false
        }
    }
}

dependencies {
    api(libs.kotlinx.coroutines.core)
    api(libs.androidx.lifecycle.viewmodel.ktx)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(projects.dzr.core.testing)
}
