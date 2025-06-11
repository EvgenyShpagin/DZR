package com.music.dzr.core.network.api

import com.music.dzr.core.network.model.BrowseCategoriesContainer
import com.music.dzr.core.network.model.BrowseCategory
import com.music.dzr.core.network.model.NetworkResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface BrowseCategoryApi {

    /**
     * Get a list of categories used to tag items in Spotify (on, for example, the Spotify player's "Browse" tab).
     *
     * @param locale The desired language, consisting of an ISO 639-1 language code and an ISO 3166-1 alpha-2 country code, joined by an underscore. For example: es_MX.
     *               if locale is not supplied, or if the specified language is not available will be used American English.
     * @param limit  The maximum number of items to return. Default: 20. Minimum: 1. Maximum: 50.
     * @param offset The index of the first item to return. Default: 0 (the first item). Use with limit to get the next set of items.
     */
    @GET("browse/categories")
    suspend fun getMultipleBrowseCategories(
        @Query("locale") locale: String? = null,
        @Query("limit") limit: Int? = null,
        @Query("offset") offset: Int? = null
    ): NetworkResponse<BrowseCategoriesContainer>

    /**
     * Get a single category used to tag items in Spotify (on, for example, the Spotify player's "Browse" tab).
     *
     * @param id     The Spotify category ID for the category.
     * @param locale The desired language, consisting of an ISO 639-1 language code and an ISO 3166-1 alpha-2 country code, joined by an underscore. For example: es_MX.
     *               if locale is not supplied, or if the specified language is not available will be used American English.
     */
    @GET("browse/categories/{id}")
    suspend fun getSingleBrowseCategory(
        @Path("id") id: String,
        @Query("locale") locale: String? = null
    ): NetworkResponse<BrowseCategory>
}
