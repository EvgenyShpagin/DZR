package com.music.dzr.core.data.mapper

import com.music.dzr.core.data.remote.dto.Markets
import com.music.dzr.core.model.Market as DomainMarket


fun Markets.toDomain(): List<DomainMarket> {
    return list.map { code -> DomainMarket(code) }
}

fun DomainMarket.toNetwork(): String {
    return code
}

fun List<DomainMarket>.toNetwork(): Markets {
    val codes = map { market -> market.code }
    return Markets(codes)
}