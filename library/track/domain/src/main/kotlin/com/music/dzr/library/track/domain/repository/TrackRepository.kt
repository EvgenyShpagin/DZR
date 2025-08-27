package com.music.dzr.library.track.domain.repository

import com.music.dzr.core.error.AppError
import com.music.dzr.core.model.DetailedTrack
import com.music.dzr.core.model.Market
import com.music.dzr.core.pagination.Page
import com.music.dzr.core.result.Result
import com.music.dzr.library.track.domain.model.SavedTrack
import com.music.dzr.library.track.domain.model.TimestampedId

/**
 * Provides access to track-related data.
 *
 * All methods in this repository return a [com.music.dzr.core.result.Result] and can fail with:
 * - [com.music.dzr.core.error.ConnectivityError] for network connection issues.
 * - [com.music.dzr.core.error.NetworkError] for API-related problems (e.g., authorization, server errors).
 * Specific domain errors are documented on a per-method basis.
 */
interface TrackRepository {

    /**
     * Fetch track information by its ID.
     *
     * @param id The track ID
     * @param market The market to filter content by. Overridden by authenticated user's market.
     */
    suspend fun getTrack(
        id: String,
        market: Market? = null
    ): Result<DetailedTrack, AppError>

    /**
     * Fetch information for multiple tracks.
     *
     * @param ids List of track IDs (maximum 50)
     * @param market The market to filter content by. Overridden by authenticated user's market.
     */
    suspend fun getMultipleTracks(
        ids: List<String>,
        market: Market? = null
    ): Result<List<DetailedTrack>, AppError>

    /**
     * Get the current user's saved tracks.
     *
     * @param limit Maximum number of items to return (1..50)
     * @param offset The index of the first item to return
     * @param market The market to filter content by. Overridden by authenticated user's market.
     */
    suspend fun getUserSavedTracks(
        limit: Int? = null,
        offset: Int? = null,
        market: Market? = null
    ): Result<Page<SavedTrack>, AppError>

    /**
     * Save one or more tracks to the user's library.
     *
     * @param ids List of track IDs (maximum 50)
     */
    suspend fun saveTracksForUser(ids: List<String>): Result<Unit, AppError>

    /**
     * Save one or more tracks to the user's library with timestamps (**Preferred method**).
     * This allows you to specify when tracks were added to maintain a specific chronological order in the user's library.
     *
     * @param timestampedIds List of timestamped IDs (id + addedAt), maximum 50
     */
    suspend fun saveTracksForUserWithTimestamps(
        timestampedIds: List<TimestampedId>
    ): Result<Unit, AppError>

    /**
     * Remove one or more tracks from the user's library.
     *
     * @param ids List of track IDs (maximum 50)
     */
    suspend fun removeTracksForUser(ids: List<String>): Result<Unit, AppError>

    /**
     * Check whether the specified tracks are saved in the user's library.
     *
     * @param ids List of track IDs (maximum 50)
     * @return List of flags for each ID: true — saved, false — not saved
     */
    suspend fun checkUserSavedTracks(ids: List<String>): Result<List<Boolean>, AppError>
}