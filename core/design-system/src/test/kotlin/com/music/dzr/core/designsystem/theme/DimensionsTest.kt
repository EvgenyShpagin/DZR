package com.music.dzr.core.designsystem.theme

import androidx.window.core.layout.WindowSizeClass
import kotlin.test.Test
import kotlin.test.assertEquals

class DimensionsTest {

    @Test
    fun resolveDimensions_returnsCompact_whenPhoneInPortrait() {
        // Arrange
        val sizeClass = WindowSizeClass(
            widthDp = 390f,
            heightDp = 844f
        )
        // Act
        val dimensions = sizeClass.resolveDimensions()
        // Assert
        assertEquals(CompactDimensions, dimensions)
    }

    @Test
    fun resolveDimensions_returnsCompact_whenPhoneInLandscape() {
        // Arrange
        val sizeClass = WindowSizeClass(
            widthDp = 844f,
            heightDp = 390f
        )
        // Act
        val dimensions = sizeClass.resolveDimensions()
        // Assert
        assertEquals(CompactDimensions, dimensions)
    }

    @Test
    fun resolveDimensions_returnsLarge_whenTabletInPortrait() {
        // Arrange
        val sizeClass = WindowSizeClass(
            widthDp = 800f,
            heightDp = 1280f
        )
        // Act
        val dimensions = sizeClass.resolveDimensions()
        // Assert
        assertEquals(LargeDimensions, dimensions)
    }

    @Test
    fun resolveDimensions_returnsLarge_whenTabletInLandscape() {
        // Arrange
        val sizeClass = WindowSizeClass(
            widthDp = 1280f,
            heightDp = 800f
        )
        // Act
        val dimensions = sizeClass.resolveDimensions()
        // Assert
        assertEquals(LargeDimensions, dimensions)
    }

    @Test
    fun resolveDimensions_returnsLarge_whenDesktop() {
        // Arrange
        val sizeClass = WindowSizeClass(
            widthDp = 1920f,
            heightDp = 1080f
        )
        // Act
        val dimensions = sizeClass.resolveDimensions()
        // Assert
        assertEquals(LargeDimensions, dimensions)
    }
}