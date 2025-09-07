package com.music.dzr.core.network.di

import com.music.dzr.core.network.BuildConfig
import com.music.dzr.core.network.http.RateLimitInterceptor
import com.music.dzr.core.network.http.TokenAuthenticator
import okhttp3.Authenticator
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module

internal val httpClientModule = module {

    single<HttpLoggingInterceptor.Logger> { HttpLoggingInterceptor.Logger.DEFAULT }

    single {
        HttpLoggingInterceptor(get()).apply {
            if (BuildConfig.DEBUG) {
                level = HttpLoggingInterceptor.Level.BODY
            }
        }
    }

    single { RateLimitInterceptor() }

    single<Authenticator> {
        TokenAuthenticator(tokenRepository = get())
    }

    single(AuthClientQualifier) {
        OkHttpClient.Builder()
            .addInterceptor(get<HttpLoggingInterceptor>())
            .build()
    }

    single(ApiClientQualifier) {
        OkHttpClient.Builder()
            .authenticator(get<Authenticator>())
            .addInterceptor(get<Interceptor>(AuthInterceptorQualifier))
            .addInterceptor(get<HttpLoggingInterceptor>())
            .addNetworkInterceptor(get<RateLimitInterceptor>())
            .build()
    }
} 