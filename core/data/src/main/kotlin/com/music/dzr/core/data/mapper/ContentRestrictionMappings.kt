package com.music.dzr.core.data.mapper

import com.music.dzr.core.model.ContentRestriction as DomainRestriction
import com.music.dzr.core.network.dto.Restrictions as NetworkRestriction

fun NetworkRestriction.toDomain(): DomainRestriction {
    return when (reason) {
        "market" -> DomainRestriction.MARKET
        "product" -> DomainRestriction.PRODUCT
        "explicit" -> DomainRestriction.EXPLICIT
        else -> throw IllegalArgumentException("Unsupported restriction reason received: '$reason'")
    }
}