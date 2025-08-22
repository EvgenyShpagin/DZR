package com.music.dzr.library.user.data.repository

import com.music.dzr.core.coroutine.ApplicationScope
import com.music.dzr.core.coroutine.DispatcherProvider
import com.music.dzr.core.data.mapper.toDomain
import com.music.dzr.core.data.mapper.toResult
import com.music.dzr.core.error.AppError
import com.music.dzr.core.model.Artist
import com.music.dzr.core.model.OffsetPage
import com.music.dzr.core.model.Page
import com.music.dzr.core.model.Track
import com.music.dzr.core.model.User
import com.music.dzr.core.result.Result
import com.music.dzr.library.user.data.mapper.toDomain
import com.music.dzr.library.user.data.mapper.toNetwork
import com.music.dzr.library.user.data.remote.source.UserRemoteDataSource
import com.music.dzr.library.user.domain.model.CurrentUser
import com.music.dzr.library.user.domain.model.TimeRange
import com.music.dzr.library.user.domain.repository.UserRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext

internal class UserRepositoryImpl(
    private val remoteDataSource: UserRemoteDataSource,
    private val dispatchers: DispatcherProvider,
    private val externalScope: ApplicationScope
) : UserRepository {

    override suspend fun getCurrentUserProfile(): Result<CurrentUser, AppError> {
        return withContext(dispatchers.io) {
            remoteDataSource.getCurrentUserProfile().toResult { it.toDomain() }
        }
    }

    override suspend fun getUserTopArtists(
        timeRange: TimeRange?,
        limit: Int?,
        offset: Int?
    ): Result<OffsetPage<Artist>, AppError> {
        return withContext(dispatchers.io) {
            remoteDataSource.getUserTopArtists(
                timeRange = timeRange?.toNetwork(),
                limit = limit,
                offset = offset
            ).toResult { networkPage ->
                networkPage.toDomain { networkArtist ->
                    networkArtist.toDomain()
                }
            }
        }
    }

    override suspend fun getUserTopTracks(
        timeRange: TimeRange?,
        limit: Int?,
        offset: Int?
    ): Result<OffsetPage<Track>, AppError> {
        return withContext(dispatchers.io) {
            val networkTimeRange = timeRange?.toNetwork()
            remoteDataSource.getUserTopTracks(
                timeRange = networkTimeRange,
                limit = limit,
                offset = offset
            ).toResult { networkPage ->
                networkPage.toDomain { networkTrack ->
                    networkTrack.toDomain()
                }
            }
        }
    }

    override suspend fun getUserProfile(userId: String): Result<User, AppError> {
        return withContext(dispatchers.io) {
            remoteDataSource.getUserProfile(userId).toResult { it.toDomain() }
        }
    }

    override suspend fun followPlaylist(
        playlistId: String,
        asPublic: Boolean
    ): Result<Unit, AppError> {
        return withContext(dispatchers.io) {
            externalScope.async {
                remoteDataSource.followPlaylist(playlistId, asPublic).toResult()
            }.await()
        }
    }

    override suspend fun unfollowPlaylist(playlistId: String): Result<Unit, AppError> {
        return withContext(dispatchers.io) {
            externalScope.async {
                remoteDataSource.unfollowPlaylist(playlistId).toResult()
            }.await()
        }
    }

    override suspend fun getFollowedArtists(
        limit: Int?,
        after: String?
    ): Result<Page<Artist>, AppError> {
        return withContext(dispatchers.io) {
            remoteDataSource.getFollowedArtists(limit, after).toResult { networkCursorPage ->
                val content = networkCursorPage.list
                content.toDomain { it.toDomain() }
            }
        }
    }

    override suspend fun followArtists(ids: List<String>): Result<Unit, AppError> {
        return withContext(dispatchers.io) {
            externalScope.async {
                remoteDataSource.followArtists(ids).toResult()
            }.await()
        }
    }

    override suspend fun followUsers(ids: List<String>): Result<Unit, AppError> {
        return withContext(dispatchers.io) {
            externalScope.async {
                remoteDataSource.followUsers(ids).toResult()
            }.await()
        }
    }

    override suspend fun unfollowArtists(ids: List<String>): Result<Unit, AppError> {
        return withContext(dispatchers.io) {
            externalScope.async {
                remoteDataSource.unfollowArtists(ids).toResult()
            }.await()
        }
    }

    override suspend fun unfollowUsers(ids: List<String>): Result<Unit, AppError> {
        return withContext(dispatchers.io) {
            externalScope.async {
                remoteDataSource.unfollowUsers(ids).toResult()
            }.await()
        }
    }

    override suspend fun checkIfUserFollowsArtists(
        ids: List<String>
    ): Result<List<Boolean>, AppError> {
        return withContext(dispatchers.io) {
            remoteDataSource.checkIfUserFollowsArtists(ids).toResult()
        }
    }

    override suspend fun checkIfUserFollowsUsers(
        ids: List<String>
    ): Result<List<Boolean>, AppError> {
        return withContext(dispatchers.io) {
            remoteDataSource.checkIfUserFollowsUsers(ids).toResult()
        }
    }

    override suspend fun checkIfUsersFollowPlaylist(
        playlistId: String
    ): Result<Boolean, AppError> {
        return withContext(dispatchers.io) {
            remoteDataSource.checkIfUsersFollowPlaylist(playlistId).toResult()
        }
    }
}
