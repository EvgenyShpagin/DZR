plugins {
    alias(libs.plugins.dzr.jvm.library)
    alias(libs.plugins.dzr.koin)
}
dependencies {
    api(libs.kotlinx.coroutines.core)
}