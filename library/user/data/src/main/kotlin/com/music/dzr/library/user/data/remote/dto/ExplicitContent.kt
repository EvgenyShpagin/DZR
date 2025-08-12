package com.music.dzr.library.user.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * The user's explicit content settings.
 *
 * @property filterEnabled When true, indicates that explicit content should not be played.
 * @property filterLocked When true, indicates that the explicit content setting is locked and can't be changed by the user.
 */
@Serializable
internal data class ExplicitContent(
    @SerialName("filter_enabled")
    val filterEnabled: Boolean,
    @SerialName("filter_locked")
    val filterLocked: Boolean
)