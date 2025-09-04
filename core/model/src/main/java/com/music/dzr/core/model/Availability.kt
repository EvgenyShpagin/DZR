package com.music.dzr.core.model

/**
 * Marker interface for Album-only availability.
 */
sealed interface AlbumAvailability : Availability

/**
 * Marker interface for Track-only availability.
 */
sealed interface TrackAvailability : Availability

/**
 * Closed hierarchy of all availability states.
 */
sealed interface Availability {

    /**
     * The content is fully available for playback and display.
     */
    data object Available : Availability, AlbumAvailability, TrackAvailability

    /**
     * The content is not available for playback due to a specific [reason].
     */
    data class Restricted(
        val reason: ContentRestriction
    ) : Availability, AlbumAvailability, TrackAvailability

    /**
     * The availability is unresolved for the current context.
     *
     * Typically used when a market is not specified (or user context is unknown).
     */
    data object Unknown : Availability, AlbumAvailability, TrackAvailability

    /**
     * The requested track has been relinked for the target market to another track with [linkedFromId].
     */
    data class Relinked(
        val linkedFromId: String
    ) : Availability, TrackAvailability

    /**
     * The track is a local file stored on the user's device. Not streamable from the network.
     */
    data object Local : Availability, TrackAvailability
}
