package com.music.dzr.library.playlist.domain.model

import com.music.dzr.core.model.Image
import com.music.dzr.core.model.User

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
    abstract val version: PlaylistVersion
    abstract val tracksCount: Int
}
