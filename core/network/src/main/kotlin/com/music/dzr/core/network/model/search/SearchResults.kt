package com.music.dzr.core.network.model.search

import com.music.dzr.core.network.model.album.SimplifiedAlbum
import com.music.dzr.core.network.model.SimplifiedArtist
import com.music.dzr.core.network.model.playlist.PlaylistWithTracksInfo
import com.music.dzr.core.network.model.PaginatedList
import com.music.dzr.core.network.model.Track
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
    val artists: PaginatedList<SimplifiedArtist>? = null,
    val albums: PaginatedList<SimplifiedAlbum>? = null,
    val playlists: PaginatedList<PlaylistWithTracksInfo>? = null,
)