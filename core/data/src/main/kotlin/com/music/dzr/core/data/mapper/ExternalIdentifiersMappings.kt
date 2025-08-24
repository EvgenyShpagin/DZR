package com.music.dzr.core.data.mapper

import com.music.dzr.core.model.ExternalIdentifiers as DomainExternalIds
import com.music.dzr.core.network.dto.ExternalIds as NetworkExternalIds


fun NetworkExternalIds.toDomain(): DomainExternalIds {
    return DomainExternalIds(isrc = isrc, ean = ean, upc = upc)
}