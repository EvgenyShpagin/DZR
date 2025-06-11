package com.music.dzr.core.network.api

import com.music.dzr.core.network.model.MarketsContainer
import com.music.dzr.core.network.model.NetworkResponse
import retrofit2.http.GET

interface MarketApi {

    @GET("markets")
    suspend fun getAvailableMarkets(): NetworkResponse<MarketsContainer>
}