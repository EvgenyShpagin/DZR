package com.music.dzr.library.playlist.domain.model

import com.music.dzr.core.model.Image
import com.music.dzr.core.model.User

/**
 * A complete representation of a playlist with all its tracks loaded in a simple list.
 * Useful for small playlists or when the API returns all items at once.
 */
data class FullPlaylist(
    override val id: String,
    override val name: String,
    override val description: String?,
    override val isPublic: Boolean?,
    override val isCollaborative: Boolean,
    override val owner: User,
    override val images: List<Image>,
    override val followersCount: Int?,
    override val externalUrl: String,
    override val version: PlaylistVersion,
    override val tracksCount: Int,
    val entries: List<PlaylistEntry>
) : Playlist()
