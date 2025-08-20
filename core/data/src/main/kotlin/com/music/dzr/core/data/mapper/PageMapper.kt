package com.music.dzr.core.data.mapper

import com.music.dzr.core.model.CursorPage
import com.music.dzr.core.model.OffsetPage
import com.music.dzr.core.network.dto.CursorPaginatedList as NetworkCursorPage
import com.music.dzr.core.network.dto.PaginatedList as NetworkOffsetPage


fun <I, O> NetworkOffsetPage<I>.toDomain(
    mapContent: (I) -> O
): OffsetPage<O> {
    return OffsetPage(
        items = items.map(mapContent),
        total = total
    )
}

fun <I, O> NetworkCursorPage<I>.toDomain(
    mapContent: (I) -> O
): CursorPage<O> {
    return CursorPage(
        items = items.map(mapContent),
        nextCursor = next
    )
}
