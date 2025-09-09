package com.music.dzr.library.track.data.di

import com.music.dzr.core.network.di.ApiRetrofitQualifier
import com.music.dzr.library.track.data.remote.api.TrackApi
import com.music.dzr.library.track.data.remote.source.TrackRemoteDataSource
import com.music.dzr.library.track.data.remote.source.TrackRemoteDataSourceImpl
import com.music.dzr.library.track.data.repository.TrackRepositoryImpl
import com.music.dzr.library.track.domain.repository.TrackRepository
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.create

val trackDataModule = module {
    single<TrackApi> { get<Retrofit>(ApiRetrofitQualifier).create<TrackApi>() }
    single<TrackRemoteDataSource> { TrackRemoteDataSourceImpl(get()) }
    single<TrackRepository> { TrackRepositoryImpl(get(), get(), get()) }
}
