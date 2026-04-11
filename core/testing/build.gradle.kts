plugins {
    alias(libs.plugins.dzr.jvm.library)
}

dependencies {
    api(projects.core.common)
    api(projects.core.model)
    api(libs.kotlinx.coroutines.test)
    api(libs.kotlin.test)
    api(libs.junit)
}
