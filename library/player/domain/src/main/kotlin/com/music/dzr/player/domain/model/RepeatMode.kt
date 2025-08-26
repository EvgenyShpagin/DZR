package com.music.dzr.player.domain.model

/**
 * Playback repeat modes.
 *
 * Determines the player's behavior when reaching the end of the current item or context.
 *
 * @see PlaybackState
 */
enum class RepeatMode {
    /**
     * Repeat current track mode.
     * The current track will play in a continuous loop until manually changed.
     */
    Track,

    /**
     * Repeat context mode.
     * After the last track in the current context, playback will restart from the first track.
     */
    Context,

    /**
     * Repeat disabled.
     * Playback will stop after the last track in the context is finished.
     */
    Off;

    companion object
}