package com.music.dzr.core.network.api

import com.music.dzr.core.network.model.ChangePlaylistDetailsRequest
import com.music.dzr.core.network.model.NetworkResponse
import com.music.dzr.core.network.model.PaginatedList
import com.music.dzr.core.network.model.Playlist
import com.music.dzr.core.network.model.PlaylistTrack
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * API for all things related to playlists.
 *
 * @see <a href="https://developer.spotify.com/documentation/web-api/">Spotify API</a>
 */
interface PlaylistApi {

    /**
     * Get a playlist owned by a Spotify user.
     *
     * @param playlistId The Spotify ID of the playlist.
     * @param market An [ISO 3166-1 alpha-2](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2) country code.
     * @param fields Filters for the query.
     * @return A [Playlist] object.
     */
    @GET("playlists/{playlist_id}")
    suspend fun getPlaylist(
        @Path("playlist_id") playlistId: String,
        @Query("market") market: String? = null,
        @Query("fields") fields: String? = null
    ): NetworkResponse<Playlist>

    /**
     * Change a playlist's name and public/private state. (The user must, of course, own the playlist.)
     *
     * @param playlistId The Spotify ID of the playlist.
     * @param body The request body.
     */
    @PUT("playlists/{playlist_id}")
    suspend fun changePlaylistDetails(
        @Path("playlist_id") playlistId: String,
        @Body body: ChangePlaylistDetailsRequest
    ): NetworkResponse<Unit>

    /**
     * Get full details of the items of a playlist owned by a Spotify user.
     *
     * @param playlistId The [Spotify ID] of the playlist.
     * @param market An [ISO 3166-1 alpha-2](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2) country code.
     * @param fields Filters for the query.
     * @param limit The maximum number of items to return.
     * @param offset The index of the first item to return.
     */
    @GET("playlists/{playlist_id}/tracks")
    suspend fun getPlaylistTracks(
        @Path("playlist_id") playlistId: String,
        @Query("market") market: String? = null,
        @Query("fields") fields: String? = null,
        @Query("limit") limit: Int? = null,
        @Query("offset") offset: Int? = null
    ): NetworkResponse<PaginatedList<PlaylistTrack>>

} 