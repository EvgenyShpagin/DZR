package com.music.dzr.core.model.album

/**
 * Album release date with variable precision.
 */
data class ReleaseDate(
    val year: Int,
    val month: Int?,
    val day: Int?
)
