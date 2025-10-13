package com.music.dzr.library.album.data.remote.source

import com.music.dzr.core.data.test.HasForcedError
import com.music.dzr.core.data.test.runUnlessForcedError
import com.music.dzr.core.data.test.toPaginatedList
import com.music.dzr.core.network.dto.NetworkResponse
import com.music.dzr.core.network.dto.PaginatedList
import com.music.dzr.core.network.dto.error.NetworkError
import com.music.dzr.core.network.test.getJsonBodyAsset
import com.music.dzr.library.album.data.remote.dto.Album
import com.music.dzr.library.album.data.remote.dto.AlbumTrack
import com.music.dzr.library.album.data.remote.dto.Albums
import com.music.dzr.library.album.data.remote.dto.SavedAlbum
import kotlinx.serialization.json.Json
import kotlin.time.Instant

/**
 * Asset-backed Fake implementation of [AlbumRemoteDataSource] for repository tests.
 *
 * Loads default data from JSON assets and exposes minimal mutable state for saved albums.
 */
internal class FakeAlbumRemoteDataSource : AlbumRemoteDataSource, HasForcedError<NetworkError> {

    override var forcedError: NetworkError? = null

    // Default data loaded from assets
    private val defaultAlbum: Album = json.decodeFromString(
        getJsonBodyAsset("responses/album.json")
    )
    private val defaultAlbums: List<Album> = json.decodeFromString<Albums>(
        getJsonBodyAsset("responses/multiple-albums.json")
    ).list
    private val defaultAlbumTracks = json.decodeFromString<PaginatedList<AlbumTrack>>(
        getJsonBodyAsset("responses/album-tracks.json")
    ).items
    private val defaultUserSaved: PaginatedList<SavedAlbum> = json.decodeFromString(
        getJsonBodyAsset("responses/user-saved-albums.json")
    )

    // Minimal in-memory state (only for saved albums; others are static from assets)
    val albums: List<Album> = listOf(defaultAlbum) + defaultAlbums
    val albumTracks: List<AlbumTrack> = defaultAlbumTracks
    val userSavedAlbums: MutableList<SavedAlbum> = defaultUserSaved.items.toMutableList()

    override suspend fun getAlbum(id: String, market: String?) = runUnlessForcedError {
        albums.find { it.id == id } ?: defaultAlbum
    }

    override suspend fun getMultipleAlbums(
        ids: List<String>,
        market: String?
    ): NetworkResponse<Albums> = runUnlessForcedError {
        Albums(albums.filter { it.id in ids })
    }

    override suspend fun getAlbumTracks(
        id: String,
        market: String?,
        limit: Int?,
        offset: Int?
    ): NetworkResponse<PaginatedList<AlbumTrack>> = runUnlessForcedError {
        albumTracks.toPaginatedList(limit, offset)
    }

    override suspend fun getUserSavedAlbums(
        limit: Int?,
        offset: Int?,
        market: String?
    ): NetworkResponse<PaginatedList<SavedAlbum>> = runUnlessForcedError {
        userSavedAlbums.toList().toPaginatedList(limit, offset)
    }

    override suspend fun saveAlbumsForUser(ids: List<String>) = runUnlessForcedError {
        ids.forEach { id ->
            val album = albums.find { it.id == id }
            if (album != null) {
                userSavedAlbums.add(
                    SavedAlbum(addedAt = Instant.fromEpochMilliseconds(0), album = album)
                )
            }
        }
    }

    override suspend fun removeAlbumsForUser(ids: List<String>): NetworkResponse<Unit> =
        runUnlessForcedError { userSavedAlbums.removeAll { it.album.id in ids } }
}

private val json = Json { ignoreUnknownKeys = true }
