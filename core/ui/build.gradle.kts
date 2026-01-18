plugins {
    alias(libs.plugins.dzr.android.library)
    alias(libs.plugins.dzr.android.library.compose)
    alias(libs.plugins.dzr.screenshot)
}

android {
    namespace = "com.music.dzr.core.ui"

    packaging {
        resources.excludes.addAll(
            listOf(
                "META-INF/LICENSE.md",
                "META-INF/LICENSE-notice.md"
            )
        )
    }
}

dependencies {
    api(projects.core.designSystem)
    api(projects.core.model)
    api(libs.coil.compose)
    api(libs.coil.network.okhttp)
    testImplementation(libs.junit)

    androidTestImplementation(libs.androidx.test.junit)
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    androidTestImplementation(libs.mockk.android)
}