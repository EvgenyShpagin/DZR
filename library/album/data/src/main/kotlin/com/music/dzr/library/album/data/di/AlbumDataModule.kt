package com.music.dzr.library.album.data.di

import com.music.dzr.core.network.di.ApiRetrofitQualifier
import com.music.dzr.library.album.data.remote.api.AlbumApi
import com.music.dzr.library.album.data.remote.source.AlbumRemoteDataSource
import com.music.dzr.library.album.data.remote.source.AlbumRemoteDataSourceImpl
import com.music.dzr.library.album.data.repository.AlbumRepositoryImpl
import com.music.dzr.library.album.domain.repository.AlbumRepository
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.create

val albumDataModule = module {
    single<AlbumApi> { get<Retrofit>(ApiRetrofitQualifier).create<AlbumApi>() }
    single<AlbumRemoteDataSource> { AlbumRemoteDataSourceImpl(get()) }
    single<AlbumRepository> { AlbumRepositoryImpl(get(), get()) }
}
