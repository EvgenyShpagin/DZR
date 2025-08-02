package com.music.dzr.core.network.model

import com.music.dzr.core.network.serialization.ReleaseDateSerializer
import kotlinx.serialization.Serializable

/**
 * The date the object was first released.
 */
@Serializable(with = ReleaseDateSerializer::class)
data class ReleaseDate(
    val year: Int,
    val month: Int?,
    val day: Int?
)