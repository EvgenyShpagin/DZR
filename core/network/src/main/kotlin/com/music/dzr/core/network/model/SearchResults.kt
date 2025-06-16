package com.music.dzr.core.network.model

import kotlinx.serialization.Serializable

/**
 * Represents the response from a search query to the Spotify API.
 *
 * This class holds the results of a search, which can include paginated lists of
 * tracks, artists, albums, and playlists.
 *
 * See: [Search for Item](https://developer.spotify.com/documentation/web-api/reference/search)
 */
@Serializable
data class SearchResults(
    val tracks: PaginatedList<Track>? = null,
    val artists: PaginatedList<Artist>? = null,
    val albums: PaginatedList<Album>? = null,
    val playlists: PaginatedList<PlaylistWithTracksInfo>? = null,
)