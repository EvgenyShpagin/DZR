plugins {
    alias(libs.plugins.dzr.jvm.library)
}

dependencies {
    api(projects.dzr.core.common)
    testImplementation(projects.core.testing)
    testImplementation(libs.mockk)
}