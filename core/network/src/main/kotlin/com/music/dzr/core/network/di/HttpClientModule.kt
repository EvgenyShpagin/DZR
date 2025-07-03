package com.music.dzr.core.network.di

import com.music.dzr.core.network.BuildConfig
import com.music.dzr.core.network.http.AuthInterceptor
import com.music.dzr.core.network.http.RateLimitInterceptor
import com.music.dzr.core.network.http.TokenAuthenticator
import okhttp3.Authenticator
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.qualifier.named
import org.koin.dsl.module

internal const val API_CLIENT = "ApiOkHttpClient"
internal const val AUTH_CLIENT = "AuthOkHttpClient"

internal val httpClientModule = module {

    single<HttpLoggingInterceptor.Logger> { HttpLoggingInterceptor.Logger.DEFAULT }

    single {
        HttpLoggingInterceptor(get()).apply {
            if (BuildConfig.DEBUG) {
                level = HttpLoggingInterceptor.Level.BODY
            }
        }
    }

    single { AuthInterceptor(get()) }

    single { RateLimitInterceptor() }

    single<Authenticator> {
        TokenAuthenticator(
            tokenRepository = get(),
            clientId = BuildConfig.SPOTIFY_CLIENT_ID,
            authApi = get()
        )
    }

    single(named(AUTH_CLIENT)) {
        OkHttpClient.Builder()
            .addInterceptor(get<HttpLoggingInterceptor>())
            .build()
    }

    single(named(API_CLIENT)) {
        OkHttpClient.Builder()
            .authenticator(get<Authenticator>())
            .addInterceptor(get<AuthInterceptor>())
            .addInterceptor(get<HttpLoggingInterceptor>())
            .addNetworkInterceptor(get<RateLimitInterceptor>())
            .build()
    }
} 