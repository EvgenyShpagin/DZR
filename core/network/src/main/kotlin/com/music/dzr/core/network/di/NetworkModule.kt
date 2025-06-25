package com.music.dzr.core.network.di

import com.music.dzr.core.network.BuildConfig
import com.music.dzr.core.network.api.AlbumApi
import com.music.dzr.core.network.api.ArtistApi
import com.music.dzr.core.network.api.AuthApi
import com.music.dzr.core.network.api.BrowseCategoryApi
import com.music.dzr.core.network.api.MarketApi
import com.music.dzr.core.network.api.PlayerApi
import com.music.dzr.core.network.api.PlaylistApi
import com.music.dzr.core.network.api.SearchApi
import com.music.dzr.core.network.api.TrackApi
import com.music.dzr.core.network.api.UserApi
import com.music.dzr.core.network.http.TokenAuthenticator
import com.music.dzr.core.network.http.AuthInterceptor
import com.music.dzr.core.network.retrofit.NetworkErrorResponseParser
import com.music.dzr.core.network.retrofit.NetworkResponseCallAdapterFactory
import com.music.dzr.core.network.retrofit.UrlParameterConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import retrofit2.create

private const val API_CLIENT = "ApiOkHttpClient"
private const val AUTH_CLIENT = "AuthOkHttpClient"

private const val API_RETROFIT = "ApiRetrofit"
private const val AUTH_RETROFIT = "AuthRetrofit"

private const val JSON_CONVERTER_FACTORY = "JsonConverterFactory"
private const val URL_PARAM_CONVERTER_FACTORY = "UrlParamConverterFactory"

val networkModule = module {

    single { Json { ignoreUnknownKeys = true } }

    single {
        HttpLoggingInterceptor().apply {
            if (BuildConfig.DEBUG) {
                level = HttpLoggingInterceptor.Level.BODY
            }
        }
    }

    single { NetworkErrorResponseParser(get()) }

    single(named(JSON_CONVERTER_FACTORY)) {
        get<Json>().asConverterFactory("application/json".toMediaType())
    }

    single(named(URL_PARAM_CONVERTER_FACTORY)) {
        UrlParameterConverterFactory()
    }

    single { NetworkResponseCallAdapterFactory(get()) }

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
            .build()
    }

    single(named(AUTH_RETROFIT)) {
        Retrofit.Builder()
            .baseUrl(BuildConfig.SPOTIFY_ACCOUNTS_URL)
            .client(get(named(AUTH_CLIENT)))
            .addConverterFactory(get(named(JSON_CONVERTER_FACTORY)))
            .addCallAdapterFactory(get<NetworkResponseCallAdapterFactory>())
            .build()
    }

    // This AuthApi is special, it doesn't use the AuthInterceptor to avoid a dependency cycle.
    single<AuthApi> {
        get<Retrofit>(named(AUTH_RETROFIT)).create<AuthApi>()
    }

    single { AuthInterceptor(tokenRepository = get()) }

    single<Authenticator> {
        TokenAuthenticator(
            tokenRepository = get(),
            clientId = BuildConfig.SPOTIFY_CLIENT_ID,
            authApi = get()
        )
    }

    single(named(API_RETROFIT)) {
        Retrofit.Builder()
            .baseUrl(BuildConfig.SPOTIFY_API_URL)
            .client(get(named(API_CLIENT)))
            .addConverterFactory(get(named(URL_PARAM_CONVERTER_FACTORY)))
            .addConverterFactory(get(named(JSON_CONVERTER_FACTORY)))
            .addCallAdapterFactory(get<NetworkResponseCallAdapterFactory>())
            .build()
    }

    // API Implementations
    single { get<Retrofit>(named(API_RETROFIT)).create<AlbumApi>() }
    single { get<Retrofit>(named(API_RETROFIT)).create<ArtistApi>() }
    single { get<Retrofit>(named(API_RETROFIT)).create<BrowseCategoryApi>() }
    single { get<Retrofit>(named(API_RETROFIT)).create<MarketApi>() }
    single { get<Retrofit>(named(API_RETROFIT)).create<PlayerApi>() }
    single { get<Retrofit>(named(API_RETROFIT)).create<PlaylistApi>() }
    single { get<Retrofit>(named(API_RETROFIT)).create<SearchApi>() }
    single { get<Retrofit>(named(API_RETROFIT)).create<TrackApi>() }
    single { get<Retrofit>(named(API_RETROFIT)).create<UserApi>() }
}