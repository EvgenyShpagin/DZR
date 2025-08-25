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

    @Suppress("UnstableApiUsage")
    testFixtures.enable = true
}

dependencies {
    api(projects.dzr.core.model)
    api(projects.dzr.core.common)

    testFixturesApi(projects.dzr.core.network)
}