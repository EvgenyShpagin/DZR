package com.music.dzr.core.model.shared

/**
 * Content access restriction reason.
 */
enum class ContentRestriction {
    /** Not available in market */
    MARKET,

    /** Explicit content filter */
    EXPLICIT,

    /** Product subscription required */
    PRODUCT
}
