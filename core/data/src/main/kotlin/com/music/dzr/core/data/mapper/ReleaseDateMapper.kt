package com.music.dzr.core.data.mapper

import com.music.dzr.core.model.ReleaseDate as DomainReleaseDate
import com.music.dzr.core.network.dto.ReleaseDate as NetworkReleaseDate

fun NetworkReleaseDate.toDomain(): DomainReleaseDate {
    return DomainReleaseDate(
        year = year,
        month = month,
        day = day
    )
}