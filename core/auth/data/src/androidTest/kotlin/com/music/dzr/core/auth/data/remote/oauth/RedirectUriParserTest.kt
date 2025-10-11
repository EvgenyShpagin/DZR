package com.music.dzr.core.auth.data.remote.oauth

import com.music.dzr.core.auth.data.remote.model.RedirectUriParams
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class RedirectUriParserTest {

    @Test
    fun returnsSuccess_onValidSuccessURI() {
        // Arrange
        val successUri = "https://example.com/callback?code=AUTH_CODE_123&state=STATE_XYZ"

        // Act
        val result = successUri.parseRedirectUriParams()

        // Assert
        assertIs<RedirectUriParams.Success>(result, "Result should be Success")
        assertEquals("AUTH_CODE_123", result.code)
        assertEquals("STATE_XYZ", result.state)
    }

    @Test
    fun returnsError_onValidErrorURI() {
        // Arrange
        val errorUri = "https://example.com/callback" +
                "?error=access_denied&state=STATE_XYZ&error_description=User%20denied%20access"

        // Act
        val result = errorUri.parseRedirectUriParams()

        // Assert
        assertIs<RedirectUriParams.Error>(result, "Result should be Error")
        assertEquals("access_denied", result.error)
        assertEquals("STATE_XYZ", result.state)
        assertEquals("User denied access", result.errorDescription)
    }

    @Test
    fun returnInvalid_onMalformedURI() {
        // Arrange
        val invalidUri = "this is not a valid uri"

        // Act
        val result = invalidUri.parseRedirectUriParams()

        // Assert
        assertIs<RedirectUriParams.Invalid>(result, "Result should be Invalid")
    }

    @Test
    fun returnSuccess_onCustomSchemeURI() {
        // Arrange
        val customSchemeUri = "dzr-auth://callback?code=CUSTOM_CODE&state=CUSTOM_STATE"

        // Act
        val result = customSchemeUri.parseRedirectUriParams()

        // Assert
        assertIs<RedirectUriParams.Success>(result, "Result should be Success")
        assertEquals("CUSTOM_CODE", result.code)
        assertEquals("CUSTOM_STATE", result.state)
    }
}