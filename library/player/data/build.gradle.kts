plugins {
    alias(libs.plugins.dzr.android.library.network.data)
}

android {
    namespace = "com.music.dzr.library.player.data"

    buildTypes {
        release {
            isMinifyEnabled = false
        }
    }
}

dependencies {
    implementation(projects.dzr.core.data)
    implementation(projects.dzr.library.player.domain)
    testImplementation(testFixtures(projects.dzr.core.data))
}
