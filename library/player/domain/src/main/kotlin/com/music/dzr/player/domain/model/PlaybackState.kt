package com.music.dzr.player.domain.model

import com.music.dzr.core.model.device.Device
import com.music.dzr.core.model.shared.AudioContent
import kotlin.time.Duration

/**
 * Represents the complete playback state of the user.
 *
 * Contains all information about the current player state: what's playing, on which device,
 * what playback settings are active, and what actions are available.
 *
 * @property device The device on which playback is occurring
 * @property context The playback context (album, playlist, etc.)
 * @property repeatMode The repeat mode setting
 * @property isShuffling Whether shuffle mode is enabled
 * @property isPlaying Whether playback is currently active
 * @property progress Progress through the current item. May be `null` if progress is unavailable
 * @property playingItem The currently playing audio content
 * @property actions Available playback control actions
 *
 * @see com.music.dzr.core.model.device.Device
 * @see PlaybackContext
 * @see RepeatMode
 * @see com.music.dzr.core.model.shared.AudioContent
 * @see PlaybackActions
 */
data class PlaybackState(
    val device: Device,
    val context: PlaybackContext,
    val repeatMode: RepeatMode,
    val isShuffling: Boolean,
    val isPlaying: Boolean,
    val progress: Duration?,
    val playingItem: AudioContent,
    val actions: PlaybackActions
)