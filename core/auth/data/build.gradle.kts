plugins {
    alias(libs.plugins.dzr.android.library.network.data)
    alias(libs.plugins.protobuf)
}

android {
    namespace = "com.music.dzr.core.auth.data"

    defaultConfig {
        buildConfigField("String", "SPOTIFY_AUTH_BASE_URL ", "\"https://accounts.spotify.com/\"")
    }

    buildFeatures {
        buildConfig = true
    }
}

protobuf {
    protoc {
        artifact = libs.protobuf.protoc.get().toString()
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
    implementation(projects.core.auth.domain)
    implementation(projects.dzr.core.data)
    implementation(libs.okhttp.logging)
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.androidx.datastore)
    implementation(libs.protobuf.kotlin.lite)
    androidTestImplementation(libs.androidx.junit)
}