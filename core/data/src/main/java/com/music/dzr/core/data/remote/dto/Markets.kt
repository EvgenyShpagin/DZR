package com.music.dzr.core.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Response wrapper for getting multiple Markets
 * @property list A markets object with an array of country codes
 * Example: `["CA","BR","IT"]`
 */
@Serializable
data class Markets(
    @SerialName("markets")
    val list: List<String>
)