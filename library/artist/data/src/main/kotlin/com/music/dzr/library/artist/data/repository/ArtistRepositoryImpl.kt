package com.music.dzr.library.artist.data.repository

import com.music.dzr.core.coroutine.DispatcherProvider
import com.music.dzr.core.data.mapper.toDomain
import com.music.dzr.core.data.mapper.toNetwork
import com.music.dzr.core.data.mapper.toResult
import com.music.dzr.core.error.AppError
import com.music.dzr.core.model.DetailedArtist
import com.music.dzr.core.model.DetailedTrack
import com.music.dzr.core.model.Market
import com.music.dzr.core.model.ReleaseType
import com.music.dzr.core.pagination.OffsetPageable
import com.music.dzr.core.pagination.Page
import com.music.dzr.core.result.Result
import com.music.dzr.library.artist.data.mapper.toDomain
import com.music.dzr.library.artist.data.mapper.toNetwork
import com.music.dzr.library.artist.data.remote.source.ArtistRemoteDataSource
import com.music.dzr.library.artist.domain.model.AlbumInDiscography
import com.music.dzr.library.artist.domain.repository.ArtistRepository
import kotlinx.coroutines.withContext

internal class ArtistRepositoryImpl(
    private val remoteDataSource: ArtistRemoteDataSource,
    private val dispatchers: DispatcherProvider
) : ArtistRepository {

    override suspend fun getArtist(
        id: String
    ): Result<DetailedArtist, AppError> {
        return withContext(dispatchers.io) {
            remoteDataSource.getArtist(id).toResult { networkArtist ->
                networkArtist.toDomain()
            }
        }
    }

    override suspend fun getMultipleArtists(
        ids: List<String>
    ): Result<List<DetailedArtist>, AppError> {
        return withContext(dispatchers.io) {
            remoteDataSource.getMultipleArtists(ids).toResult { it.toDomain() }
        }
    }

    override suspend fun getArtistAlbums(
        id: String,
        releaseTypes: List<ReleaseType>,
        includeAppearsOn: Boolean,
        market: Market,
        pageable: OffsetPageable
    ): Result<Page<AlbumInDiscography>, AppError> {
        return withContext(dispatchers.io) {
            remoteDataSource.getArtistAlbums(
                id = id,
                includeGroups = releaseTypes.toNetwork(includeAppearsOn),
                market = market.toNetwork(),
                limit = pageable.limit,
                offset = pageable.offset
            ).toResult { networkPage ->
                networkPage.toDomain { it.toDomain() }
            }
        }
    }

    override suspend fun getArtistTopTracks(
        id: String,
        market: Market
    ): Result<List<DetailedTrack>, AppError> {
        return withContext(dispatchers.io) {
            remoteDataSource.getArtistTopTracks(
                id = id,
                market = market.toNetwork()
            ).toResult { tracksWrapper ->
                tracksWrapper.list.map { it.toDomain() }
            }
        }
    }
}
