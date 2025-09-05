package com.music.dzr.library.artist.domain.repository

import com.music.dzr.core.error.AppError
import com.music.dzr.core.model.DetailedArtist
import com.music.dzr.core.model.DetailedTrack
import com.music.dzr.core.model.Market
import com.music.dzr.core.model.ReleaseType
import com.music.dzr.core.pagination.OffsetPageable
import com.music.dzr.core.pagination.Page
import com.music.dzr.core.result.Result
import com.music.dzr.library.artist.domain.model.AlbumInDiscography

/**
 * Repository for artist-related operations.
 *
 * All methods return a [Result] and can fail with:
 * - [com.music.dzr.core.error.ConnectivityError] for network connection issues.
 * - [com.music.dzr.core.error.NetworkError] for API-related problems (e.g., authorization, server errors).
 */
interface ArtistRepository {

    /**
     * Get detailed information about a single artist.
     */
    suspend fun getArtist(
        id: String
    ): Result<DetailedArtist, AppError>

    /**
     * Get detailed information for several artists.
     */
    suspend fun getMultipleArtists(
        ids: List<String>
    ): Result<List<DetailedArtist>, AppError>

    /**
     * Get an artist's albums (discography).
     *
     * @param releaseTypes Optional. Filter by release types.
     * @param includeAppearsOn Optional. Include albums where artist is appearing on.
     * @param market The market to filter content by. Overridden by authenticated user's market.
     * @param pageable Offset-based pagination parameters (limit and offset).
     */
    suspend fun getArtistAlbums(
        id: String,
        releaseTypes: List<ReleaseType> = ReleaseType.entries,
        includeAppearsOn: Boolean = false,
        market: Market = Market.Unspecified,
        pageable: OffsetPageable = OffsetPageable.Default
    ): Result<Page<AlbumInDiscography>, AppError>

    /**
     * Get an artist's top tracks.
     *
     * @param market The market to filter content by. Overridden by authenticated user's market.
     */
    suspend fun getArtistTopTracks(
        id: String,
        market: Market = Market.Unspecified
    ): Result<List<DetailedTrack>, AppError>
}
