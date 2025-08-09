plugins {
    alias(libs.plugins.dzr.android.library.network.data)
}

android {
    namespace = "com.music.dzr.library.album.data"

    buildTypes {
        release {
            isMinifyEnabled = false
        }
    }
}
