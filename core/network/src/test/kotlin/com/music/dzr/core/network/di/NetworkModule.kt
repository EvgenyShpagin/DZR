package com.music.dzr.core.network.di

import com.music.dzr.core.oauth.repository.TokenRepository
import io.mockk.mockk
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.dsl.module
import org.koin.test.verify.verify
import kotlin.test.Test

@OptIn(KoinExperimentalAPI::class)
class NetworkModuleTest {

    @Test
    fun verifiesDependencyGraphSuccessfully() {
        // Arrange: The network module requires a TokenRepository.
        // We provide a relaxed mock to satisfy this dependency for verification.
        val mockTokenRepositoryModule = module {
            single<TokenRepository> { mockk(relaxed = true) }
        }

        // Act & Assert: Combine the real network module with the mock and run Koin's verification.
        module {
            includes(networkModule, mockTokenRepositoryModule)
        }.verify()
    }
}