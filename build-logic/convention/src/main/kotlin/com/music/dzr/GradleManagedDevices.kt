@file:Suppress("UnstableApiUsage")

package com.music.dzr

import com.android.build.api.dsl.CommonExtension
import com.android.build.api.dsl.ManagedVirtualDevice
import org.gradle.kotlin.dsl.get
import org.gradle.kotlin.dsl.invoke

/**
 * Configure Gradle Managed Devices for consistent, automated testing.
 * This function defines a set of virtual devices (emulators) that Gradle can
 * automatically manage for running instrumentation tests.
 *
 * It creates a group named "allDevices" that includes all defined emulators,
 * allowing you to run tests on all of them with a single command.
 */
internal fun configureGradleManagedDevices(
    commonExtension: CommonExtension<*, *, *, *, *, *>,
) {
    // Defines the list of virtual devices to be used for testing.
    val devices = listOf(
        DeviceConfig("Pixel 9", 35, "aosp-atd"),
        DeviceConfig("Small Phone", 24, "aosp-atd"),
    )

    commonExtension.testOptions {
        managedDevices {
            allDevices {
                devices.forEach { deviceConfig ->
                    maybeCreate(deviceConfig.taskName, ManagedVirtualDevice::class.java).apply {
                        device = deviceConfig.device
                        apiLevel = deviceConfig.apiLevel
                        systemImageSource = deviceConfig.systemImageSource
                    }
                }
            }
            groups {
                // Create a group that includes all the devices defined above.
                // Running `./gradlew allDevicesDebugAndroidTest` will execute tests on all these emulators.
                maybeCreate("allDevices").apply {
                    devices.forEach { deviceConfig ->
                        targetDevices.add(allDevices[deviceConfig.taskName])
                    }
                }
            }
        }
    }
}

private data class DeviceConfig(
    val device: String,
    val apiLevel: Int,
    val systemImageSource: String,
) {
    // A unique name for the Gradle task, e.g., "pixel6api31aosp"
    val taskName = buildString {
        append(device.lowercase().replace(" ", ""))
        append("api")
        append(apiLevel.toString())
        append(systemImageSource.replace("-", ""))
    }
} 