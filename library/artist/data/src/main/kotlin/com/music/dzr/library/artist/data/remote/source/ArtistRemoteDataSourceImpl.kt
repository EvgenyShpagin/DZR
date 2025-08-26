package com.music.dzr.library.artist.data.remote.source

import com.music.dzr.core.network.dto.AlbumGroup
import com.music.dzr.core.network.dto.Artist
import com.music.dzr.core.network.dto.NetworkResponse
import com.music.dzr.core.network.dto.PaginatedList
import com.music.dzr.core.network.dto.Tracks
import com.music.dzr.library.artist.data.remote.api.ArtistApi
import com.music.dzr.library.artist.data.remote.dto.ArtistAlbum
import com.music.dzr.library.artist.data.remote.dto.Artists

internal class ArtistRemoteDataSourceImpl(
    private val artistApi: ArtistApi
) : ArtistRemoteDataSource {

    override suspend fun getArtist(
        id: String
    ): NetworkResponse<Artist> {
        return artistApi.getArtist(id)
    }

    override suspend fun getMultipleArtists(
        ids: List<String>
    ): NetworkResponse<Artists> {
        val csv = ids.joinToString(",")
        return artistApi.getMultipleArtists(csv)
    }

    override suspend fun getArtistAlbums(
        id: String,
        includeGroups: List<AlbumGroup>?,
        market: String?,
        limit: Int?,
        offset: Int?
    ): NetworkResponse<PaginatedList<ArtistAlbum>> {
        val groupsCsv = includeGroups?.joinToString(",") { it.name.lowercase() }
        return artistApi.getArtistAlbums(id, groupsCsv, market, limit, offset)
    }

    override suspend fun getArtistTopTracks(
        id: String,
        market: String?
    ): NetworkResponse<Tracks> {
        return artistApi.getArtistTopTracks(id, market)
    }
}