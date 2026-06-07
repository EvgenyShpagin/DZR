plugins {
    alias(libs.plugins.dzr.android.feature)
    alias(libs.plugins.dzr.screenshot)
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
    implementation(projects.core.auth.domain)
    implementation(libs.androidx.palette)
    implementation(projects.core.navigation) // TODO: mb move to convention plugin
    testImplementation(projects.core.testing)
    androidTestImplementation(libs.androidx.test.junit)
}