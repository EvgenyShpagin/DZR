import com.android.build.gradle.LibraryExtension
import com.music.dzr.libs
import com.music.dzr.screenshotTestImplementation
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

class ScreenshotConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            apply(plugin = "com.android.compose.screenshot")

            extensions.configure<LibraryExtension> {
                @Suppress("UnstableApiUsage")
                experimentalProperties["android.experimental.enableScreenshotTest"] = true
            }

            dependencies {
                screenshotTestImplementation(libs.findLibrary("androidx-compose-ui-tooling").get())
                screenshotTestImplementation(libs.findLibrary("screenshot-validation-api").get())
            }
        }
    }
}