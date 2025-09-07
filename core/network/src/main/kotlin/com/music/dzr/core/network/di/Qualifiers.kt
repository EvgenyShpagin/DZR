package com.music.dzr.core.network.di

import org.koin.core.qualifier.named

val ApiRetrofitQualifier = named("ApiRetrofit")

internal val ApiClientQualifier = named("ApiOkHttpClient")

// AuthInterceptor is provided by another Koin module
val AuthInterceptorQualifier = named("AuthInterceptor")

// Authenticator is provided by another Koin module
val AuthenticatorQualifier = named("Authenticator")

val JsonConverterFactoryQualifier = named("JsonConverterFactory")
internal val UrlParamConverterFactoryQualifier = named("UrlParamConverterFactory")
val NetworkResponseCallAdapterFactoryQualifier = named("NetworkResponseCallAdapterFactory")
