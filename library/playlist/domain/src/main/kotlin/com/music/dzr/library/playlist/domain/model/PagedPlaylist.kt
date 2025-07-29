package com.music.dzr.library.playlist.domain.model

import com.music.dzr.core.model.shared.Image
import com.music.dzr.core.model.shared.PaginatedList
import com.music.dzr.core.model.user.User

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
    override val snapshotId: String,
    override val tracksCount: Int,
    val entries: PaginatedList<PlaylistEntry>
) : Playlist()
