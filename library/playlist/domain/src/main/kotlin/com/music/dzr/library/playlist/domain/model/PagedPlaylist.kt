package com.music.dzr.library.playlist.domain.model

import com.music.dzr.core.model.Image
import com.music.dzr.core.pagination.OffsetPage
import com.music.dzr.core.model.User

/**
 * A detailed representation of a playlist that includes a paginated list of its tracks.
 * Used for viewing the content of large playlists.
 */
data class PagedPlaylist(
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
    val entries: OffsetPage<PlaylistEntry>
) : Playlist()
