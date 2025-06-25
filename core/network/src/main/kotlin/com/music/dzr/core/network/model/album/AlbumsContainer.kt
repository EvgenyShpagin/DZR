package com.music.dzr.core.network.model.album

import kotlinx.serialization.Serializable

/**
 * Response wrapper for getting multiple albums.
 */
@Serializable
data class AlbumsContainer(
    val albums: List<Album>
)