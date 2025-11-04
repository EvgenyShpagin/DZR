package com.music.dzr.library.artist.data.remote.source

import com.music.dzr.core.data.test.HasForcedError
import com.music.dzr.core.data.test.TestJson
import com.music.dzr.core.data.test.runUnlessForcedError
import com.music.dzr.core.data.test.toPaginatedList
import com.music.dzr.core.network.dto.AlbumGroup
import com.music.dzr.core.network.dto.Artist
import com.music.dzr.core.network.dto.NetworkResponse
import com.music.dzr.core.network.dto.PaginatedList
import com.music.dzr.core.network.dto.Track
import com.music.dzr.core.network.dto.Tracks
import com.music.dzr.core.network.dto.error.NetworkError
import com.music.dzr.core.network.test.getJsonBodyAsset
import com.music.dzr.library.artist.data.remote.dto.ArtistAlbum
import com.music.dzr.library.artist.data.remote.dto.Artists

/**
 * Configurable in-memory test implementation of [ArtistRemoteDataSource] with default data.
 *
 * State is set via constructor or direct property assignment. Set [forcedError] to return failures.
 *
 * Not thread-safe.
 */
internal class TestArtistRemoteDataSource(
    var artists: List<Artist> = defaultArtists,
    var artistAlbums: List<ArtistAlbum> = defaultArtistAlbums,
    var artistTopTracks: List<Track> = defaultArtistTopTracks
) : ArtistRemoteDataSource, HasForcedError<NetworkError> {

    override var forcedError: NetworkError? = null
    override var isStickyForcedError: Boolean = false

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

private val defaultArtists = TestJson.decodeFromString<Artists>(
    getJsonBodyAsset("responses/multiple-artists.json")
).list

private val defaultArtistAlbums = TestJson.decodeFromString<PaginatedList<ArtistAlbum>>(
    getJsonBodyAsset("responses/artist-albums.json")
).items

private val defaultArtistTopTracks = TestJson.decodeFromString<Tracks>(
    getJsonBodyAsset("responses/artist-top-tracks.json")
).list
