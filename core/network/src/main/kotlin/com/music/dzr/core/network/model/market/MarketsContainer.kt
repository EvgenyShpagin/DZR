package com.music.dzr.core.network.model.market

import kotlinx.serialization.Serializable

/**
 * Response wrapper for getting multiple Markets
 * @property markets A markets object with an array of country codes
 * Example: `["CA","BR","IT"]`
 */
@Serializable
data class MarketsContainer(val markets: List<String>)