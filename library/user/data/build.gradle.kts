plugins {
    alias(libs.plugins.dzr.android.library.network.data)
}

android {
    namespace = "com.music.dzr.library.user.data"

    buildTypes {
        release {
            isMinifyEnabled = false
        }
    }
}
