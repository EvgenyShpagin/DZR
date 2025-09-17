package com.music.dzr.core.auth.data.di

import com.music.dzr.core.auth.data.remote.http.AuthInterceptor
import com.music.dzr.core.auth.data.remote.http.AuthTokenAuthenticator
import com.music.dzr.core.auth.data.remote.oauth.AuthorizationUrlBuilder
import com.music.dzr.core.network.di.AuthInterceptorQualifier
import com.music.dzr.core.network.di.AuthenticatorQualifier
import com.music.dzr.core.network.di.JsonConverterFactoryQualifier
import com.music.dzr.core.network.di.NetworkResponseCallAdapterFactoryQualifier
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import com.music.dzr.core.auth.data.BuildConfig as AuthBuildConfig
import com.music.dzr.core.network.BuildConfig as NetworkBuildConfig

private val AuthRetrofitQualifier = named("AuthRetrofit")
private val AuthClientQualifier = named("AuthOkHttpClient")

val authModule = module {
    single(AuthInterceptorQualifier) { AuthInterceptor(get()) }

    single(AuthClientQualifier) {
        OkHttpClient.Builder()
            .addInterceptor(get<HttpLoggingInterceptor>())
            .build()
    }

    single(AuthenticatorQualifier) {
        AuthTokenAuthenticator(tokenRepository = get())
    }

    single(AuthRetrofitQualifier) {
        Retrofit.Builder()
            .baseUrl(AuthBuildConfig.SPOTIFY_AUTH_BASE_URL)
            .client(get(AuthClientQualifier))
            .addConverterFactory(get(JsonConverterFactoryQualifier))
            .addCallAdapterFactory(get(NetworkResponseCallAdapterFactoryQualifier))
            .build()
    }

    single {
        AuthorizationUrlBuilder(
            clientId = NetworkBuildConfig.SPOTIFY_CLIENT_ID,
            authBaseUrl = AuthBuildConfig.SPOTIFY_AUTH_BASE_URL,
        )
    }
}
