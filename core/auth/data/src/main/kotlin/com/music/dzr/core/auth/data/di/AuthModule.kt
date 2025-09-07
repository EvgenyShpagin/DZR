package com.music.dzr.core.auth.data.di

import com.music.dzr.core.auth.data.remote.http.AuthInterceptor
import com.music.dzr.core.network.di.AuthInterceptorQualifier
import org.koin.dsl.module

val authModule = module {
    single(AuthInterceptorQualifier) { AuthInterceptor(get()) }
}
