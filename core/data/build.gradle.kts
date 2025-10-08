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
    // core:storage could be provided by a convention plugin,
    // but added directly as it's the only storage-related dependency for now.
    api(projects.dzr.core.storage)

    testFixturesApi(projects.dzr.core.network)
}