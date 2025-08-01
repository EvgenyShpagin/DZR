package com.music.dzr.library.playlist.domain.model

import com.music.dzr.core.model.Image
import com.music.dzr.library.user.domain.model.User

/**
 * A lightweight, summary representation of a playlist without its track list.
 * Ideal for use in lists or search results.
 */
data class SimplifiedPlaylist(
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
    override val tracksCount: Int
) : Playlist()
