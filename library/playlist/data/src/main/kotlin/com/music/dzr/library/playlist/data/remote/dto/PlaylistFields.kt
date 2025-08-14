package com.music.dzr.library.playlist.data.remote.dto

/**
 * Builder for Spotify playlist "fields" filter expressions.
 * Produces serialized values for Retrofit @Query("fields") parameter.
 *
 * Examples:
 * - Single field: `field(NAME)` → "name"
 * - Exclude field: `exclude(NAME)` → "!name"
 * - Multiple fields: `include(listOf(NAME, DESCRIPTION))` → "name,description"
 * - Dot path: `path(listOf(OWNER, ID))` → "owner.id"
 * - Group: `group(TRACKS, listOf(items(...)))` → "tracks(items(...))"
 * - Items: `items(listOf(field(ADDED_AT)))` → "items(added_at)"
 *
 * Complex example:
 * ```
 * tracks.items(track(name,href,album(name,href)))
 * ```
 * ```
 * group(TRACKS, listOf(
 *     items(listOf(
 *         group(TRACK, listOf(
 *             include(listOf(NAME, HREF)),
 *             group(ALBUM, listOf(include(listOf(NAME, HREF)))
 *         ))
 *     ))
 * ))
 * ```
 */
@JvmInline
internal value class PlaylistFields private constructor(val value: String) {
    /**
     * One or multiple fields: `PlaylistFields(listOf(NAME, DESCRIPTION))` → `"name,description"`
     */
    constructor(fields: List<PlaylistField>) : this(fields.joinToString(","))

    override fun toString(): String = value

    companion object {
        /**
         * Multiple expressions:
         * ```
         * includeExpr(
         *     listOf(PlaylistFields(NAME),
         *     PlaylistFields(!DESCRIPTION))
         * )
         * ```
         * `"name,!description"`
         */
        fun includeExpr(exprs: List<PlaylistFields>) = PlaylistFields(exprs.joinToString(","))

        /**
         * Dot path: `path(listOf(OWNER, ID))` → `"owner.id"`
         */
        fun path(parts: List<PlaylistField>) = PlaylistFields(parts.joinToString("."))

        /**
         * Items group: `items(listOf(field(ADDED_AT)))` → `"items(added_at)"`
         */
        fun items(sub: List<PlaylistFields>) = group(PlaylistField.Items, sub)

        /**
         * Named group: `group(TRACKS, listOf(...))` → `"tracks(...)"`
         */
        fun group(name: PlaylistField, sub: List<PlaylistFields>) =
            PlaylistFields("${name.value}(" + sub.joinToString(",") + ")")
    }
}
