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
    implementation(libs.androidx.palette.ktx)
    testImplementation(projects.core.testing)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
}