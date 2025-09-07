package com.music.dzr.core.auth.data.di

import com.music.dzr.core.auth.data.remote.http.AuthInterceptor
import com.music.dzr.core.auth.data.remote.http.TokenAuthenticator
import com.music.dzr.core.network.BuildConfig
import com.music.dzr.core.network.di.AuthInterceptorQualifier
import com.music.dzr.core.network.di.AuthenticatorQualifier
import com.music.dzr.core.network.di.JsonConverterFactoryQualifier
import com.music.dzr.core.network.di.NetworkResponseCallAdapterFactoryQualifier
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit

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
        TokenAuthenticator(tokenRepository = get())
    }

    single(AuthRetrofitQualifier) {
        Retrofit.Builder()
            .baseUrl(BuildConfig.SPOTIFY_ACCOUNTS_URL)
            .client(get(AuthClientQualifier))
            .addConverterFactory(get(JsonConverterFactoryQualifier))
            .addCallAdapterFactory(get(NetworkResponseCallAdapterFactoryQualifier))
            .build()
    }

}
