plugins {
    alias(libs.plugins.dzr.jvm.library)
}

dependencies {
    api(projects.core.model)
    api(projects.core.common)
}