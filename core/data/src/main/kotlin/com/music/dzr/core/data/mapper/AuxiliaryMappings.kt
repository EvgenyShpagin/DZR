package com.music.dzr.core.data.mapper

import com.music.dzr.core.network.util.UriBuilder

fun idToUri(type: String, id: String): String {
    return UriBuilder.build(type, id)
}

fun trackIdToUri(id: String): String {
    return idToUri(type = "track", id = id)
}
