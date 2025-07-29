plugins {
    alias(libs.plugins.dzr.jvm.library)
    `java-test-fixtures`
}

dependencies {
    api(libs.kotlinx.coroutines.core)
    testFixturesApi(libs.kotlinx.coroutines.test)
}