package com.music.dzr.library.playlist.data.remote.source

import com.music.dzr.core.data.test.HasForcedNetworkError
import com.music.dzr.core.data.test.respond
import com.music.dzr.core.data.test.toPaginatedList
import com.music.dzr.core.network.dto.ExternalUrls
import com.music.dzr.core.network.dto.Followers
import com.music.dzr.core.network.dto.Image
import com.music.dzr.core.network.dto.NetworkResponse
import com.music.dzr.core.network.dto.PaginatedList
import com.music.dzr.core.network.dto.Playlist
import com.music.dzr.core.network.dto.PlaylistTrack
import com.music.dzr.core.network.dto.PlaylistTracksInfo
import com.music.dzr.core.network.dto.PlaylistWithPaginatedTracks
import com.music.dzr.core.network.dto.PlaylistWithTracks
import com.music.dzr.core.network.dto.PlaylistWithTracksInfo
import com.music.dzr.core.network.dto.PublicUser
import com.music.dzr.core.network.dto.SnapshotId
import com.music.dzr.core.network.dto.error.NetworkError
import com.music.dzr.library.playlist.data.remote.dto.NewPlaylistDetails
import com.music.dzr.library.playlist.data.remote.dto.PlaylistDetailsUpdate
import com.music.dzr.library.playlist.data.remote.dto.PlaylistFields
import com.music.dzr.library.playlist.data.remote.dto.PlaylistItemsUpdate
import com.music.dzr.library.playlist.data.remote.dto.TrackAdditions
import com.music.dzr.library.playlist.data.remote.dto.TrackRemovals

/**
 * In-memory Fake implementation of [PlaylistRemoteDataSource].
 *
 * Mirrors the contract of the real remote source but keeps all state in memory so tests can
 * deterministically set up scenarios and observe effects without network.
 */
internal class FakePlaylistRemoteDataSource : PlaylistRemoteDataSource, HasForcedNetworkError {

    override var forcedError: NetworkError? = null

    private val playlists = mutableMapOf<String, Playlist<List<PlaylistTrack>>>()
    private val tracks = mutableMapOf<String, PlaylistTrack>()
    private var nextPlaylistId = 0

    val owner: PublicUser = PublicUser(
        id = "test-user",
        displayName = "Fake User",
        uri = "spotify:user:test-user",
        type = "user",
        images = emptyList(),
        href = "https://api.spotify.com/v1/test-user",
        followers = Followers("", 0),
        externalUrls = ExternalUrls("https://open.spotify.com/test-user")
    )

    var snapshot: SnapshotId = SnapshotId("PresetSnapshot")

    fun seedPlaylists(vararg items: Playlist<List<PlaylistTrack>>) {
        items.forEach { playlist ->
            require(playlist.tracks.all { tracks.containsValue(it) }) {
                "Playlists should use seeded tracks"
            }
            playlists[playlist.id] = playlist
        }
    }

    fun seedTracks(vararg items: PlaylistTrack) {
        items.forEach { tracks[it.track.uri] = it }
    }

    override suspend fun getPlaylist(
        playlistId: String,
        market: String?,
        fields: PlaylistFields?
    ): NetworkResponse<PlaylistWithPaginatedTracks> = respond {
        val playlist = playlists[playlistId] ?: errorNoPlaylist(playlistId)

        playlist.withTracks { list ->
            list.toPaginatedList()
        }
    }

    private fun <T> Playlist<List<PlaylistTrack>>.withTracks(
        transform: (List<PlaylistTrack>) -> T
    ): Playlist<T> {
        return Playlist(
            collaborative = collaborative,
            description = description,
            externalUrls = externalUrls,
            followers = followers,
            href = href,
            id = id,
            images = images,
            name = name,
            owner = owner,
            public = public,
            snapshotId = snapshotId,
            tracks = transform(tracks),
            type = type,
            uri = uri
        )
    }

    override suspend fun changePlaylistDetails(
        playlistId: String,
        update: PlaylistDetailsUpdate
    ): NetworkResponse<Unit> = respond {
        val playlist = playlists[playlistId] ?: errorNoPlaylist(playlistId)
        val updated = playlist.copy(
            name = update.name ?: playlist.name,
            public = update.public ?: playlist.public,
            collaborative = update.collaborative ?: playlist.collaborative,
            description = update.description ?: playlist.description
        )
        playlists[playlistId] = updated
    }

    override suspend fun getPlaylistTracks(
        playlistId: String,
        market: String?,
        fields: PlaylistFields?,
        limit: Int?,
        offset: Int?
    ): NetworkResponse<PaginatedList<PlaylistTrack>> = respond {
        val playlist = playlists[playlistId] ?: errorNoPlaylist(playlistId)
        playlist.tracks.toPaginatedList(limit, offset)
    }

    override suspend fun updatePlaylistTracks(
        playlistId: String,
        update: PlaylistItemsUpdate
    ): NetworkResponse<SnapshotId> = respond {
        val playlist = playlists[playlistId] ?: errorNoPlaylist(playlistId)

        val updatedTracks = when {
            update.uris != null -> {
                // Replace all tracks with the given URIs
                update.uris.map { tracks[it] ?: errorNoTrack(it) }
            }

            update.rangeStart != null && update.insertBefore != null -> {
                // Reorder a range of tracks
                val currentTracks = playlist.tracks.toMutableList()
                val rangeLength = update.rangeLength ?: 1
                val rangeStart = update.rangeStart
                val insertBefore = update.insertBefore

                require(rangeStart >= 0 && rangeStart + rangeLength <= currentTracks.size) {
                    "Fake: invalid range for reordering."
                }
                require(insertBefore >= 0 && insertBefore <= currentTracks.size) {
                    "Fake: invalid insertBefore index for reordering."
                }

                val itemsToMove = List(rangeLength) { currentTracks.removeAt(rangeStart) }
                val actualInsertBefore = if (insertBefore > rangeStart) {
                    insertBefore - rangeLength
                } else {
                    insertBefore
                }
                currentTracks.addAll(actualInsertBefore, itemsToMove)
                currentTracks
            }

            else -> playlist.tracks // No operation specified
        }

        playlists[playlistId] = playlist.copy(tracks = updatedTracks)
        snapshot
    }

    override suspend fun addTracksToPlaylist(
        playlistId: String,
        additions: TrackAdditions
    ): NetworkResponse<SnapshotId> = respond {
        val playlist = playlists[playlistId] ?: errorNoPlaylist(playlistId)
        val tracksToAdd = additions.uris.map {
            tracks[it] ?: errorNoTrack(it)
        }
        val updatedTracks = (playlist.tracks.toMutableList().apply {
            val position = additions.position ?: size
            require(position >= 0 && position <= size) { "Fake: invalid position for adding tracks." }
            addAll(position, tracksToAdd)
        }).distinctBy { it.track.uri }
        playlists[playlistId] = playlist.copy(tracks = updatedTracks)
        snapshot
    }

    override suspend fun removePlaylistTracks(
        playlistId: String,
        removals: TrackRemovals
    ): NetworkResponse<SnapshotId> = respond {
        val playlist = playlists[playlistId] ?: errorNoPlaylist(playlistId)
        val urisToRemove = removals.tracks.map { it.uri }.toSet()
        val updatedTracks = playlist.tracks.filter { it.track.uri !in urisToRemove }
        playlists[playlistId] = playlist.copy(tracks = updatedTracks)
        snapshot
    }

    override suspend fun getCurrentUserPlaylists(
        limit: Int?,
        offset: Int?
    ): NetworkResponse<PaginatedList<PlaylistWithTracksInfo>> = respond {
        val items = playlists.values.map { playlist ->
            playlist.withTracks { tracks ->
                PlaylistTracksInfo(href = "", total = tracks.count())
            }
        }
        items.toPaginatedList(limit, offset)
    }

    override suspend fun getUserPlaylists(
        userId: String,
        limit: Int?,
        offset: Int?
    ): NetworkResponse<PaginatedList<PlaylistWithTracks>> = respond {
        val items = playlists.values
            .filter { it.owner.id == userId }
        items.toPaginatedList(limit, offset)
    }

    override suspend fun createPlaylist(
        userId: String,
        details: NewPlaylistDetails
    ): NetworkResponse<PlaylistWithPaginatedTracks> = respond {
        val playlistId = "fakePlaylist_${nextPlaylistId++}"
        val newPlaylist = Playlist<List<PlaylistTrack>>(
            id = playlistId,
            name = details.name,
            description = details.description ?: "",
            public = details.public ?: false,
            collaborative = details.collaborative ?: false,
            owner = owner,
            tracks = emptyList(),
            externalUrls = ExternalUrls(""),
            followers = null,
            href = "",
            images = emptyList(),
            snapshotId = snapshot.snapshotId,
            type = "playlist",
            uri = "spotify:playlist:$playlistId"
        )
        playlists[newPlaylist.id] = newPlaylist
        newPlaylist.withTracks { it.toPaginatedList() }
    }

    override suspend fun getPlaylistCoverImage(
        playlistId: String
    ): NetworkResponse<List<Image>> = respond {
        val playlist = playlists[playlistId] ?: errorNoPlaylist(playlistId)
        playlist.images
    }

    override suspend fun uploadCustomPlaylistCover(
        playlistId: String,
        jpegImageData: ByteArray
    ): NetworkResponse<Unit> = respond {
        val playlist = playlists[playlistId]
            ?: errorNoPlaylist(playlistId)
        val updated = playlist.copy(
            images = listOf(
                Image(
                    url = "fake_uploaded_image_url",
                    width = 640,
                    height = 640
                )
            )
        )
        playlists[playlistId] = updated
    }

    private fun errorNoPlaylist(id: String): Nothing {
        error("Fake: playlist '$id' is not seeded.")
    }

    private fun errorNoTrack(uri: String): Nothing {
        error("Fake: track with uri '$uri' is not seeded")
    }

    fun clearAll() {
        tracks.clear()
        playlists.clear()
        forcedError = null
        nextPlaylistId = 0
    }
}
