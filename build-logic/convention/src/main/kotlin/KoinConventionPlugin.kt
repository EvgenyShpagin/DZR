import com.android.build.gradle.api.AndroidBasePlugin
import com.music.dzr.androidTestImplementation
import com.music.dzr.implementation
import com.music.dzr.libs
import com.music.dzr.testImplementation
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class KoinConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            val bom = libs.findLibrary("koin.bom").get()
            dependencies {
                implementation(platform(bom))
                testImplementation(platform(bom))
                testImplementation(libs.findLibrary("koin.test").get())
            }

            // Add support for Jvm Modules
            pluginManager.withPlugin("org.jetbrains.kotlin.jvm") {
                dependencies {
                    implementation(libs.findLibrary("koin.core").get())
                }
            }

            /** Add support for Android modules (based on [AndroidBasePlugin]) */
            pluginManager.withPlugin("com.android.base") {
                dependencies {
                    implementation(libs.findLibrary("koin.android").get())
                    androidTestImplementation(platform(bom))
                }
            }
        }
    }
} 