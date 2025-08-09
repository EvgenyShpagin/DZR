import com.android.build.gradle.LibraryExtension
import com.music.dzr.libs
import com.music.dzr.testImplementation
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

/**
 * Base convention plugin for data layer modules (`library:*:data`, `core:data`).
 * Keeps common Android + test configuration.
 */
class AndroidLibraryDataConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            apply(plugin = "dzr.android.library")
            apply(plugin = "dzr.koin")

            extensions.configure<LibraryExtension> {
                testOptions.unitTests.isIncludeAndroidResources = true
            }

            dependencies {
                testImplementation(project(":core:testing"))
                testImplementation(libs.findLibrary("junit").get())
                testImplementation(libs.findLibrary("mockk").get())
                testImplementation(libs.findLibrary("kotlinx.coroutines.test").get())
            }
        }
    }
}
