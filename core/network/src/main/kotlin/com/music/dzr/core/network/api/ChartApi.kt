package com.music.dzr.core.network.api

import com.music.dzr.core.network.model.ApiResponse
import com.music.dzr.core.network.model.Chart
import com.music.dzr.core.network.model.ChartAlbum
import com.music.dzr.core.network.model.ChartArtist
import com.music.dzr.core.network.model.ChartPlaylist
import com.music.dzr.core.network.model.ChartTrack
import com.music.dzr.core.network.model.PaginatedList
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Retrofit API interface for Deezer chart endpoints
 *
 * @see <a href="https://developers.deezer.com/api/chart">Deezer Chart API Documentation</a>
 */
internal interface ChartApi {
    /**
     * Retrieves general charts for a specified genre.
     */
    @GET("chart")
    suspend fun getCharts(): ApiResponse<Chart>

    /**
     * Retrieves the top tracks from charts.
     */
    @GET("chart/{genre_id}/tracks")
    suspend fun getTopTracks(
        @Path("genre_id") genreId: Long = 0
    ): ApiResponse<PaginatedList<ChartTrack>>

    /**
     * Retrieves the top albums from charts.
     */
    @GET("chart/{genre_id}/albums")
    suspend fun getTopAlbums(
        @Path("genre_id") genreId: Long = 0
    ): ApiResponse<PaginatedList<ChartAlbum>>

    /**
     * Retrieves the top artists from charts.
     */
    @GET("chart/{genre_id}/artists")
    suspend fun getTopArtists(
        @Path("genre_id") genreId: Long = 0
    ): ApiResponse<PaginatedList<ChartArtist>>

    /**
     * Retrieves the top playlists from charts.
     */
    @GET("chart/{genre_id}/playlists")
    suspend fun getTopPlaylists(
        @Path("genre_id") genreId: Long = 0
    ): ApiResponse<PaginatedList<ChartPlaylist>>
}