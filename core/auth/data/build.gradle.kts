plugins {
    alias(libs.plugins.dzr.android.library.network.data)
}

android {
    namespace = "com.music.dzr.core.auth.data"

    defaultConfig {
        buildConfigField("String", "SPOTIFY_ACCOUNTS_URL", "\"https://accounts.spotify.com/\"")
    }

    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementation(projects.core.auth.domain)
    implementation(libs.okhttp.logging)
    implementation(libs.androidx.datastore.preferences)
    androidTestImplementation(libs.androidx.junit)
}