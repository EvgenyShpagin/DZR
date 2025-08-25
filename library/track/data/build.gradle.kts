plugins {
    alias(libs.plugins.dzr.android.library.network.data)
}

android {
    namespace = "com.music.dzr.library.track.data"

    buildTypes {
        release {
            isMinifyEnabled = false
        }
    }
}

dependencies {
    implementation(projects.dzr.core.data)
    implementation(projects.dzr.library.track.domain)
    testImplementation(testFixtures(projects.core.data))
}