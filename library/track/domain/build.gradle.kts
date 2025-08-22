plugins {
    alias(libs.plugins.dzr.jvm.library)
}

dependencies {
    api(projects.dzr.core.common)
    api(projects.dzr.core.model)
    api(projects.dzr.library.artist.domain)
}