package com.music.dzr.core.model.playlist

import com.music.dzr.core.model.shared.Image
import com.music.dzr.core.model.user.User

/**
 * Common class for all playlist models.
 */
sealed class Playlist {
    abstract val id: String
    abstract val name: String
    abstract val description: String?
    abstract val isPublic: Boolean?
    abstract val isCollaborative: Boolean
    abstract val owner: User
    abstract val images: List<Image>
    abstract val followersCount: Int?
    abstract val externalUrl: String
    abstract val snapshotId: String
    abstract val tracksCount: Int
}
