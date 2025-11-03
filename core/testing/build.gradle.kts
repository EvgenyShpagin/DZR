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
    api(projects.core.common)
    api(projects.core.network)
    api(projects.core.model)
    api(libs.kotlinx.coroutines.test)
    api(libs.kotlin.test)
}