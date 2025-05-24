package com.music.dzr.core.network.retrofiit

import com.music.dzr.core.network.model.Album
import com.music.dzr.core.network.model.AlbumTrack
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
 * Retrofit API interface for Deezer API endpoints.
 *
 * This interface defines all available endpoints for interacting with the Deezer API,
 * including album, artist, chart, editorial, genre, playlist, radio, search, track,
 * and user endpoints with both read and write operations.
 *
 * @see <a href="https://developers.deezer.com/api">Deezer API Documentation</a>
 */
private interface RetrofitDzrNetworkApi {

    // ========== ALBUM ENDPOINTS ========== //

    /**
     * Retrieves detailed information about a specific album.
     *
     * @param albumId The unique identifier of the album
     * @return An [Album] object containing album details
     */
    @GET("album/{id}")
    suspend fun getAlbum(@Path("id") albumId: Long): Album

    /**
     * Retrieves all tracks from a specific album.
     *
     * @param albumId The unique identifier of the album
     * @return A paginated list of [AlbumTrack] objects representing the album's tracks
     */
    @GET("album/{id}/tracks")
    suspend fun getAlbumTracks(@Path("id") albumId: Long): PaginatedList<AlbumTrack>

    // ========== ARTIST ENDPOINTS ========== //

    /**
     * Retrieves detailed information about a specific artist.
     *
     * @param artistId The unique identifier of the artist
     * @return A [Artist] object containing artist details
     */
    @GET("artist/{id}")
    suspend fun getArtist(@Path("id") artistId: Long): Artist

    /**
     * Retrieves the top 5 tracks of an artist.
     *
     * @param artistId The unique identifier of the artist
     * @return A paginated list of the artist's most popular [ArtistTopTrack] objects
     */
    @GET("artist/{id}/top")
    suspend fun getArtistTopTracks(@Path("id") artistId: Long): PaginatedList<ArtistTopTrack>

    /**
     * Retrieves all albums by a specific artist.
     *
     * @param artistId The unique identifier of the artist
     * @return A paginated list of [ArtistAlbum] objects representing the artist's albums
     */
    @GET("artist/{id}/albums")
    suspend fun getArtistAlbums(@Path("id") artistId: Long): PaginatedList<ArtistAlbum>

    /**
     * Retrieves artists related to the specified artist.
     *
     * @param artistId The unique identifier of the artist
     * @return A paginated list of related [Artist] objects
     */
    @GET("artist/{id}/related")
    suspend fun getRelatedArtists(@Path("id") artistId: Long): PaginatedList<Artist>

    /**
     * Retrieves radio tracks based on an artist's style.
     *
     * @param artistId The unique identifier of the artist
     * @return A list of [TrackBrief] objects for the artist's radio
     */
    @GET("artist/{id}/radio")
    suspend fun getArtistRadioTracks(@Path("id") artistId: Long): WholeList<TrackBrief>

    /**
     * Retrieves playlists created by or featuring the artist.
     *
     * @param artistId The unique identifier of the artist
     * @return A paginated list of [ArtistPlaylist] objects associated with the artist
     */
    @GET("artist/{id}/playlists")
    suspend fun getArtistPlaylists(@Path("id") artistId: Long): PaginatedList<ArtistPlaylist>

}

private const val DZR_BASE_URL = "https://api.deezer.com/"