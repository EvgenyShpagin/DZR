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

dependencies {
    implementation(projects.dzr.core.data)
    implementation(projects.dzr.library.album.domain)
    testImplementation(testFixtures(projects.dzr.core.data))
}