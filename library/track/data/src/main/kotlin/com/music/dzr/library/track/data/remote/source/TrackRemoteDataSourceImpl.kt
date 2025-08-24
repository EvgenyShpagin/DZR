package com.music.dzr.library.track.data.remote.source

import com.music.dzr.core.network.dto.NetworkResponse
import com.music.dzr.core.network.dto.PaginatedList
import com.music.dzr.core.network.dto.Track
import com.music.dzr.library.track.data.remote.api.TrackApi
import com.music.dzr.library.track.data.remote.dto.SaveTracksTimestampedRequest
import com.music.dzr.library.track.data.remote.dto.SavedTrack
import com.music.dzr.library.track.data.remote.dto.TimestampedId

internal class TrackRemoteDataSourceImpl(private val trackApi: TrackApi) : TrackRemoteDataSource {

    override suspend fun getTrack(id: String, market: String?): NetworkResponse<Track> {
        return trackApi.getTrack(id = id, market = market)
    }

    override suspend fun getMultipleTracks(
        ids: List<String>,
        market: String?
    ): NetworkResponse<List<Track>> {
        val idsCsv = ids.joinToString(",")
        val tracks = trackApi.getMultipleTracks(ids = idsCsv, market = market)
        return NetworkResponse(tracks.data?.list, tracks.error)
    }

    override suspend fun getUserSavedTracks(
        limit: Int?,
        offset: Int?,
        market: String?
    ): NetworkResponse<PaginatedList<SavedTrack>> {
        return trackApi.getUserSavedTracks(limit = limit, offset = offset, market = market)
    }

    override suspend fun saveTracksForUser(ids: List<String>): NetworkResponse<Unit> {
        val idsCsv = ids.joinToString(",")
        return trackApi.saveTracksForUser(ids = idsCsv)
    }

    override suspend fun saveTracksForUserWithTimestamps(
        timestampedIds: List<TimestampedId>
    ): NetworkResponse<Unit> {
        return trackApi.saveTracksForUser(
            request = SaveTracksTimestampedRequest(timestampedIds)
        )
    }

    override suspend fun removeTracksForUser(ids: List<String>): NetworkResponse<Unit> {
        return trackApi.removeTracksForUser(ids = ids)
    }

    override suspend fun checkUserSavedTracks(ids: List<String>): NetworkResponse<List<Boolean>> {
        val idsCsv = ids.joinToString(",")
        return trackApi.checkUserSavedTracks(ids = idsCsv)
    }
}
