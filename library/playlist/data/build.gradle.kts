plugins {
    alias(libs.plugins.dzr.android.library.network.data)
}

android {
    namespace = "com.music.dzr.library.playlist.data"

    buildTypes {
        release {
            isMinifyEnabled = false
        }
    }
}

dependencies {
    api(projects.dzr.core.common)
    api(projects.dzr.core.data)
    api(projects.dzr.library.playlist.domain)
    testImplementation(testFixtures(projects.dzr.core.data))
}