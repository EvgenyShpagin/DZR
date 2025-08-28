package com.music.dzr.core.model

/**
 * Represents a market (country/region) where content is available.
 *
 * Markets are identified by ISO 3166-1 alpha-2 country codes (e.g., "US", "GB", "DE").
 * Using a value class provides type safety while avoiding runtime overhead.
 */
@JvmInline
value class Market(val code: String) {

    init {
        require(code.isNotBlank()) { "Market code cannot be blank" }
        require(code.length == 2) { "Market code must be 2 characters (ISO 3166-1 alpha-2)" }
        require(code.all { it.isUpperCase() }) { "Market code must be uppercase" }
    }

    override fun toString(): String = code

    companion object {
        /**
         * A special value representing an unspecified market.
         *
         * This must be mapped to a null or omitted parameter in any external API call.
         */
        val Unspecified = Market("ZZ")
    }
}
