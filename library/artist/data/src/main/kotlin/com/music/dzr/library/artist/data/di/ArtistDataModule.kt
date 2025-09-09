package com.music.dzr.library.artist.data.di

import com.music.dzr.core.network.di.ApiRetrofitQualifier
import com.music.dzr.library.artist.data.remote.api.ArtistApi
import com.music.dzr.library.artist.data.remote.source.ArtistRemoteDataSource
import com.music.dzr.library.artist.data.remote.source.ArtistRemoteDataSourceImpl
import com.music.dzr.library.artist.data.repository.ArtistRepositoryImpl
import com.music.dzr.library.artist.domain.repository.ArtistRepository
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.create

val artistDataModule = module {
    single<ArtistApi> { get<Retrofit>(ApiRetrofitQualifier).create<ArtistApi>() }
    single<ArtistRemoteDataSource> { ArtistRemoteDataSourceImpl(get()) }
    single<ArtistRepository> { ArtistRepositoryImpl(get(), get()) }
}
