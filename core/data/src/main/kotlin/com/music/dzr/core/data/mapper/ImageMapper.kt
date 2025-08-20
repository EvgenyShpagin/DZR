package com.music.dzr.core.data.mapper

import com.music.dzr.core.model.Image as DomainImage
import com.music.dzr.core.network.dto.Image as NetworkImage


fun NetworkImage.toDomain(): DomainImage {
    return DomainImage(
        url = url,
        width = width,
        height = height
    )
}