package com.music.dzr.library.playlist.data.remote.dto

/**
 * Atomic field token used by [PlaylistFields] builder.
 * Contains constants for common playlist and track fields used in Spotify Web API.
 *
 * Examples:
 * - Basic fields: `NAME`, `DESCRIPTION`, `URI`
 * - Track fields: `TRACK`, `ALBUM`, `ARTISTS`
 * - Collection fields: `ITEMS`, `TRACKS`
 * - Nested fields: `ADDED_AT`, `ADDED_BY`
 *
 * Usage with [PlaylistFields]:
 * ```
 * PlaylistFields.include(listOf(NAME, DESCRIPTION))     // "name,description"
 * PlaylistFields.path(listOf(OWNER, ID))               // "owner.id"
 * PlaylistFields.group(TRACKS, listOf(items(...)))     // "tracks(items(...))"
 * ```
 *
 * Exclusion operator:
 * ```
 * val excludedName = !NAME  // Creates PlaylistField("!name")
 * PlaylistFields.exclude(NAME)  // Same result
 * ```
 */
@JvmInline
internal value class PlaylistField private constructor(val value: String) {

    override fun toString(): String = value
    
    operator fun not() = PlaylistField("!$value")

    companion object {
        // Top-level common playlist fields
        val ID = PlaylistField("id")
        val NAME = PlaylistField("name")
        val URI = PlaylistField("uri")
        val HREF = PlaylistField("href")
        val TYPE = PlaylistField("type")
        val DESCRIPTION = PlaylistField("description")
        val PUBLIC = PlaylistField("public")
        val SNAPSHOT_ID = PlaylistField("snapshot_id")
        val IMAGES = PlaylistField("images")
        val OWNER = PlaylistField("owner")
        val TRACKS = PlaylistField("tracks")

        // Recurring/collection helpers
        val ITEMS = PlaylistField("items")
        val ADDED_AT = PlaylistField("added_at")
        val ADDED_BY = PlaylistField("added_by")
        val IS_LOCAL = PlaylistField("is_local")
        val TRACK = PlaylistField("track")
        val ALBUM = PlaylistField("album")

        // Frequently used nested fields
        val DISPLAY_NAME = PlaylistField("display_name")
        val EXTERNAL_URLS = PlaylistField("external_urls")
        val FOLLOWERS = PlaylistField("followers")
        val IMAGES_URL = PlaylistField("url")

        // Track fields
        val ARTISTS = PlaylistField("artists")
        val AVAILABLE_MARKETS = PlaylistField("available_markets")
        val DISC_NUMBER = PlaylistField("disc_number")
        val DURATION_MS = PlaylistField("duration_ms")
        val EXPLICIT = PlaylistField("explicit")
        val EXTERNAL_IDS = PlaylistField("external_ids")
        val IS_PLAYABLE = PlaylistField("is_playable")
        val LINKED_FROM = PlaylistField("linked_from")
        val RESTRICTIONS = PlaylistField("restrictions")
        val POPULARITY = PlaylistField("popularity")
        val TRACK_NUMBER = PlaylistField("track_number")
    }
}
