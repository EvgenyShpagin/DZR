package com.music.dzr.core.network.di

import org.koin.core.qualifier.named

internal val ApiClientQualifier = named("ApiOkHttpClient")

// Public qualifiers (for consumers of core:network)
val ApiRetrofitQualifier = named("ApiRetrofit")
val JsonConverterFactoryQualifier = named("JsonConverterFactory")
val NetworkResponseCallAdapterFactoryQualifier = named("NetworkResponseCallAdapterFactory")

// Qualifiers expected to be provided by external modules (e.g., core:auth)
val AuthInterceptorQualifier = named("AuthInterceptor")
val AuthenticatorQualifier = named("Authenticator")
