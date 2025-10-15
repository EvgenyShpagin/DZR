package com.music.dzr.library.track.data.remote.source

import com.music.dzr.core.data.test.HasForcedError
import com.music.dzr.core.data.test.runUnlessForcedError
import com.music.dzr.core.network.dto.NetworkResponse
import com.music.dzr.core.network.dto.PaginatedList
import com.music.dzr.core.network.dto.Track
import com.music.dzr.core.network.dto.error.NetworkError
import com.music.dzr.library.track.data.remote.dto.SavedTrack
import com.music.dzr.library.track.data.remote.dto.TimestampedId
import kotlin.time.Clock
import kotlin.time.Instant

/**
 * Configurable in-memory test implementation of [TrackRemoteDataSource] with default data.
 *
 * State is set via constructor or direct property assignment; data that must satisfy relationships
 * or ordering is prepared with a seed method that preserves invariants.
 * Set [forcedError] to return failures.
 *
 * Not thread-safe.
 */
internal class TestTrackRemoteDataSource(
    var saveTimestamp: Instant = Clock.System.now()
) : TrackRemoteDataSource, HasForcedError<NetworkError> {

    override var forcedError: NetworkError? = null
    private val tracks = mutableMapOf<String, Track>()
    private val saved = linkedMapOf<String, Instant>()

    fun seedTracks(vararg items: Track) {
        items.forEach { tracks[it.id] = it }
    }

    override suspend fun getTrack(id: String, market: String?) = runUnlessForcedError {
        tracks[id] ?: error("Fake: track '$id' is not seeded. Seed it before calling getTrack()")
    }

    override suspend fun getMultipleTracks(
        ids: List<String>,
        market: String?
    ): NetworkResponse<List<Track>> = runUnlessForcedError {
        val list = ids.map { id ->
            tracks[id] ?: error(
                "Fake: track '$id' is not seeded. Seed it before calling getMultipleTracks()"
            )
        }
        list
    }

    override suspend fun getUserSavedTracks(
        limit: Int?,
        offset: Int?,
        market: String?
    ): NetworkResponse<PaginatedList<SavedTrack>> = runUnlessForcedError {
        val items = saved
            .entries
            .sortedByDescending { it.value }
            .map { (id, ts) ->
                val track = tracks[id]
                    ?: error("Fake: saved track '$id' is not seeded. Seed tracks consistently.")
                SavedTrack(addedAt = ts, track = track)
            }
        paginateSaved(items, limit, offset)
    }

    private fun paginateSaved(
        items: List<SavedTrack>,
        limit: Int?,
        offset: Int?
    ): PaginatedList<SavedTrack> {
        val offset = offset ?: 0
        val limit = limit ?: items.size
        return PaginatedList(
            items = items.drop(offset).take(limit),
            limit = limit,
            offset = offset,
            total = items.size,
            href = "",
            next = null,
            previous = null
        )
    }

    override suspend fun saveTracksForUser(ids: List<String>) = runUnlessForcedError {
        ids.forEach { id ->
            check(tracks.containsKey(id)) { "Fake: cannot save unknown track '$id'. Seed it first." }
            saved[id] = saveTimestamp
        }
    }

    override suspend fun saveTracksForUserWithTimestamps(
        timestampedIds: List<TimestampedId>
    ): NetworkResponse<Unit> = runUnlessForcedError {
        timestampedIds.forEach { (id, ts) ->
            check(tracks.containsKey(id)) { "Fake: cannot save unknown track '$id'. Seed it first." }
            saved[id] = ts
        }
    }

    override suspend fun removeTracksForUser(ids: List<String>) = runUnlessForcedError {
        ids.forEach { saved.remove(it) }
    }

    override suspend fun checkUserSavedTracks(
        ids: List<String>
    ): NetworkResponse<List<Boolean>> = runUnlessForcedError {
        ids.map { saved.containsKey(it) }
    }
}
