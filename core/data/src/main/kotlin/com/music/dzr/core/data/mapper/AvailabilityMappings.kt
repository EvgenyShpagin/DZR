package com.music.dzr.core.data.mapper

import com.music.dzr.core.model.Availability
import com.music.dzr.core.network.dto.LinkedFrom
import com.music.dzr.core.network.dto.Restrictions

fun Availability.Companion.fromNetwork(
    isPlayable: Boolean? = null,
    restrictions: Restrictions? = null,
    isLocal: Boolean? = null,
    linkedFrom: LinkedFrom? = null
): Availability {
    return when {
        linkedFrom != null -> Availability.Relinked(linkedFrom.id)
        isPlayable == true -> Availability.Available
        restrictions != null -> Availability.Restricted(restrictions.toDomain())
        isLocal == true -> Availability.Local
        else -> Availability.Unknown
    }
}