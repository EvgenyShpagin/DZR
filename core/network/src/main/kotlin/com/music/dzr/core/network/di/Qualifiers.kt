package com.music.dzr.core.network.di

import org.koin.core.qualifier.named

val ApiRetrofitQualifier = named("ApiRetrofit")

internal val ApiClientQualifier = named("ApiOkHttpClient")

// AuthInterceptor is provided by another Koin module
val AuthInterceptorQualifier = named("AuthInterceptor")

val JsonConverterFactoryQualifier = named("JsonConverterFactory")
internal val UrlParamConverterFactoryQualifier = named("UrlParamConverterFactory")
val NetworkResponseCallAdapterFactoryQualifier = named("NetworkResponseCallAdapterFactory")
