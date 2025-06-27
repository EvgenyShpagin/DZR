import com.android.build.api.dsl.ApplicationExtension
import com.music.dzr.configureGradleManagedDevices
import com.music.dzr.configureKotlinAndroid
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure

class AndroidApplicationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            apply(plugin = "com.android.application")
            apply(plugin = "org.jetbrains.kotlin.android")

            extensions.configure<ApplicationExtension> {
                configureKotlinAndroid(this)
                defaultConfig.targetSdk = 35
                // Disable animations during instrumented tests run from the command line
                testOptions.animationsDisabled = true
                configureGradleManagedDevices(this)
            }
        }
    }
}