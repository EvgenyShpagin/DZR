package com.music.dzr.core.network.di

import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.dsl.module
import org.koin.test.verify.verify
import kotlin.test.Test

@OptIn(KoinExperimentalAPI::class)
class NetworkModuleTest {

    @Test
    fun verifiesDependencyGraphSuccessfully() {
        // Act & Assert: Combine the real network module with the mock and run Koin's verification.
        module {
            includes(networkModule)
        }.verify()
    }
}