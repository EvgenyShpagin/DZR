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
    implementation(projects.core.oauth)
}