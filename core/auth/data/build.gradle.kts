plugins {
    alias(libs.plugins.dzr.android.library.network.data)
    alias(libs.plugins.dzr.android.library.proto.data)
}

android {
    namespace = "com.music.dzr.core.auth.data"

    defaultConfig {
        buildConfigField("String", "SPOTIFY_AUTH_BASE_URL ", "\"https://accounts.spotify.com/\"")
    }

    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementation(projects.core.auth.domain)
    implementation(projects.dzr.core.storage)
    implementation(libs.okhttp.logging)
}