package com.music.dzr.library.track.data.remote.source

import com.music.dzr.core.network.dto.NetworkResponse
import com.music.dzr.core.network.dto.PaginatedList
import com.music.dzr.core.network.dto.Track
import com.music.dzr.library.track.data.remote.api.TrackApi
import com.music.dzr.library.track.data.remote.dto.SavedTrack
import com.music.dzr.library.track.data.remote.dto.TimestampedId

/**
 * Remote data source for working with tracks.
 * Thin wrapper around [TrackApi] with convenient method signatures.
 */
internal interface TrackRemoteDataSource {

    /**
     * Fetch track information by its Spotify ID.
     *
     * @param id Spotify track ID
     * @param market ISO 3166-1 alpha-2 country code (optional)
     */
    suspend fun getTrack(id: String, market: String? = null): NetworkResponse<Track>

    /**
     * Fetch information for multiple tracks.
     *
     * @param ids List of Spotify track IDs (maximum 50)
     * @param market ISO 3166-1 alpha-2 country code (optional)
     */
    suspend fun getMultipleTracks(
        ids: List<String>,
        market: String? = null
    ): NetworkResponse<List<Track>>

    /**
     * Get the current user's saved tracks.
     *
     * @param limit Maximum number of items to return (1..50)
     * @param offset The index of the first item to return
     * @param market ISO 3166-1 alpha-2 country code (optional)
     */
    suspend fun getUserSavedTracks(
        limit: Int? = null,
        offset: Int? = null,
        market: String? = null
    ): NetworkResponse<PaginatedList<SavedTrack>>

    /**
     * Save one or more tracks to the user's library.
     *
     * @param ids List of Spotify IDs (maximum 50)
     */
    suspend fun saveTracksForUser(ids: List<String>): NetworkResponse<Unit>

    /**
     * Save one or more tracks to the user's library with timestamps (**Preferred method**).
     * This allows you to specify when tracks were added to maintain a specific chronological order in the user's library.
     *
     * @param timestampedIds List of timestamped IDs (id + addedAt), maximum 50
     */
    suspend fun saveTracksForUserWithTimestamps(
        timestampedIds: List<TimestampedId>
    ): NetworkResponse<Unit>

    /**
     * Remove one or more tracks from the user's library.
     *
     * @param ids List of Spotify IDs (maximum 50)
     */
    suspend fun removeTracksForUser(ids: List<String>): NetworkResponse<Unit>

    /**
     * Check whether the specified tracks are saved in the user's library.
     *
     * @param ids List of Spotify IDs (maximum 50)
     * @return List of flags for each ID: true — saved, false — not saved
     */
    suspend fun checkUserSavedTracks(ids: List<String>): NetworkResponse<List<Boolean>>
}