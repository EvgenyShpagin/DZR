package com.music.dzr.core.model.shared

/**
 * Domain representation of an image.
 */
data class Image(
    val url: String,
    val width: Int?,
    val height: Int?
)
