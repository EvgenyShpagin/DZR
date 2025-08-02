package com.music.dzr.library.artist.data.remote.dto

import com.music.dzr.core.network.model.ExternalUrls
import com.music.dzr.core.network.model.Followers
import com.music.dzr.core.network.model.Image
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents a Spotify artist with full profile information
 */
@Serializable
data class Artist(
    @SerialName("external_urls") val externalUrls: ExternalUrls,
    val followers: Followers,
    val genres: List<String>,
    val href: String,
    val id: String,
    val images: List<Image>,
    val name: String,
    val popularity: Int,
    val type: String,
    val uri: String
) 