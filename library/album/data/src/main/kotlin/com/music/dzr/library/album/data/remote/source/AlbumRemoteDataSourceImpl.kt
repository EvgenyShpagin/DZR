package com.music.dzr.library.album.data.remote.source

import com.music.dzr.core.network.dto.NetworkResponse
import com.music.dzr.core.network.dto.PaginatedList
import com.music.dzr.library.album.data.remote.api.AlbumApi
import com.music.dzr.library.album.data.remote.dto.Album
import com.music.dzr.library.album.data.remote.dto.AlbumTrack
import com.music.dzr.library.album.data.remote.dto.Albums
import com.music.dzr.library.album.data.remote.dto.SavedAlbum

internal class AlbumRemoteDataSourceImpl(
    private val albumApi: AlbumApi
) : AlbumRemoteDataSource {

    /**
     * Retrieves detailed information about a single album.
     */
    override suspend fun getAlbum(id: String, market: String?): NetworkResponse<Album> {
        return albumApi.getAlbum(id, market)
    }

    /**
     * Retrieves detailed information for multiple albums.
     */
    override suspend fun getMultipleAlbums(
        ids: List<String>,
        market: String?
    ): NetworkResponse<Albums> {
        val csv = ids.joinToString(",")
        return albumApi.getMultipleAlbums(csv, market)
    }

    /**
     * Retrieves a paginated list of tracks for a given album.
     */
    override suspend fun getAlbumTracks(
        id: String,
        market: String?,
        limit: Int?,
        offset: Int?
    ): NetworkResponse<PaginatedList<AlbumTrack>> {
        return albumApi.getAlbumTracks(id, market, limit, offset)
    }

    /**
     * Retrieves a paginated list of albums saved in the current user’s library.
     */
    override suspend fun getUserSavedAlbums(
        limit: Int?,
        offset: Int?,
        market: String?
    ): NetworkResponse<PaginatedList<SavedAlbum>> {
        return albumApi.getUserSavedAlbums(limit, offset, market)
    }

    /**
     * Save one or more albums to the current user’s library.
     */
    override suspend fun saveAlbumsForUser(ids: List<String>): NetworkResponse<Unit> {
        val csv = ids.joinToString(",")
        return albumApi.saveAlbumsForUser(ids = csv)
    }

    /**
     * Remove one or more albums from the current user’s library.
     */
    override suspend fun removeAlbumsForUser(ids: List<String>): NetworkResponse<Unit> {
        val csv = ids.joinToString(",")
        return albumApi.removeAlbumsForUser(ids = csv)
    }
}