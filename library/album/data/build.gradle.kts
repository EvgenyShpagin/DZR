plugins {
    alias(libs.plugins.dzr.android.data.library)
}

android {
    namespace = "com.music.dzr.library.album.data"

    buildTypes {
        release {
            isMinifyEnabled = false
        }
    }
}
