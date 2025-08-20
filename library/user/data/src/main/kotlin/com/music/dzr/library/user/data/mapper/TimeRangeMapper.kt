package com.music.dzr.library.user.data.mapper

import com.music.dzr.library.user.data.remote.dto.TimeRange as NetworkTimeRange
import com.music.dzr.library.user.domain.model.TimeRange as DomainTimeRange


internal fun DomainTimeRange.toNetwork(): NetworkTimeRange {
    return when (months) {
        1 -> NetworkTimeRange.ShortTerm
        in 2..6 -> NetworkTimeRange.MediumTerm
        else -> NetworkTimeRange.LongTerm
    }
}

internal fun NetworkTimeRange.toDomain(): DomainTimeRange {
    return when (this) {
        NetworkTimeRange.ShortTerm -> DomainTimeRange(months = 1)
        NetworkTimeRange.MediumTerm -> DomainTimeRange(months = 6)
        NetworkTimeRange.LongTerm -> DomainTimeRange(months = 12)
    }
}