plugins {
    alias(libs.plugins.dzr.android.library)
    alias(libs.plugins.dzr.koin)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.music.dzr.library.user.data"

    buildTypes {
        release {
            isMinifyEnabled = false
        }
    }
}

dependencies {
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.retrofit.core)
    implementation(libs.retrofit.kotlin.serialization)
    implementation(projects.dzr.core.network)
}