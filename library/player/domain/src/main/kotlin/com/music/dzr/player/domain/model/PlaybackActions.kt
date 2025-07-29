package com.music.dzr.player.domain.model

/**
 * Represents the available playback control actions for the current context.
 *
 * Each property indicates whether the corresponding action is allowed in the current
 * player state. For example, during ad playback, many actions may be disabled.
 *
 * @property pausing Whether pausing playback is allowed
 * @property resuming Whether resuming playback is allowed
 * @property seeking Whether seeking through the track is allowed
 * @property skippingNext Whether skipping to the next track is allowed
 * @property skippingPrev Whether skipping to the previous track is allowed
 * @property togglingRepeatContext Whether toggling repeat context mode is allowed
 * @property togglingShuffle Whether toggling shuffle mode is allowed
 * @property togglingRepeatTrack Whether toggling repeat track mode is allowed
 * @property transferringPlayback Whether transferring playback to another device is allowed
 *
 * @see PlaybackState
 */
data class PlaybackActions(
    val pausing: Boolean = false,
    val resuming: Boolean = false,
    val seeking: Boolean = false,
    val skippingNext: Boolean = false,
    val skippingPrev: Boolean = false,
    val togglingRepeatContext: Boolean = false,
    val togglingShuffle: Boolean = false,
    val togglingRepeatTrack: Boolean = false,
    val transferringPlayback: Boolean = false
)