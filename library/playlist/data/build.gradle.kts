plugins {
    alias(libs.plugins.dzr.android.data.library)
}

android {
    namespace = "com.music.dzr.library.playlist.data"

    buildTypes {
        release {
            isMinifyEnabled = false
        }
    }
}
