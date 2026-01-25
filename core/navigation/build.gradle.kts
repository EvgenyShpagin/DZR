plugins {
    alias(libs.plugins.dzr.android.library.compose)
}

android {
    namespace = "com.music.dzr.core.navigation"

    buildTypes {
        release {
            isMinifyEnabled = false
        }
    }
}
