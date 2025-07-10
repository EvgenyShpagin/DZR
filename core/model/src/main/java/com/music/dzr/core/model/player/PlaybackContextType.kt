package com.music.dzr.core.model.player

/**
 * Types of playback contexts.
 *
 * Defines the source from which music playback occurs.
 * Each context type has its own logic for determining the next track.
 *
 * @see PlaybackContext
 */
enum class PlaybackContextType {
    /**
     * Album context - tracks are played in the order they appear in the album.
     * Next track selection follows the album's track listing.
     */
    Album,

    /**
     * Playlist context - tracks are played according to playlist order.
     * May include tracks from various albums and artists.
     */
    Playlist,

    /**
     * Artist context - playing from an artist's profile or top tracks.
     * Usually includes the most popular tracks by the artist.
     */
    Artist,

    /**
     * Unknown or undefined context type.
     * Used when the playback source cannot be determined or categorized.
     */
    Unknown
}