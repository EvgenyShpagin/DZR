package com.music.dzr.core.network.di

import org.koin.core.qualifier.named

val ApiRetrofitQualifier = named("ApiRetrofit")
internal val AuthRetrofitQualifier = named("AuthRetrofit")

internal val ApiClientQualifier = named("ApiOkHttpClient")
internal val AuthClientQualifier = named("AuthOkHttpClient")

// AuthInterceptor is provided by another Koin module
val AuthInterceptorQualifier = named("AuthInterceptor")