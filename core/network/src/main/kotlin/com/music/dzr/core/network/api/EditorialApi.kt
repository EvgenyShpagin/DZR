package com.music.dzr.core.network.api

import com.music.dzr.core.network.model.ApiResponse
import com.music.dzr.core.network.model.Editorial
import com.music.dzr.core.network.model.EditorialReleasesAlbum
import com.music.dzr.core.network.model.EditorialSelectionAlbum
import com.music.dzr.core.network.model.PaginatedList
import com.music.dzr.core.network.model.WholeList
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Retrofit API interface for Deezer editorial endpoints
 *
 * @see <a href="https://developers.deezer.com/api/editorial">Deezer Editorial API Documentation</a>
 */
internal interface EditorialApi {

    /**
     * Retrieves all available editorial objects.
     */
    @GET("editorial")
    suspend fun getEditorials(): ApiResponse<PaginatedList<Editorial>>

    /**
     * Retrieves detailed information about a specific editorial.
     */
    @GET("editorial/{id}")
    suspend fun getEditorial(@Path("id") editorialId: Long): ApiResponse<Editorial>

    /**
     * Retrieves albums selected weekly by the Deezer Team.
     */
    @GET("editorial/{id}/selection")
    suspend fun getEditorialSelection(
        @Path("id") editorialId: Long
    ): ApiResponse<WholeList<EditorialSelectionAlbum>>

    /**
     * Retrieves new releases per genre for the current country.
     */
    @GET("editorial/{id}/releases")
    suspend fun getEditorialReleases(
        @Path("id") editorialId: Long
    ): ApiResponse<PaginatedList<EditorialReleasesAlbum>>
}