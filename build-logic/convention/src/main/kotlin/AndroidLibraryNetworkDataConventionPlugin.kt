import com.android.build.gradle.LibraryExtension
import com.music.dzr.implementation
import com.music.dzr.libs
import com.music.dzr.testImplementation
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

/**
 * Convention plugin for network-enabled data layer modules (`library:*:data`, `core:data`).
 * Adds Retrofit + Kotlin Serialization and core:network deps to base [AndroidLibraryDataConventionPlugin].
 */
class AndroidLibraryNetworkDataConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            apply(plugin = "dzr.android.library.data")
            apply(plugin = "org.jetbrains.kotlin.plugin.serialization")

            extensions.configure<LibraryExtension> {
                testOptions.unitTests.isIncludeAndroidResources = true
            }

            dependencies {
                implementation(libs.findLibrary("retrofit-core").get())
                implementation(libs.findLibrary("kotlinx-serialization-json").get())
                implementation(libs.findLibrary("retrofit-kotlin-serialization").get())
                implementation(project(":core:network"))

                testImplementation(libs.findLibrary("mockwebserver").get())
                testImplementation(testFixtures(project(":core:network")))
            }
        }
    }
}
