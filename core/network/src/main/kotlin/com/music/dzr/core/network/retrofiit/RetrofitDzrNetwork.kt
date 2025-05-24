package com.music.dzr.core.network.retrofiit

import com.music.dzr.core.network.model.Album
import com.music.dzr.core.network.model.AlbumTrack
import com.music.dzr.core.network.model.PaginatedList
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

}

private const val DZR_BASE_URL = "https://api.deezer.com/"