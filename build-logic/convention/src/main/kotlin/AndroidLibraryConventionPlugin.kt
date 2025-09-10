import com.android.build.api.variant.LibraryAndroidComponentsExtension
import com.android.build.gradle.LibraryExtension
import com.music.dzr.androidTestImplementation
import com.music.dzr.configureGradleManagedDevices
import com.music.dzr.configureKotlinAndroid
import com.music.dzr.disableEmptyAndroidTests
import com.music.dzr.libs
import com.music.dzr.testImplementation
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

class AndroidLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            apply(plugin = "com.android.library")
            apply(plugin = "org.jetbrains.kotlin.android")

            extensions.configure<LibraryExtension> {
                configureKotlinAndroid(this)
                defaultConfig.targetSdk = 35
                defaultConfig.testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
                // Disable animations during instrumented tests run from the command line
                testOptions.animationsDisabled = true
                configureGradleManagedDevices(this)
                resourcePrefix = path.toModuleResourcePrefix()
            }
            extensions.configure<LibraryAndroidComponentsExtension> {
                disableEmptyAndroidTests(target)
            }
            dependencies {
                testImplementation(libs.findLibrary("kotlin.test").get())
                androidTestImplementation(libs.findLibrary("kotlin.test").get())
                androidTestImplementation(libs.findLibrary("androidx-test-runner").get())
            }
        }
    }
}

/**
 * Converts a module path like ":core:ui" into a resource prefix like "core_ui_".
 */
private fun String.toModuleResourcePrefix() = split("""\W""".toRegex())
    .drop(1)
    .distinct()
    .joinToString(separator = "_")
    .lowercase() + "_"