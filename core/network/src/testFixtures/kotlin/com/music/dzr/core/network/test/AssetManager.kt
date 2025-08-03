package com.music.dzr.core.network.test

import java.io.File
import java.io.InputStream
import java.util.Properties

/**
 * This class helps with loading Android `/assets` files, when running JVM unit tests.
 */
internal object AssetManager {
    private val config = requireNotNull(
        Thread.currentThread()
            .contextClassLoader
            ?.getResource("com/android/tools/test_config.properties")
    ) {
        """
            Missing Android resources properties file.
            Did you forget to enable the feature in the gradle build file?
            android.testOptions.unitTests.isIncludeAndroidResources = true
        """.trimIndent()
    }
    private val properties = Properties().apply { config.openStream().use(::load) }
    private val assets = File(properties["android_merged_assets"].toString())

    fun open(fileName: String): InputStream = File(assets, fileName).inputStream()
}