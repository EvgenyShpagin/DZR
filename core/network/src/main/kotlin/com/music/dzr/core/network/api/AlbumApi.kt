package com.music.dzr.core.network.api

import com.music.dzr.core.network.model.Album
import com.music.dzr.core.network.model.AlbumTrack
import com.music.dzr.core.network.model.ApiResponse
import com.music.dzr.core.network.model.PaginatedList
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Retrofit API interface for Deezer album endpoints
 *
 * @see <a href="https://developers.deezer.com/api/album">Deezer Album API Documentation</a>
 */
internal interface AlbumApi {

    /**
     * Retrieves detailed information about a specific album.
     */
    @GET("album/{id}")
    suspend fun getAlbum(@Path("id") albumId: Long): ApiResponse<Album>

    /**
     * Retrieves all tracks from a specific album.
     */
    @GET("album/{id}/tracks")
    suspend fun getAlbumTracks(@Path("id") albumId: Long): ApiResponse<PaginatedList<AlbumTrack>>
}