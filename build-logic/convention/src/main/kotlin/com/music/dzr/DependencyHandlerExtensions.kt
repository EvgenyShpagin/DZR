package com.music.dzr

import org.gradle.api.artifacts.dsl.DependencyHandler

fun DependencyHandler.implementation(dependencyNotation: Any) =
    add("implementation", dependencyNotation)


fun DependencyHandler.api(dependencyNotation: Any) =
    add("api", dependencyNotation)


fun DependencyHandler.coreLibraryDesugaring(dependencyNotation: Any) =
    add("coreLibraryDesugaring", dependencyNotation)


fun DependencyHandler.debugImplementation(dependencyNotation: Any) =
    add("debugImplementation", dependencyNotation)


fun DependencyHandler.testImplementation(dependencyNotation: Any) =
    add("testImplementation", dependencyNotation)


fun DependencyHandler.androidTestImplementation(dependencyNotation: Any) =
    add("androidTestImplementation", dependencyNotation)

fun DependencyHandler.screenshotTestImplementation(dependencyNotation: Any) =
    add("screenshotTestImplementation", dependencyNotation)
