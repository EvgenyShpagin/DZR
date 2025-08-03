package com.music.dzr.library.player.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents the set of actions that can be performed on the current playback.
 *
 * @property interruptingPlayback Interrupting playback.
 * @property pausing Pausing.
 * @property resuming Resuming.
 * @property seeking Seeking playback location.
 * @property skippingNext Skipping to the next context.
 * @property skippingPrev Skipping to the previous context.
 * @property togglingRepeatContext Toggling repeat context flag.
 * @property togglingShuffle Toggling shuffle flag.
 * @property togglingRepeatTrack Toggling repeat track flag.
 * @property transferringPlayback Transferring playback between devices.
 */
@Serializable
data class Actions(
    @SerialName("interrupting_playback")
    val interruptingPlayback: Boolean? = null,
    val pausing: Boolean? = null,
    val resuming: Boolean? = null,
    val seeking: Boolean? = null,
    @SerialName("skipping_next")
    val skippingNext: Boolean? = null,
    @SerialName("skipping_prev")
    val skippingPrev: Boolean? = null,
    @SerialName("toggling_repeat_context")
    val togglingRepeatContext: Boolean? = null,
    @SerialName("toggling_shuffle")
    val togglingShuffle: Boolean? = null,
    @SerialName("toggling_repeat_track")
    val togglingRepeatTrack: Boolean? = null,
    @SerialName("transferring_playback")
    val transferringPlayback: Boolean? = null
)