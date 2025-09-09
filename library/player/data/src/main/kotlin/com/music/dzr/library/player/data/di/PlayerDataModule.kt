package com.music.dzr.library.player.data.di

import com.music.dzr.core.network.di.ApiRetrofitQualifier
import com.music.dzr.library.player.data.remote.api.PlayerApi
import com.music.dzr.library.player.data.remote.source.PlayerRemoteDataSource
import com.music.dzr.library.player.data.remote.source.PlayerRemoteDataSourceImpl
import com.music.dzr.library.player.data.repository.PlayerRepositoryImpl
import com.music.dzr.player.domain.repository.PlayerRepository
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.create

val playerDataModule = module {
    single<PlayerApi> { get<Retrofit>(ApiRetrofitQualifier).create<PlayerApi>() }
    single<PlayerRemoteDataSource> { PlayerRemoteDataSourceImpl(get()) }
    single<PlayerRepository> { PlayerRepositoryImpl(get(), get(), get()) }
}
