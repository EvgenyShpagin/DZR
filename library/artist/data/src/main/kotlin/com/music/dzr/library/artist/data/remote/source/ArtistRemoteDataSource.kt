package com.music.dzr.library.artist.data.remote.source

import com.music.dzr.core.network.dto.AlbumGroup
import com.music.dzr.core.network.dto.Artist
import com.music.dzr.core.network.dto.NetworkResponse
import com.music.dzr.core.network.dto.PaginatedList
import com.music.dzr.core.network.dto.Tracks
import com.music.dzr.library.artist.data.remote.api.ArtistApi
import com.music.dzr.library.artist.data.remote.dto.ArtistAlbum
import com.music.dzr.library.artist.data.remote.dto.Artists

/**
 * Remote data source for artist-related operations.
 * Thin wrapper around [ArtistApi] with convenient method signatures and parameter preparation.
 */
internal class ArtistRemoteDataSource(private val artistApi: ArtistApi) {

    /**
     * Get Spotify catalog information for a single artist.
     */
    suspend fun getArtist(id: String): NetworkResponse<Artist> {
        return artistApi.getArtist(id)
    }

    /**
     * Get Spotify catalog information for several artists.
     */
    suspend fun getMultipleArtists(ids: List<String>): NetworkResponse<Artists> {
        val csv = ids.joinToString(",")
        return artistApi.getMultipleArtists(csv)
    }

    /**
     * Get Spotify catalog information about an artist's albums.
     */
    suspend fun getArtistAlbums(
        id: String,
        includeGroups: List<AlbumGroup>? = null,
        market: String? = null,
        limit: Int? = null,
        offset: Int? = null
    ): NetworkResponse<PaginatedList<ArtistAlbum>> {
        val groupsCsv = includeGroups?.joinToString(",") { it.name.lowercase() }
        return artistApi.getArtistAlbums(id, groupsCsv, market, limit, offset)
    }

    /**
     * Get Spotify catalog information about an artist's top tracks by country.
     */
    suspend fun getArtistTopTracks(
        id: String,
        market: String? = null
    ): NetworkResponse<Tracks> {
        return artistApi.getArtistTopTracks(id, market)
    }
}