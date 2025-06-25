package com.music.dzr.core.network.retrofit

import com.music.dzr.core.network.api.getJsonBodyAsset
import com.music.dzr.core.network.model.error.NetworkError
import com.music.dzr.core.network.model.error.NetworkErrorType
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.net.ssl.SSLException

class ErrorResponseParserTest {

    private val json = Json { ignoreUnknownKeys = true }
    private val parser = NetworkErrorResponseParser(json)

    @Test
    fun parses_HttpException() {
        // Arrange
        val httpException = HttpException(
            Response.error<Any>(404, "Not Found".toResponseBody("text/plain".toMediaType()))
        )
        val expectedError = NetworkError(
            type = NetworkErrorType.HttpException,
            message = httpException.localizedMessage!!,
            code = 404
        )

        // Act
        val actualError = parser.parse(httpException)

        // Assert
        assertEquals(expectedError, actualError)
    }

    @Test
    fun parses_ConnectException() {
        // Arrange
        val connectException = ConnectException("Failed to connect")
        val expectedError = NetworkError(
            type = NetworkErrorType.UnreachableHost,
            message = "Failed to connect",
            code = NetworkErrorType.UnreachableHost.ordinal
        )

        // Act
        val actualError = parser.parse(connectException)

        // Assert
        assertEquals(expectedError, actualError)
    }

    @Test
    fun parses_UnknownHostException() {
        // Arrange
        val unknownHostException = UnknownHostException("Unable to resolve host")
        val expectedError = NetworkError(
            type = NetworkErrorType.UnknownHost,
            message = "Unable to resolve host",
            code = NetworkErrorType.UnknownHost.ordinal
        )

        // Act
        val actualError = parser.parse(unknownHostException)

        // Assert
        assertEquals(expectedError, actualError)
    }

    @Test
    fun parses_SocketTimeoutException() {
        // Arrange
        val socketTimeoutException = SocketTimeoutException("Read timed out")
        val expectedError = NetworkError(
            type = NetworkErrorType.Timeout,
            message = "Read timed out",
            code = NetworkErrorType.Timeout.ordinal
        )

        // Act
        val actualError = parser.parse(socketTimeoutException)

        // Assert
        assertEquals(expectedError, actualError)
    }

    @Test
    fun parses_SSLException() {
        // Arrange
        val sslException = SSLException("SSL handshake failed")
        val expectedError = NetworkError(
            type = NetworkErrorType.SslException,
            message = "SSL handshake failed",
            code = NetworkErrorType.SslException.ordinal
        )

        // Act
        val actualError = parser.parse(sslException)

        // Assert
        assertEquals(expectedError, actualError)
    }

    @Test
    fun parses_IOException() {
        // Arrange
        val ioException = IOException("Network error")
        val expectedError = NetworkError(
            type = NetworkErrorType.SomeConnectionError,
            message = "Network error",
            code = NetworkErrorType.SomeConnectionError.ordinal
        )

        // Act
        val actualError = parser.parse(ioException)

        // Assert
        assertEquals(expectedError, actualError)
    }

    @Test
    fun parses_SerializationException() {
        // Arrange
        val serializationException = SerializationException("Failed to parse")
        val expectedError = NetworkError(
            type = NetworkErrorType.SerializationError,
            message = "Failed to parse",
            code = NetworkErrorType.SerializationError.ordinal
        )

        // Act
        val actualError = parser.parse(serializationException)

        // Assert
        assertEquals(expectedError, actualError)
    }

    @Test
    fun parses_UnknownError() {
        // Arrange
        val unknownException = RuntimeException("Something went wrong")
        val expectedError = NetworkError(
            type = NetworkErrorType.Unknown,
            message = "Something went wrong",
            code = NetworkErrorType.Unknown.ordinal
        )

        // Act
        val actualError = parser.parse(unknownException)

        // Assert
        assertEquals(expectedError, actualError)
    }

    @Test
    fun parses_errorResponse_withReason() {
        // Arrange
        val errorJson = getJsonBodyAsset("responses/error/premium-required.json")
        val errorResponse = Response.error<Any>(403, errorJson.toResponseBody())
        val expectedError = NetworkError(
            type = NetworkErrorType.HttpException,
            message = "Player command failed: Premium required",
            reason = "PREMIUM_REQUIRED",
            code = 403
        )

        // Act
        val actualError = parser.parse(errorResponse.errorBody()!!)

        // Assert
        assertEquals(expectedError, actualError)
    }

    @Test
    fun parses_errorResponse_withoutReason() {
        // Arrange
        val errorJson = getJsonBodyAsset("responses/error/expired-token.json")
        val errorResponse = Response.error<Any>(401, errorJson.toResponseBody())
        val expectedError = NetworkError(
            type = NetworkErrorType.HttpException,
            message = "The access token expired",
            code = 401
        )

        // Act
        val actualError = parser.parse(errorResponse.errorBody()!!)

        // Assert
        assertEquals(expectedError, actualError)
    }

    @Test
    fun parses_oauthErrorResponse() {
        // Arrange
        val errorJson = getJsonBodyAsset("responses/error/oauth-error.json")
        val errorResponse = Response.error<Any>(400, errorJson.toResponseBody())
        val expectedError = NetworkError(
            type = NetworkErrorType.HttpException,
            message = "Invalid refresh token",
            reason = "invalid_grant",
            code = 400
        )

        // Act
        val actualError = parser.parse(errorResponse.errorBody()!!)

        // Assert
        assertEquals(expectedError, actualError)
    }
}