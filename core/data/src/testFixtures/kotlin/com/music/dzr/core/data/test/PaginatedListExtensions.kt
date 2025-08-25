package com.music.dzr.core.data.test

import com.music.dzr.core.network.dto.PaginatedList

/**
 * Test-only helper to create a [PaginatedList] from an in-memory [List].
 *
 * Intended for unit/integration tests and fake remote data sources.
 */
fun <T> List<T>.toPaginatedList(
    limit: Int? = null,
    offset: Int? = null
): PaginatedList<T> {
    val offset = offset ?: 0
    val limit = limit ?: size
    return PaginatedList(
        items = drop(offset).take(limit),
        limit = limit,
        offset = offset,
        total = size,
        href = "",
        next = null,
        previous = null
    )
}
