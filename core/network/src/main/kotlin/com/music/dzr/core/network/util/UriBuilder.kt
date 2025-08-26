package com.music.dzr.core.network.util

/**
 * Spotify API URI Builder by id.
 * Examples: `spotify:track:<id>`, `spotify:album:<id>`
 */
object UriBuilder {

    private const val SCHEME = "spotify"

    fun build(type: String, id: String): String = "$SCHEME:$type:$id"
}
