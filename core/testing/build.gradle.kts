plugins {
    alias(libs.plugins.dzr.android.library)
}

android {
    namespace = "com.music.dzr.core.testing"

    buildTypes {
        release {
            isMinifyEnabled = false
        }
    }
}

dependencies {
    api(projects.core.oauth)
}