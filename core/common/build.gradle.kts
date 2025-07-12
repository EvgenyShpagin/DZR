plugins {
    alias(libs.plugins.dzr.jvm.library)
    alias(libs.plugins.dzr.koin)
    `java-test-fixtures`
}

dependencies {
    api(libs.kotlinx.coroutines.core)
    testFixturesApi(libs.kotlinx.coroutines.test)
}