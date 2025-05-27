package com.music.dzr.core.network.api

import com.music.dzr.core.network.model.ApiResponse
import com.music.dzr.core.network.model.PaginatedList
import com.music.dzr.core.network.model.Playlist
import com.music.dzr.core.network.model.PlaylistTrack
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Retrofit API interface for Deezer playlist endpoints
 *
 * @see <a href="https://developers.deezer.com/api/playlist">Deezer Playlist API Documentation</a>
 */
internal interface PlaylistApi {

    /**
     * Retrieves detailed information about a specific playlist.
     */
    @GET("playlist/{id}")
    suspend fun getPlaylist(@Path("id") playlistId: Long): ApiResponse<Playlist>

    /**
     * Retrieves all tracks from a specific playlist.
     */
    @GET("playlist/{id}/tracks")
    suspend fun getPlaylistTracks(
        @Path("id") playlistId: Long
    ): ApiResponse<PaginatedList<PlaylistTrack>>

}