package com.music.dzr.core.data.mapper

import com.music.dzr.core.model.CopyrightType
import com.music.dzr.core.model.Copyright as DomainCopyright
import com.music.dzr.core.network.dto.Copyright as NetworkCopyright

fun NetworkCopyright.toDomain(): DomainCopyright {
    return DomainCopyright(
        statement = text,
        kind = type.toCopyrightType()
    )
}

private fun String.toCopyrightType(): CopyrightType {
    return when (this) {
        "C" -> CopyrightType.COPYRIGHT
        else -> CopyrightType.PERFORMANCE
    }
}
