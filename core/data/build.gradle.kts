plugins {
    alias(libs.plugins.dzr.android.library.network.data)
}

android {
    namespace = "com.music.dzr.core.data"

    buildTypes {
        release {
            isMinifyEnabled = false
        }
    }
}

dependencies {
    api(projects.dzr.core.model)
    api(projects.dzr.core.common)
}