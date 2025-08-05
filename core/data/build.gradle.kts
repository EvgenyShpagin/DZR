plugins {
    alias(libs.plugins.dzr.android.data.library)
}

android {
    namespace = "com.music.dzr.core.data"

    buildTypes {
        release {
            isMinifyEnabled = false
        }
    }
}
