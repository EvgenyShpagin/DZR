plugins {
    alias(libs.plugins.dzr.android.feature)
}

android {
    namespace = "com.music.dzr.feature.search"

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
    }
}

dependencies {
    implementation(projects.core.network)
    implementation(libs.retrofit.core)

    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.mockwebserver)
    testImplementation(testFixtures(projects.core.network))
}