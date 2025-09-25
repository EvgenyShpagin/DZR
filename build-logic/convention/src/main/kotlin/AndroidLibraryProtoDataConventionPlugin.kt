import com.google.protobuf.gradle.ProtobufExtension
import com.music.dzr.androidTestImplementation
import com.music.dzr.implementation
import com.music.dzr.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

/**
 * Convention plugin for `Proto DataStore` required data layer modules (`library:*:data`, `core:data`).
 * Adds Protobuf and DataStore deps to base [AndroidLibraryDataConventionPlugin].
 */
class AndroidLibraryProtoDataConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            apply(plugin = "dzr.android.library.data")
            apply(plugin = "com.google.protobuf")

            extensions.configure<ProtobufExtension> {
                protoc {
                    artifact = libs.findLibrary("protobuf.protoc").get().get().toString()
                }

                generateProtoTasks {
                    all().forEach { task ->
                        task.builtins {
                            register("java") {
                                option("lite")
                            }
                            register("kotlin") {
                                option("lite")
                            }
                        }
                    }
                }
            }

            dependencies {
                implementation(libs.findLibrary("androidx.datastore").get())
                implementation(libs.findLibrary("protobuf.kotlin.lite").get())

                androidTestImplementation(project(":core:testing"))
                androidTestImplementation(libs.findLibrary("junit").get())
                androidTestImplementation(libs.findLibrary("kotlinx.coroutines.test").get())
                androidTestImplementation(libs.findLibrary("androidx-test-core-ktx").get())
            }
        }
    }
}