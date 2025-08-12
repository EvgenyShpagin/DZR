package com.music.dzr.feature.search.data.remote.api

import com.music.dzr.core.network.dto.NetworkResponse
import com.music.dzr.feature.search.data.remote.dto.SearchResults
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * A service for interacting with the Spotify Search API.
 * @see <a href="https://developer.spotify.com/documentation/web-api/">Spotify API</a>
 */
interface SearchApi {

    /**
     * Get Spotify catalog information about albums, artists, playlists, tracks that match a keyword string.
     *
     * @param query Your search query. You can narrow down your search using field filters. The available filters are:
     * - `album`: For albums and tracks.
     * - `artist`: For albums, artists and tracks.
     * - `track`: For tracks.
     * - `year`: For albums, artists and tracks. Can be a single year or a range (e.g. `1955-1960`).
     * - `upc`: For albums.
     * - `tag:new`: For albums released in the last two weeks.
     * - `tag:hipster`: For albums with the lowest 10% popularity.
     * - `isrc`: For tracks.
     * - `genre`: For artists and tracks.
     *
     * Example: `"remaster%20track:Doxy%20artist:Miles%20Davi"`(with encoded space)
     *
     * @param type A comma-separated list of item types to search for (e.g., `"album,track"`).
     * @param market An [ISO 3166-1 alpha-2 country code](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2).
     * If specified, only content playable in that market is returned.
     * @param limit The maximum number of results to return. Default: 20, Min: 1, Max: 50.
     * @param offset The index of the first result to return. Default: 0. Use with [limit] for pagination.
     * @param includeExternal If `"audio"` is specified, the response may include externally hosted
     * audio content and will mark it as playable.
     *
     * @see <a href="https://developer.spotify.com/documentation/web-api/reference/search">Search for Item</a>
     */
    @GET("search")
    suspend fun search(
        @Query("q") query: String,
        @Query("type") type: String,
        @Query("market") market: String? = null,
        @Query("limit") limit: Int? = null,
        @Query("offset") offset: Int? = null,
        @Query("include_external") includeExternal: String? = null
    ): NetworkResponse<SearchResults>
}