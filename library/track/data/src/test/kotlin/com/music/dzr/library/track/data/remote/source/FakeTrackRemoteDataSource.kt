package com.music.dzr.library.track.data.remote.source

import com.music.dzr.core.data.test.HasForcedNetworkError
import com.music.dzr.core.data.test.respond
import com.music.dzr.core.network.dto.NetworkResponse
import com.music.dzr.core.network.dto.PaginatedList
import com.music.dzr.core.network.dto.Track
import com.music.dzr.core.network.dto.error.NetworkError
import com.music.dzr.library.track.data.remote.dto.SavedTrack
import com.music.dzr.library.track.data.remote.dto.TimestampedId
import kotlin.time.Clock
import kotlin.time.Instant

internal class FakeTrackRemoteDataSource : TrackRemoteDataSource, HasForcedNetworkError {

    override var forcedError: NetworkError? = null
    private val tracks = mutableMapOf<String, Track>()
    private val saved = linkedMapOf<String, Instant>()
    var saveTimestamp: Instant = Clock.System.now()

    override suspend fun getTrack(id: String, market: String?): NetworkResponse<Track> = respond {
        tracks[id] ?: error("Fake: track '$id' is not seeded. Seed it before calling getTrack()")
    }

    override suspend fun getMultipleTracks(
        ids: List<String>,
        market: String?
    ): NetworkResponse<List<Track>> = respond {
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
    ): NetworkResponse<PaginatedList<SavedTrack>> = respond {
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

    override suspend fun saveTracksForUser(ids: List<String>): NetworkResponse<Unit> = respond {
        ids.forEach { id ->
            check(tracks.containsKey(id)) { "Fake: cannot save unknown track '$id'. Seed it first." }
            saved[id] = saveTimestamp
        }
    }

    override suspend fun saveTracksForUserWithTimestamps(
        timestampedIds: List<TimestampedId>
    ): NetworkResponse<Unit> = respond {
        timestampedIds.forEach { (id, ts) ->
            check(tracks.containsKey(id)) { "Fake: cannot save unknown track '$id'. Seed it first." }
            saved[id] = ts
        }
    }

    override suspend fun removeTracksForUser(ids: List<String>): NetworkResponse<Unit> = respond {
        ids.forEach { saved.remove(it) }
    }

    override suspend fun checkUserSavedTracks(
        ids: List<String>
    ): NetworkResponse<List<Boolean>> = respond {
        ids.map { saved.containsKey(it) }
    }

    fun seedTracks(vararg items: Track) {
        items.forEach { tracks[it.id] = it }
    }

    fun clearAll() {
        tracks.clear()
        saved.clear()
        forcedError = null
        saveTimestamp = Clock.System.now()
    }
}
