package com.music.dzr.core.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents the "linked_from" object when a track has been replaced by another.
 */
@Serializable
data class LinkedFrom(
    @SerialName("external_urls") val externalUrls: ExternalUrls,
    val href: String,
    val id: String,
    val type: String,
    val uri: String
)