package com.music.dzr.core.network.api

import com.music.dzr.core.network.model.NetworkResponse
import com.music.dzr.core.network.model.Artist
import com.music.dzr.core.network.model.ArtistAlbum
import com.music.dzr.core.network.model.ArtistPlaylist
import com.music.dzr.core.network.model.ArtistTopTrack
import com.music.dzr.core.network.model.PaginatedList
import com.music.dzr.core.network.model.TrackBrief
import com.music.dzr.core.network.model.WholeList
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Retrofit API interface for Deezer artist endpoints
 *
 * @see <a href="https://developers.deezer.com/api/artist">Deezer Artist API Documentation</a>
 */
internal interface ArtistApi {

    /**
     * Retrieves detailed information about a specific artist.
     */
    @GET("artist/{id}")
    suspend fun getArtist(@Path("id") artistId: Long): NetworkResponse<Artist>

    /**
     * Retrieves the top 5 tracks of an artist.
     */
    @GET("artist/{id}/top")
    suspend fun getArtistTopTracks(
        @Path("id") artistId: Long
    ): NetworkResponse<PaginatedList<ArtistTopTrack>>

    /**
     * Retrieves all albums by a specific artist.
     */
    @GET("artist/{id}/albums")
    suspend fun getArtistAlbums(
        @Path("id") artistId: Long
    ): NetworkResponse<PaginatedList<ArtistAlbum>>

    /**
     * Retrieves artists related to the specified artist.
     */
    @GET("artist/{id}/related")
    suspend fun getRelatedArtists(
        @Path("id") artistId: Long
    ): NetworkResponse<PaginatedList<Artist>>

    /**
     * Retrieves radio tracks based on an artist's style.
     */
    @GET("artist/{id}/radio")
    suspend fun getArtistRadioTracks(
        @Path("id") artistId: Long
    ): NetworkResponse<WholeList<TrackBrief>>

    /**
     * Retrieves playlists created by or featuring the artist.
     */
    @GET("artist/{id}/playlists")
    suspend fun getArtistPlaylists(
        @Path("id") artistId: Long
    ): NetworkResponse<PaginatedList<ArtistPlaylist>>
}