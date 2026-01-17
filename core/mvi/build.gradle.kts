plugins {
    alias(libs.plugins.dzr.android.library)
}

android {
    namespace = "com.music.dzr.core.mvi"

    buildTypes {
        release {
            isMinifyEnabled = false
        }
    }
}

dependencies {
    api(libs.kotlinx.coroutines.core)
    api(libs.androidx.lifecycle.viewModel.ktx)
}
