package com.music.dzr.core.network.api

import com.music.dzr.core.network.model.MarketsContainer
import com.music.dzr.core.network.model.NetworkResponse
import retrofit2.http.GET

/**
 * A service for interacting with the Spotify Player API.
 * @see <a href="https://developer.spotify.com/documentation/web-api/">Spotify API</a>
 */
interface MarketApi {

    /**
     * Get the list of markets where Spotify is available.
     */
    @GET("markets")
    suspend fun getAvailableMarkets(): NetworkResponse<MarketsContainer>
}