plugins {
    alias(libs.plugins.dzr.android.library)
    alias(libs.plugins.dzr.koin)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.secrets.gradle)
}

android {
    namespace = "com.music.dzr.core.network"

    defaultConfig {
        buildConfigField("String", "SPOTIFY_API_URL", "\"https://api.spotify.com/v1/\"")
        buildConfigField("String", "SPOTIFY_ACCOUNTS_URL", "\"https://accounts.spotify.com/\"")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
        }
    }
    buildFeatures {
        buildConfig = true
    }
    testOptions {
        unitTests {
            // Enable to use assets with jsons
            isIncludeAndroidResources = true
        }
    }
}

secrets {
    propertiesFileName = "core/network/secrets.properties"
    defaultPropertiesFileName = "core/network/secrets.defaults.properties"
}

dependencies {
    api(projects.core.oauth)
    api(libs.kotlinx.serialization.json)
    implementation(libs.retrofit.core)
    implementation(libs.retrofit.kotlin.serialization)
    implementation(libs.okhttp.logging)
    implementation(libs.kotlinx.coroutines.core)

    testImplementation(projects.core.testing)
    testImplementation(libs.junit)
    testImplementation(libs.mockk)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.mockwebserver)
}