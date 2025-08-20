package com.music.dzr.core.data.mapper

import com.music.dzr.core.model.PopularityLevel


fun PopularityLevel.Companion.parse(networkPopularity: Int): PopularityLevel {
    return when {
        networkPopularity >= 75 -> PopularityLevel.VERY_HIGH
        networkPopularity >= 50 -> PopularityLevel.HIGH
        networkPopularity >= 25 -> PopularityLevel.MEDIUM
        else -> PopularityLevel.LOW
    }
}