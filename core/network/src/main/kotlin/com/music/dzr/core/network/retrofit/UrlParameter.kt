package com.music.dzr.core.network.retrofit

/**
 * An interface for types that require a specific string representation
 * when used as a URL parameter in Retrofit (e.g., with @Query or @Path).
 *
 * This provides a more explicit contract than overriding [toString] for providing
 * a value to be used in a URL.
 *
 * A custom [retrofit2.Converter.Factory] should be used to recognize this interface
 * and call [urlValue] to get the string representation.
 */
interface UrlParameter {
    /**
     * The string representation of the object to be used in a URL.
     */
    val urlValue: String
}