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
                implementation(libs.findLibrary("retrofit.core").get())
                implementation(libs.findLibrary("kotlinx.serialization.json").get())
                implementation(libs.findLibrary("retrofit.kotlin.serialization").get())
                implementation(project(":core:network"))

                testImplementation(project(":core:testing"))
                testImplementation(libs.findLibrary("junit").get())
                testImplementation(libs.findLibrary("mockk").get())
                testImplementation(libs.findLibrary("kotlinx.coroutines.test").get())
                testImplementation(libs.findLibrary("mockwebserver").get())
            }
        }
    }
}