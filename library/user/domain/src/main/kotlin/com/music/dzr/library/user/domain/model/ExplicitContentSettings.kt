package com.music.dzr.library.user.domain.model

/**
 * The user's explicit content settings.
 *
 * @property filterEnabled When true, indicates that explicit content should not be played.
 * @property filterLocked When true, indicates that the explicit content setting is locked and can't be changed by the user.
 */
data class ExplicitContentSettings(
    val filterEnabled: Boolean,
    val filterLocked: Boolean
)
