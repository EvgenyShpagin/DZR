plugins {
    alias(libs.plugins.dzr.android.feature)
}

android {
    namespace = "com.music.dzr.feature.auth"

    buildTypes {
        release {
            isMinifyEnabled = false
        }
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(projects.core.ui)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
}