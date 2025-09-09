package com.music.dzr.library.playlist.data.di

import com.music.dzr.core.network.di.ApiRetrofitQualifier
import com.music.dzr.library.playlist.data.remote.api.PlaylistApi
import com.music.dzr.library.playlist.data.remote.source.PlaylistRemoteDataSource
import com.music.dzr.library.playlist.data.remote.source.PlaylistRemoteDataSourceImpl
import com.music.dzr.library.playlist.data.repository.PlaylistRepositoryImpl
import com.music.dzr.library.playlist.domain.repository.PlaylistRepository
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.create

val playlistDataModule = module {
    single<PlaylistApi> { get<Retrofit>(ApiRetrofitQualifier).create<PlaylistApi>() }
    single<PlaylistRemoteDataSource> { PlaylistRemoteDataSourceImpl(get()) }
    single<PlaylistRepository> { PlaylistRepositoryImpl(get(), get(), get()) }
}
