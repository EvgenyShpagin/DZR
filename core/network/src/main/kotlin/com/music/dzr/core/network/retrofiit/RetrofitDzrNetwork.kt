package com.music.dzr.core.network.retrofiit

import com.music.dzr.core.network.model.Album
import com.music.dzr.core.network.model.AlbumTrack
import com.music.dzr.core.network.model.Artist
import com.music.dzr.core.network.model.ArtistAlbum
import com.music.dzr.core.network.model.ArtistPlaylist
import com.music.dzr.core.network.model.ArtistTopTrack
import com.music.dzr.core.network.model.Chart
import com.music.dzr.core.network.model.ChartAlbum
import com.music.dzr.core.network.model.ChartArtist
import com.music.dzr.core.network.model.ChartPlaylist
import com.music.dzr.core.network.model.ChartTrack
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

    // ========== CHART ENDPOINTS ==========

    /**
     * Retrieves general charts for a specified genre.
     *
     * @return A [Chart] object containing various chart data
     */
    @GET("chart")
    suspend fun getCharts(): Chart

    /**
     * Retrieves the top tracks from charts.
     *
     * @param genreId The genre identifier (default: 0 for all genres)
     * @return A paginated list of top [ChartTrack] objects
     */
    @GET("chart/{genre_id}/tracks")
    suspend fun getTopTracks(@Path("genre_id") genreId: Int = 0): PaginatedList<ChartTrack>

    /**
     * Retrieves the top albums from charts.
     *
     * @param genreId The genre identifier (default: 0 for all genres)
     * @return A paginated list of top [ChartAlbum] objects
     */
    @GET("chart/{genre_id}/albums")
    suspend fun getTopAlbums(@Path("genre_id") genreId: Int = 0): PaginatedList<ChartAlbum>

    /**
     * Retrieves the top artists from charts.
     *
     * @param genreId The genre identifier (default: 0 for all genres)
     * @return A paginated list of top [ChartArtist] objects
     */
    @GET("chart/{genre_id}/artists")
    suspend fun getTopArtists(@Path("genre_id") genreId: Int = 0): PaginatedList<ChartArtist>

    /**
     * Retrieves the top playlists from charts.
     *
     * @param genreId The genre identifier (default: 0 for all genres)
     * @return A paginated list of top [ChartPlaylist] objects
     */
    @GET("chart/{genre_id}/playlists")
    suspend fun getTopPlaylists(@Path("genre_id") genreId: Int = 0): PaginatedList<ChartPlaylist>

}

private const val DZR_BASE_URL = "https://api.deezer.com/"