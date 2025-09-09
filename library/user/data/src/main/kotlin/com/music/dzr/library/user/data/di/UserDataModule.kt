package com.music.dzr.library.user.data.di

import com.music.dzr.core.network.di.ApiRetrofitQualifier
import com.music.dzr.library.user.data.remote.api.UserApi
import com.music.dzr.library.user.data.remote.source.UserRemoteDataSource
import com.music.dzr.library.user.data.remote.source.UserRemoteDataSourceImpl
import com.music.dzr.library.user.data.repository.UserRepositoryImpl
import com.music.dzr.library.user.domain.repository.UserRepository
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.create

val userDataModule = module {
    single<UserApi> { get<Retrofit>(ApiRetrofitQualifier).create<UserApi>() }
    single<UserRemoteDataSource> { UserRemoteDataSourceImpl(get()) }
    single<UserRepository> { UserRepositoryImpl(get(), get(), get()) }
}
