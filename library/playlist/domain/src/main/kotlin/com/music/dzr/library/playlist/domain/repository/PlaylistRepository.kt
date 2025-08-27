package com.music.dzr.library.playlist.domain.repository

import com.music.dzr.core.error.AppError
import com.music.dzr.core.model.Image
import com.music.dzr.core.pagination.Page
import com.music.dzr.core.result.Result
import com.music.dzr.library.playlist.domain.model.PagedPlaylist
import com.music.dzr.library.playlist.domain.model.Playlist
import com.music.dzr.library.playlist.domain.model.PlaylistDetails
import com.music.dzr.library.playlist.domain.model.PlaylistEntry
import com.music.dzr.library.playlist.domain.model.PlaylistVersion
import com.music.dzr.library.playlist.domain.model.SimplifiedPlaylist

/**
 * Repository for managing playlists.
 *
 * All methods in this repository return a [com.music.dzr.core.result.Result] and can fail with:
 * - [com.music.dzr.core.error.ConnectivityError] for network connection issues.
 * - [com.music.dzr.core.error.NetworkError] for API-related problems (e.g., authorization, server errors).
 * Specific domain errors are documented on a per-method basis.
 */
interface PlaylistRepository {

    /**
     * Get a playlist owned by a user.
     *
     * @param playlistId The ID of the playlist.
     * @param market Optional. An ISO 3166-1 alpha-2 country code.
     */
    suspend fun getPlaylist(
        playlistId: String,
        market: String? = null
    ): Result<PagedPlaylist, AppError>

    /**
     * Change playlist details (name, description, visibility, etc).
     *
     * @param playlistId The ID of the playlist.
     * @param details Changed playlist details.
     */
    suspend fun changePlaylistDetails(
        playlistId: String,
        details: PlaylistDetails
    ): Result<Unit, AppError>

    /**
     * Get full details of tracks in a playlist.
     *
     * @param playlistId The ID of the playlist.
     * @param market Optional. ISO country code.
     * @param limit Max number of items to return.
     * @param offset Index of the first item to return.
     */
    suspend fun getPlaylistTracks(
        playlistId: String,
        market: String? = null,
        limit: Int? = null,
        offset: Int? = null
    ): Result<Page<PlaylistEntry>, AppError>

    /**
     * Replace playlist items.
     */
    suspend fun replaceAll(
        playlistId: String,
        newItemIds: List<String>,
        playlistVersion: PlaylistVersion? = null
    ): Result<PlaylistVersion, AppError>

    /**
     * Reorder playlist items.
     */
    suspend fun moveRange(
        playlistId: String,
        fromIndex: Int,
        length: Int,
        toIndex: Int,
        playlistVersion: PlaylistVersion? = null
    ): Result<PlaylistVersion, AppError>

    /**
     * Add one or more tracks to a playlist.
     */
    suspend fun addTracksToPlaylist(
        playlistId: String,
        trackIds: List<String>,
        position: Int? = null
    ): Result<PlaylistVersion, AppError>

    /**
     * Remove one or more tracks from a playlist.
     */
    suspend fun removePlaylistTracks(
        playlistId: String,
        trackIds: List<String>,
        playlistVersion: PlaylistVersion? = null
    ): Result<PlaylistVersion, AppError>

    /**
     * Get the current user’s playlists.
     */
    suspend fun getCurrentUserPlaylists(
        limit: Int? = null,
        offset: Int? = null
    ): Result<Page<SimplifiedPlaylist>, AppError>

    /**
     * Get public playlists for a user.
     */
    suspend fun getUserPlaylists(
        userId: String,
        limit: Int? = null,
        offset: Int? = null
    ): Result<Page<Playlist>, AppError>

    /**
     * Create a new playlist.
     */
    suspend fun createPlaylist(
        userId: String,
        details: PlaylistDetails
    ): Result<Playlist, AppError>

    /**
     * Get the current image(s) for a playlist.
     */
    suspend fun getPlaylistCoverImage(
        playlistId: String
    ): Result<List<Image>, AppError>

    /**
     * Upload a custom playlist cover image.
     *
     * @param playlistId The ID of the playlist.
     * @param jpegImage Base64-encoded JPEG image data.
     */
    suspend fun uploadCustomPlaylistCover(
        playlistId: String,
        jpegImage: ByteArray
    ): Result<Unit, AppError>
}
