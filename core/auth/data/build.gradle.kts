plugins {
    alias(libs.plugins.dzr.android.data.library)
}

android {
    namespace = "com.music.dzr.core.auth.data"
}

dependencies {
    implementation(projects.core.auth.domain)
}