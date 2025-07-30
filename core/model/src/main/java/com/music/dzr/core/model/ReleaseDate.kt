package com.music.dzr.core.model

/**
 * Album release date with variable precision.
 */
data class ReleaseDate(
    val year: Int,
    val month: Int?,
    val day: Int?
)
