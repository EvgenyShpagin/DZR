package com.music.dzr.core.network.model.album

import kotlinx.serialization.Serializable

/**
 * Response wrapper for getting multiple albums.
 */
@Serializable
data class MultipleAlbumsContainer(
    val albums: List<Album>
)