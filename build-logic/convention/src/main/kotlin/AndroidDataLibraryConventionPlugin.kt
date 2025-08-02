import com.music.dzr.implementation
import com.music.dzr.libs
import com.music.dzr.testImplementation
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.dependencies

/**
 * Convention plugin for `library:*:data` modules.
 */
class AndroidDataLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            apply(plugin = "dzr.android.library")
            apply(plugin = "org.jetbrains.kotlin.plugin.serialization")
            apply(plugin = "dzr.koin")

            dependencies {
                implementation(libs.findLibrary("retrofit.core"))
                implementation(libs.findLibrary("kotlinx.serialization.json"))
                implementation(libs.findLibrary("retrofit.kotlin.serialization"))
                implementation(project(":core:network"))

                testImplementation(project("core:testing"))
                testImplementation(libs.findLibrary("junit"))
                testImplementation(libs.findLibrary("mockk"))
                testImplementation(libs.findLibrary("kotlinx.coroutines.test"))
                testImplementation(libs.findLibrary("mockwebserver"))
            }
        }
    }
}