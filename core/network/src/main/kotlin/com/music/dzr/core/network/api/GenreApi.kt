package com.music.dzr.core.network.api

import com.music.dzr.core.network.model.ApiResponse
import com.music.dzr.core.network.model.Genre
import com.music.dzr.core.network.model.GenreArtist
import com.music.dzr.core.network.model.RadioBrief
import com.music.dzr.core.network.model.WholeList
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Retrofit API interface for Deezer genre endpoints
 *
 * @see <a href="https://developers.deezer.com/api/genre">Deezer Genre API Documentation</a>
 */
internal interface GenreApi {

    /**
     * Retrieves all available music genres.
     */
    @GET("genre")
    suspend fun getGenres(): ApiResponse<WholeList<Genre>>

    /**
     * Retrieves detailed information about a specific genre.
     */
    @GET("genre/{id}")
    suspend fun getGenre(@Path("id") genreId: Long): ApiResponse<Genre>

    /**
     * Retrieves all artists associated with a specific genre.
     */
    @GET("genre/{id}/artists")
    suspend fun getGenreArtists(@Path("id") genreId: Long): ApiResponse<WholeList<GenreArtist>>

    /**
     * Retrieves all radios associated with a specific genre.
     */
    @GET("genre/{id}/radios")
    suspend fun getGenreRadios(@Path("id") genreId: Long): ApiResponse<WholeList<RadioBrief>>

}