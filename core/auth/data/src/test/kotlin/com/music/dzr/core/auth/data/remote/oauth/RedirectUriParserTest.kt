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
        assertIs<RedirectUriParams.Success>(result)
        assertEquals("AUTH_CODE_123", result.code)
        assertEquals("STATE_XYZ", result.state)
    }

    @Test
    fun returnsError_onValidErrorUriWithEncodedSpaces() {
        // Arrange
        val errorUri = "https://example.com/callback" +
            "?error=access_denied&state=STATE_XYZ&error_description=User%20denied%20access"

        // Act
        val result = errorUri.parseRedirectUriParams()

        // Assert
        assertIs<RedirectUriParams.Error>(result)
        assertEquals("access_denied", result.error)
        assertEquals("STATE_XYZ", result.state)
        assertEquals("User denied access", result.errorDescription)
    }

    @Test
    fun returnsError_onValidErrorUriWithPlusAsSpace() {
        // Arrange
        val errorUri = "https://example.com/callback" +
            "?error=access_denied&state=STATE_XYZ&error_description=User+denied+access"

        // Act
        val result = errorUri.parseRedirectUriParams()

        // Assert
        assertIs<RedirectUriParams.Error>(result)
        assertEquals("access_denied", result.error)
        assertEquals("STATE_XYZ", result.state)
        assertEquals("User denied access", result.errorDescription)
    }

    @Test
    fun returnInvalid_onMalformedUri() {
        // Arrange
        val invalidUri = "this is not a valid uri"

        // Act
        val result = invalidUri.parseRedirectUriParams()

        // Assert
        assertIs<RedirectUriParams.Invalid>(result)
    }

    @Test
    fun returnSuccess_onCustomSchemeUri() {
        // Arrange
        val customSchemeUri = "dzr-auth://callback?code=CUSTOM_CODE&state=CUSTOM_STATE"

        // Act
        val result = customSchemeUri.parseRedirectUriParams()

        // Assert
        assertIs<RedirectUriParams.Success>(result)
        assertEquals("CUSTOM_CODE", result.code)
        assertEquals("CUSTOM_STATE", result.state)
    }

    @Test
    fun returnsInvalid_whenMissingRequiredParams() {
        // Arrange
        val missingState = "https://example.com/callback?code=AUTH_CODE_123"
        val missingCode = "https://example.com/callback?state=S"

        // Act
        val result1 = missingState.parseRedirectUriParams()
        val result2 = missingCode.parseRedirectUriParams()

        // Assert
        assertIs<RedirectUriParams.Invalid>(result1)
        assertIs<RedirectUriParams.Invalid>(result2)
    }

    @Test
    fun usesFirstValue_onRepeatedKeys() {
        // Arrange
        val uri = "https://example.com/callback?code=FIRST&code=SECOND&state=S&state=T"

        // Act
        val result = uri.parseRedirectUriParams()

        // Assert
        assertIs<RedirectUriParams.Success>(result)
        assertEquals("FIRST", result.code)
        assertEquals("S", result.state)
    }
}
