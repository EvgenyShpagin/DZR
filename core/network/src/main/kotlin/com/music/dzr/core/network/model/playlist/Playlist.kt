package com.music.dzr.core.network.model.playlist

import com.music.dzr.core.network.model.ExternalUrls
import com.music.dzr.core.network.model.Followers
import com.music.dzr.core.network.model.Image
import com.music.dzr.core.network.model.PaginatedList
import com.music.dzr.core.network.model.PublicUser
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents a playlist on Spotify.
 *
 * @param T The tracks representation.
 *
 * @property collaborative Returns `true` if context is not search and the owner allows other users to modify the playlist. Otherwise returns `false`.
 * @property description The playlist description. *Only returned for modified, public playlists, otherwise `null`.*
 * @property externalUrls Known external URLs for this playlist.
 * @property followers Information about the followers of the playlist. `null` if it's user playlist
 * @property href A link to the Web API endpoint providing full details of the playlist.
 * @property id The Spotify ID for the playlist.
 * @property images Images for the playlist. The array may be empty or contain up to three images. The images are returned by size in descending order. *Note: If the playlist does not have a custom image, one will be generated automatically based on the playlist's tracks.*
 * @property name The name of the playlist.
 * @property owner The user who owns the playlist
 * @property public The playlist's public/private status: `true` the playlist is public, `false` the playlist is private, `null` the playlist status is not relevant. For more about public/private status.
 * @property snapshotId The version identifier for the current playlist. Can be supplied in other requests to target a specific playlist version.
 * @property tracks The tracks of the playlist.
 * @property type The object type: "playlist".
 * @property uri The Spotify URI for the playlist.
 */
@Serializable
data class Playlist<T>(
    val collaborative: Boolean,
    val description: String?,
    @SerialName("external_urls")
    val externalUrls: ExternalUrls,
    val followers: Followers? = null,
    val href: String,
    val id: String,
    val images: List<Image>,
    val name: String,
    val owner: PublicUser,
    val public: Boolean?,
    @SerialName("snapshot_id")
    val snapshotId: String,
    val tracks: T,
    val type: String,
    val uri: String,
)

typealias PlaylistWithTracksInfo = Playlist<PlaylistTracksInfo>
typealias PlaylistWithTracks = Playlist<List<PlaylistTrack>>
typealias PlaylistWithPaginatedTracks = Playlist<PaginatedList<PlaylistTrack>>