import com.music.dzr.androidTestImplementation
import com.music.dzr.implementation
import com.music.dzr.libs
import com.music.dzr.testImplementation
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.dependencies

class AndroidFeatureConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            apply(plugin = "dzr.android.library")
            apply(plugin = "org.jetbrains.kotlin.plugin.serialization")
            apply(plugin = "dzr.koin")

            dependencies {
                implementation(project(":core:ui"))
                implementation(project(":core:design-system"))

                implementation(libs.findLibrary("androidx.lifecycle.runtime.compose").get())
                implementation(libs.findLibrary("androidx.lifecycle.viewModel.compose").get())
                implementation(libs.findLibrary("koin.androidx.compose").get())
                implementation(libs.findLibrary("koin.androidx.compose.navigation").get())
                implementation(libs.findLibrary("kotlinx.serialization.json").get())
                implementation(libs.findLibrary("androidx.navigation.compose").get())

                testImplementation(libs.findLibrary("androidx.navigation.testing").get())
                androidTestImplementation(
                    libs.findLibrary("androidx.lifecycle.runtime.testing").get()
                )
            }
        }
    }
}