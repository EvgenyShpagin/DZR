package com.music.dzr.core.network.model.market

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Response wrapper for getting multiple Markets
 * @property items A markets object with an array of country codes
 * Example: `["CA","BR","IT"]`
 */
@Serializable
data class Markets(
    @SerialName("markets")
    val items: List<String>
)