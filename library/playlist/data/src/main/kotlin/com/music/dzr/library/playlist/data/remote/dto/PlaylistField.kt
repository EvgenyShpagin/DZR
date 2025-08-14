package com.music.dzr.library.playlist.data.remote.dto

/**
 * Atomic field token used by [PlaylistFields] builder.
 * Contains constants for common playlist and track fields used in Spotify Web API.
 *
 * Examples:
 * - Basic fields: `Name`, `Description`, `Uri`
 * - Track fields: `Track`, `Album`, `Artists`
 * - Collection fields: `Items`, `Tracks`
 * - Nested fields: `AddedAt`, `AddedBy`
 *
 * Usage with [PlaylistFields]:
 * ```
 * PlaylistFields(listOf(Name, Description))            // "name,description"
 * PlaylistFields.path(listOf(Owner, ID))               // "owner.id"
 * PlaylistFields.group(Tracks, listOf(items(...)))     // "tracks(items(...))"
 * ```
 *
 * Exclusion operator:
 * ```
 * val excludedName = !Name  // Creates PlaylistField("!name")
 * ```
 */
@JvmInline
internal value class PlaylistField private constructor(val value: String) {

    override fun toString(): String = value
    
    operator fun not() = PlaylistField("!$value")

    companion object {
        // Top-level common playlist fields
        val Id = PlaylistField("id")
        val Name = PlaylistField("name")
        val Uri = PlaylistField("uri")
        val Href = PlaylistField("href")
        val Type = PlaylistField("type")
        val Description = PlaylistField("description")
        val Public = PlaylistField("public")
        val SnapshotId = PlaylistField("snapshot_id")
        val Images = PlaylistField("images")
        val Owner = PlaylistField("owner")
        val Tracks = PlaylistField("tracks")

        // Recurring/collection helpers
        val Items = PlaylistField("items")
        val AddedAt = PlaylistField("added_at")
        val AddedBy = PlaylistField("added_by")
        val IsLocal = PlaylistField("is_local")
        val Track = PlaylistField("track")
        val Album = PlaylistField("album")

        // Frequently used nested fields
        val DisplayName = PlaylistField("display_name")
        val ExternalUrls = PlaylistField("external_urls")
        val Followers = PlaylistField("followers")
        val ImagesUrl = PlaylistField("url")

        // Track fields
        val Artists = PlaylistField("artists")
        val AvailableMarkets = PlaylistField("available_markets")
        val DiscNumber = PlaylistField("disc_number")
        val DurationMs = PlaylistField("duration_ms")
        val Explicit = PlaylistField("explicit")
        val ExternalIds = PlaylistField("external_ids")
        val IsPlayable = PlaylistField("is_playable")
        val LinkedFrom = PlaylistField("linked_from")
        val Restrictions = PlaylistField("restrictions")
        val Popularity = PlaylistField("popularity")
        val TrackNumber = PlaylistField("track_number")
    }
}
