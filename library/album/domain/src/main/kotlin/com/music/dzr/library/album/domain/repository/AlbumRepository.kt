package com.music.dzr.library.album.domain.repository

import com.music.dzr.core.error.AppError
import com.music.dzr.core.model.Market
import com.music.dzr.core.pagination.OffsetPageable
import com.music.dzr.core.pagination.Page
import com.music.dzr.core.result.Result
import com.music.dzr.library.album.domain.model.DetailedAlbum
import com.music.dzr.library.album.domain.model.TrackOnAlbum

/**
 * Repository for album-related operations.
 *
 * All methods return a [Result] and can fail with:
 * - [com.music.dzr.core.error.ConnectivityError] for network connection issues.
 * - [com.music.dzr.core.error.NetworkError] for API-related problems (e.g., authorization, server errors).
 */
interface AlbumRepository {

    /**
     * Get detailed information about a single album.
     *
     * @param id Album identifier to remove.
     * @param market The market to filter content by. Overridden by authenticated user's market.
     */
    suspend fun getAlbum(
        id: String,
        market: Market = Market.Unspecified
    ): Result<DetailedAlbum, AppError>

    /**
     * Get detailed information for several albums.
     *
     * @param ids Album identifiers to remove.
     */
    suspend fun getMultipleAlbums(
        ids: List<String>,
        market: Market = Market.Unspecified
    ): Result<List<DetailedAlbum>, AppError>

    /**
     * Get tracks for a given album.
     *
     * @param id Album identifier.
     * @param market The market to filter content by. Overridden by authenticated user's market.
     * @param pageable Offset-based pagination parameters (limit and offset).
     */
    suspend fun getAlbumTracks(
        id: String,
        market: Market = Market.Unspecified,
        pageable: OffsetPageable = OffsetPageable.Default
    ): Result<Page<TrackOnAlbum>, AppError>

    /**
     * Save one or more albums to the current user's library.
     *
     * @param ids Album identifiers to save.
     */
    suspend fun saveAlbumsForUser(
        ids: List<String>
    ): Result<Unit, AppError>

    /**
     * Remove one or more albums from the current user's library.
     *
     * @param ids Album identifiers to remove.
     */
    suspend fun removeAlbumsForUser(
        ids: List<String>
    ): Result<Unit, AppError>

    /**
     * Get a paginated list of the current user's saved albums.
     *
     * @param market The market to filter content by. Overridden by authenticated user's market.
     * @param pageable Offset-based pagination parameters (limit and offset).
     */
    suspend fun getUserSavedAlbums(
        market: Market = Market.Unspecified,
        pageable: OffsetPageable = OffsetPageable.Default
    ): Result<Page<DetailedAlbum>, AppError>
}
