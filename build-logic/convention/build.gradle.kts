import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    `kotlin-dsl`
}

group = "com.music.dzr.buildlogic"

// Configure the build-logic plugins to target JDK 17
// This matches the JDK used to build the project, and is not related to what is running on device.
java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

kotlin {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_17)
    }
}

// Specify dependencies which are used by plugins for compilation
dependencies {
    compileOnly(libs.android.gradle.plugin)
    compileOnly(libs.kotlin.gradle.plugin)
    compileOnly(libs.kotlin.compose.gradle.plugin)
    implementation(libs.protobuf.gradle.plugin)
    implementation(libs.screenshot.gradle.plugin)
}

tasks {
    validatePlugins {
        enableStricterValidation = true
        failOnWarning = true
    }
}

gradlePlugin {
    plugins {
        // Base Plugins
        register("androidApplication") {
            id = libs.plugins.dzr.android.application.asProvider().get().pluginId
            implementationClass = "AndroidApplicationConventionPlugin"
        }
        register("androidLibrary") {
            id = libs.plugins.dzr.android.library.asProvider().get().pluginId
            implementationClass = "AndroidLibraryConventionPlugin"
        }
        register("jvmLibrary") {
            id = libs.plugins.dzr.jvm.library.get().pluginId
            implementationClass = "JvmLibraryConventionPlugin"
        }

        // Feature Plugins
        register("androidApplicationCompose") {
            id = libs.plugins.dzr.android.application.compose.get().pluginId
            implementationClass = "AndroidApplicationComposeConventionPlugin"
        }
        register("androidLibraryCompose") {
            id = libs.plugins.dzr.android.library.compose.get().pluginId
            implementationClass = "AndroidLibraryComposeConventionPlugin"
        }
        register("koin") {
            id = libs.plugins.dzr.koin.get().pluginId
            implementationClass = "KoinConventionPlugin"
        }
        register("screenshot") {
            id = libs.plugins.dzr.screenshot.get().pluginId
            implementationClass = "ScreenshotConventionPlugin"
        }

        // Aggregate Plugins
        register("androidFeature") {
            id = libs.plugins.dzr.android.feature.get().pluginId
            implementationClass = "AndroidFeatureConventionPlugin"
        }
        register("androidLibraryData") {
            id = libs.plugins.dzr.android.library.data.get().pluginId
            implementationClass = "AndroidLibraryDataConventionPlugin"
        }
        register("androidLibraryDataNetwork") {
            id = libs.plugins.dzr.android.library.network.data.get().pluginId
            implementationClass = "AndroidLibraryNetworkDataConventionPlugin"
        }
        register("androidLibraryProtoData") {
            id = libs.plugins.dzr.android.library.proto.data.get().pluginId
            implementationClass = "AndroidLibraryProtoDataConventionPlugin"
        }
    }
}