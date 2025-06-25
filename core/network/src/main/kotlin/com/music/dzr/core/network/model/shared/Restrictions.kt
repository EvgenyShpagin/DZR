package com.music.dzr.core.network.model.shared

import kotlinx.serialization.Serializable

/**
 * Represents content restriction information.
 */
@Serializable
data class Restrictions(
    val reason: String
)