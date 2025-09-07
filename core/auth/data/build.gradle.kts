plugins {
    alias(libs.plugins.dzr.android.library.network.data)
}

android {
    namespace = "com.music.dzr.core.auth.data"
}

dependencies {
    implementation(projects.core.auth.domain)
    implementation(libs.okhttp.logging)
}