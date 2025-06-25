package com.music.dzr.core.network.model.artist

import kotlinx.serialization.Serializable

/**
 * Response wrapper for getting multiple artists
 */
@Serializable
data class ArtistContainer(
    val artists: List<Artist>
) 