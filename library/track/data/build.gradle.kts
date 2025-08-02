plugins {
    alias(libs.plugins.dzr.android.data.library)
}


android {
    namespace = "com.music.dzr.library.track.data"

    buildTypes {
        release {
            isMinifyEnabled = false
        }
    }
}
