package com.music.dzr.core.network.api

import com.music.dzr.core.network.model.NetworkResponse
import com.music.dzr.core.network.model.PaginatedList
import com.music.dzr.core.network.model.Radio
import com.music.dzr.core.network.model.RadioBrief
import com.music.dzr.core.network.model.RadioTrackBrief
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Retrofit API interface for Deezer radio endpoints
 *
 * @see <a href="https://developers.deezer.com/api/radio">Deezer Radio API Documentation</a>
 */
internal interface RadioApi {

    /**
     * Retrieves all available radio stations.
     */
    @GET("radio")
    suspend fun getRadios(): NetworkResponse<PaginatedList<RadioBrief>>

    /**
     * Retrieves detailed information about a specific radio station.
     */
    @GET("radio/{id}")
    suspend fun getRadio(@Path("id") radioId: Long): NetworkResponse<Radio>

    /**
     * Retrieves radio stations organized by genre.
     */
    @GET("radio/genres")
    suspend fun getRadioGenres(): NetworkResponse<PaginatedList<RadioBrief>>

    /**
     * Retrieves the top radio stations (default: 25 radios).
     */
    @GET("radio/top")
    suspend fun getTopRadios(): NetworkResponse<PaginatedList<RadioBrief>>

    /**
     * Retrieves the first 40 tracks from a specific radio station.
     */
    @GET("radio/{id}/tracks")
    suspend fun getRadioTracks(
        @Path("id") radioId: Long
    ): NetworkResponse<PaginatedList<RadioTrackBrief>>

    /**
     * Retrieves personal radio lists organized by genre (similar to MIX on website).
     */
    @GET("radio/lists")
    suspend fun getRadioLists(): NetworkResponse<PaginatedList<RadioBrief>>

}