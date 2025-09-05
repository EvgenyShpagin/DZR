package com.music.dzr.library.album.data.remote.source

import com.music.dzr.core.network.dto.NetworkResponse
import com.music.dzr.core.network.dto.PaginatedList
import com.music.dzr.library.album.data.remote.api.AlbumApi
import com.music.dzr.library.album.data.remote.dto.Album
import com.music.dzr.library.album.data.remote.dto.AlbumTrack
import com.music.dzr.library.album.data.remote.dto.Albums
import com.music.dzr.library.album.data.remote.dto.SavedAlbum

/**
 * Remote data source for album-related operations.
 * Thin wrapper around [AlbumApi] with convenient method signatures and parameter preparation.
 */
internal interface AlbumRemoteDataSource {

    /**
     * Retrieves detailed information about a single album.
     */
    suspend fun getAlbum(
        id: String,
        market: String? = null
    ): NetworkResponse<Album>

    /**
     * Retrieves detailed information for multiple albums.
     */
    suspend fun getMultipleAlbums(
        ids: List<String>,
        market: String? = null
    ): NetworkResponse<Albums>

    /**
     * Retrieves a paginated list of tracks for a given album.
     */
    suspend fun getAlbumTracks(
        id: String,
        market: String? = null,
        limit: Int? = null,
        offset: Int? = null
    ): NetworkResponse<PaginatedList<AlbumTrack>>

    /**
     * Retrieves a paginated list of albums saved in the current user’s library.
     */
    suspend fun getUserSavedAlbums(
        limit: Int? = null,
        offset: Int? = null,
        market: String? = null
    ): NetworkResponse<PaginatedList<SavedAlbum>>

    /**
     * Save one or more albums to the current user’s library.
     */
    suspend fun saveAlbumsForUser(ids: List<String>): NetworkResponse<Unit>

    /**
     * Remove one or more albums from the current user’s library.
     */
    suspend fun removeAlbumsForUser(ids: List<String>): NetworkResponse<Unit>
}
