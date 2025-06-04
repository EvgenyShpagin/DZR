package com.music.dzr.core.network.api

import com.music.dzr.core.network.model.Artist
import com.music.dzr.core.network.model.NetworkResponse
import com.music.dzr.core.network.model.PaginatedList
import com.music.dzr.core.network.model.PlaylistBrief
import com.music.dzr.core.network.model.RadioBrief
import com.music.dzr.core.network.model.SearchAlbum
import com.music.dzr.core.network.model.SearchTrack
import com.music.dzr.core.network.model.SearchUser
import com.music.dzr.core.network.model.WholeList
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Retrofit API interface for Deezer search endpoints
 *
 * @see <a href="https://developers.deezer.com/api/search">Deezer Search API Documentation</a>
 */
internal interface SearchApi {

    /**
     * Searches for tracks across the Deezer catalog.
     */
    @GET("search")
    suspend fun searchTracks(
        @Query("q") query: String
    ): NetworkResponse<PaginatedList<SearchTrack>>

    /**
     * Searches for albums across the Deezer catalog.
     */
    @GET("search/album")
    suspend fun searchAlbums(
        @Query("q") query: String
    ): NetworkResponse<PaginatedList<SearchAlbum>>

    /**
     * Searches for artists across the Deezer catalog.
     */
    @GET("search/artist")
    suspend fun searchArtists(
        @Query("q") query: String
    ): NetworkResponse<PaginatedList<Artist>>

    /**
     * Retrieves user's search history.
     */
    @GET("search/history")
    suspend fun getSearchHistory(
        @Query("q") query: String
    ): NetworkResponse<WholeList<RadioBrief>>

    /**
     * Searches for playlists across the Deezer catalog.
     */
    @GET("search/playlist")
    suspend fun searchPlaylists(
        @Query("q") query: String
    ): NetworkResponse<PaginatedList<PlaylistBrief>>

    /**
     * Searches for radio stations across the Deezer catalog.
     */
    @GET("search/radio")
    suspend fun searchRadios(
        @Query("q") query: String
    ): NetworkResponse<PaginatedList<RadioBrief>>

    /**
     * Searches for users across the Deezer platform.
     */
    @GET("search/user")
    suspend fun searchUsers(
        @Query("q") query: String
    ): PaginatedList<SearchUser>

}