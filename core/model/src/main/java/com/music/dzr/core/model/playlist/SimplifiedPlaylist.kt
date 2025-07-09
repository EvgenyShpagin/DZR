package com.music.dzr.core.model.playlist

import com.music.dzr.core.model.shared.Image
import com.music.dzr.core.model.user.User

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
