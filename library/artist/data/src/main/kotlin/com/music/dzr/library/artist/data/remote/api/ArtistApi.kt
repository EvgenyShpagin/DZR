package com.music.dzr.library.artist.data.remote.api

import com.music.dzr.core.network.dto.AlbumGroup
import com.music.dzr.core.network.dto.Artist
import com.music.dzr.core.network.dto.NetworkResponse
import com.music.dzr.core.network.dto.PaginatedList
import com.music.dzr.core.network.dto.Tracks
import com.music.dzr.library.artist.data.remote.dto.ArtistAlbum
import com.music.dzr.library.artist.data.remote.dto.Artists
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * A service for interacting with the Spotify Artist API.
 * @see <a href="https://developer.spotify.com/documentation/web-api/">Spotify API</a>
 */
internal interface ArtistApi {

    /**
     * Get Spotify catalog information for a single artist identified by their unique Spotify ID.
     *
     * @param id The Spotify ID of the artist to retrieve.
     */
    @GET("artists/{id}")
    suspend fun getArtist(
        @Path("id") id: String
    ): NetworkResponse<Artist>

    /**
     * Get Spotify catalog information for several artists based on their Spotify IDs.
     *
     * @param ids A comma-separated list of Spotify artist IDs (maximum of 50).
     */
    @GET("artists")
    suspend fun getMultipleArtists(
        @Query("ids") ids: String
    ): NetworkResponse<Artists>

    /**
     * Get Spotify catalog information about an artist's albums.
     *
     * @param id             The Spotify ID of the artist whose albums are to be retrieved.
     * @param includeGroups  A comma-separated list of [AlbumGroup].
     * @param market         An ISO 3166-1 alpha-2 country code. Filters the response to only contain albums available in that market.
     * @param limit          The maximum number of items to return. Default: 20. Minimum: 1. Maximum: 50.
     * @param offset         The index of the first album to return. Used for pagination.
     */
    @GET("artists/{id}/albums")
    suspend fun getArtistAlbums(
        @Path("id") id: String,
        @Query("include_groups") includeGroups: String? = null,
        @Query("market") market: String? = null,
        @Query("limit") limit: Int? = null,
        @Query("offset") offset: Int? = null
    ): NetworkResponse<PaginatedList<ArtistAlbum>>

    /**
     * Get Spotify catalog information about an artist's top tracks by country.
     *
     * @param id     The Spotify ID of the artist whose top tracks are to be retrieved.
     * @param market An ISO 3166-1 alpha-2 country code. Required. Filters the response to only contain tracks available in that market.
     */
    @GET("artists/{id}/top-tracks")
    suspend fun getArtistTopTracks(
        @Path("id") id: String,
        @Query("market") market: String? = null
    ): NetworkResponse<Tracks>
}