package com.music.dzr.core.auth.data.mapper

import com.music.dzr.core.auth.data.local.model.authToken
import com.music.dzr.core.auth.domain.model.AuthScope
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue
import com.music.dzr.core.auth.data.remote.dto.AuthToken as NetworkToken
import com.music.dzr.core.auth.domain.model.AuthToken as DomainToken

class AuthTokenMappingsTest {

    @Test
    fun networkToDomain_parsesScopesAndCopiesFields() {
        // Arrange
        val network = NetworkToken(
            accessToken = "ACCESS",
            refreshToken = "REFRESH",
            expiresIn = 3600,
            scope = "user-read-email user-read-private",
            tokenType = "Bearer"
        )

        // Act
        val domain = network.toDomain()

        // Assert
        assertEquals("ACCESS", domain.accessToken)
        assertEquals("Bearer", domain.tokenType)
        assertEquals(3600, domain.expiresInSeconds)
        assertEquals("REFRESH", domain.refreshToken)
        assertEquals(
            listOf(AuthScope("user-read-email"), AuthScope("user-read-private")),
            domain.scopes
        )
    }

    @Test
    fun networkToDomain_handlesNullScopeAndNullRefresh() {
        // Arrange
        val network = NetworkToken(
            accessToken = "ACCESS",
            refreshToken = null,
            expiresIn = 1800,
            scope = null,
            tokenType = "Bearer"
        )

        // Act
        val domain = network.toDomain()

        // Assert
        assertNull(domain.refreshToken)
        assertTrue(domain.scopes.isEmpty())
        assertEquals(1800, domain.expiresInSeconds)
    }

    @Test
    fun domainToNetwork_joinsScopesAndCopiesFields() {
        // Arrange
        val domain = DomainToken(
            accessToken = "ACCESS",
            tokenType = "Bearer",
            expiresInSeconds = 3600,
            refreshToken = "REFRESH",
            scopes = listOf(AuthScope("a"), AuthScope("b"))
        )

        // Act
        val network = domain.toNetwork()

        // Assert
        assertEquals("ACCESS", network.accessToken)
        assertEquals("Bearer", network.tokenType)
        assertEquals(3600, network.expiresIn)
        assertEquals("REFRESH", network.refreshToken)
        assertEquals("a b", network.scope)
    }

    @Test
    fun domainToLocal_handleOptionalPresence() {
        // Arrange: domain without refresh and scopes
        val domain = DomainToken(
            accessToken = "ACCESS",
            tokenType = "Bearer",
            expiresInSeconds = 1200,
            refreshToken = null,
            scopes = emptyList()
        )

        // Act
        val local = domain.toLocal()

        // Assert: presence bits are not set when values are absent
        assertFalse(local.hasRefreshToken())
        assertFalse(local.hasScope())
    }

    @Test
    fun localToDomain_handleOptionalPresence() {
        // Arrange: local with optional fields present
        val local = authToken {
            accessToken = "X"
            refreshToken = "R"
            expiresIn = 10
            scope = "read write"
            tokenType = "Bearer"
        }
        val domain = local.toDomain()

        // Assert optionals are mapped when present
        assertEquals("R", domain.refreshToken)
        assertEquals(listOf(AuthScope("read"), AuthScope("write")), domain.scopes)
    }
}
