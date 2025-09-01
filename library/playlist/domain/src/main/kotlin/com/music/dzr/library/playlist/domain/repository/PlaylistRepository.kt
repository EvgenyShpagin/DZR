package com.music.dzr.library.playlist.domain.repository

import com.music.dzr.core.error.AppError
import com.music.dzr.core.model.Image
import com.music.dzr.core.model.Market
import com.music.dzr.core.pagination.OffsetPageable
import com.music.dzr.core.pagination.Page
import com.music.dzr.core.result.Result
import com.music.dzr.library.playlist.domain.model.InsertPosition
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
     * @param market The market to filter content by. Overridden by authenticated user's market.
     */
    suspend fun getPlaylist(
        playlistId: String,
        market: Market = Market.Unspecified
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
     * @param market The market to filter content by. Overridden by authenticated user's market.
     * @param pageable Offset-based pagination parameters (limit and offset).
     */
    suspend fun getPlaylistTracks(
        playlistId: String,
        market: Market = Market.Unspecified,
        pageable: OffsetPageable = OffsetPageable.Default,
    ): Result<Page<PlaylistEntry>, AppError>

    /**
     * Replace playlist items.
     */
    suspend fun replaceAll(
        playlistId: String,
        newItemIds: List<String>,
        playlistVersion: PlaylistVersion = PlaylistVersion.Unspecified,
    ): Result<PlaylistVersion, AppError>

    /**
     * Reorder playlist items.
     */
    suspend fun moveRange(
        playlistId: String,
        fromIndex: Int,
        length: Int,
        toIndex: Int,
        playlistVersion: PlaylistVersion = PlaylistVersion.Unspecified,
    ): Result<PlaylistVersion, AppError>

    /**
     * Add one or more tracks to a playlist.
     */
    suspend fun addTracksToPlaylist(
        playlistId: String,
        trackIds: List<String>,
        position: InsertPosition = InsertPosition.Append
    ): Result<PlaylistVersion, AppError>

    /**
     * Remove one or more tracks from a playlist.
     */
    suspend fun removePlaylistTracks(
        playlistId: String,
        trackIds: List<String>,
        playlistVersion: PlaylistVersion = PlaylistVersion.Unspecified,
    ): Result<PlaylistVersion, AppError>

    /**
     * Get the current user’s playlists.
     *
     * @param pageable Offset-based pagination parameters (limit and offset).
     */
    suspend fun getCurrentUserPlaylists(
        pageable: OffsetPageable = OffsetPageable.Default,
    ): Result<Page<SimplifiedPlaylist>, AppError>

    /**
     * Get public playlists for a user.
     *
     * @param pageable Offset-based pagination parameters (limit and offset).
     */
    suspend fun getUserPlaylists(
        userId: String,
        pageable: OffsetPageable = OffsetPageable.Default,
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
