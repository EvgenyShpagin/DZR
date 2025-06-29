plugins {
    alias(libs.plugins.dzr.android.library)
    alias(libs.plugins.dzr.android.library.compose)
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
    api(project(":core:design-system"))
    api(project(":core:model"))
    implementation(libs.coil.compose)
    testImplementation(libs.junit)

    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.ui.test.junit4)
    androidTestImplementation(libs.mockk.android)
}