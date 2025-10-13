package com.music.dzr.library.artist.data.remote.source

import com.music.dzr.core.data.test.HasForcedError
import com.music.dzr.core.data.test.runUnlessForcedError
import com.music.dzr.core.data.test.toPaginatedList
import com.music.dzr.core.network.dto.AlbumGroup
import com.music.dzr.core.network.dto.Artist
import com.music.dzr.core.network.dto.NetworkResponse
import com.music.dzr.core.network.dto.PaginatedList
import com.music.dzr.core.network.dto.Tracks
import com.music.dzr.core.network.dto.error.NetworkError
import com.music.dzr.core.network.test.getJsonBodyAsset
import com.music.dzr.library.artist.data.remote.dto.ArtistAlbum
import com.music.dzr.library.artist.data.remote.dto.Artists
import kotlinx.serialization.json.Json

/**
 * In-memory Fake implementation of [ArtistRemoteDataSource].
 *
 * Mirrors the contract of the real remote source but keeps all state in memory so tests can
 * deterministically set up scenarios and observe effects without network.
 */
internal class FakeArtistRemoteDataSource : ArtistRemoteDataSource, HasForcedError<NetworkError> {

    override var forcedError: NetworkError? = null

    // In-memory state
    val artists = defaultArtists.toMutableList()
    val artistAlbums = defaultArtistAlbums.toMutableList()
    val artistTopTracks = defaultArtistTopTracks.toMutableList()

    override suspend fun getArtist(
        id: String
    ): NetworkResponse<Artist> = runUnlessForcedError {
        artists.find { it.id == id }!!
    }

    override suspend fun getMultipleArtists(
        ids: List<String>
    ): NetworkResponse<Artists> = runUnlessForcedError {
        Artists(list = artists.filter { it.id in ids })
    }

    override suspend fun getArtistAlbums(
        id: String,
        includeGroups: List<AlbumGroup>?,
        market: String?,
        limit: Int?,
        offset: Int?
    ): NetworkResponse<PaginatedList<ArtistAlbum>> = runUnlessForcedError {
        artistAlbums
            .filter { market == null || it.availableMarkets == null || market in it.availableMarkets }
            .filter { includeGroups == null || it.albumGroup in includeGroups }
            .toPaginatedList(limit = limit, offset = offset)
    }

    override suspend fun getArtistTopTracks(
        id: String,
        market: String?
    ): NetworkResponse<Tracks> = runUnlessForcedError {
        Tracks(
            list = artistTopTracks.takeIf { market == null }
                ?: artistTopTracks.filter { market in it.availableMarkets }
        )
    }
}

private val json = Json { ignoreUnknownKeys = true }

private val defaultArtists = json.decodeFromString<Artists>(
    getJsonBodyAsset("responses/multiple-artists.json")
).list

private val defaultArtistAlbums = json.decodeFromString<PaginatedList<ArtistAlbum>>(
    getJsonBodyAsset("responses/artist-albums.json")
).items

private val defaultArtistTopTracks = json.decodeFromString<Tracks>(
    getJsonBodyAsset("responses/artist-top-tracks.json")
).list
