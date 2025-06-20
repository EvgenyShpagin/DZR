package com.music.dzr.core.network.retrofit

import com.music.dzr.core.network.model.NetworkError
import com.music.dzr.core.network.model.NetworkErrorType
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import okhttp3.ResponseBody
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.net.ssl.SSLException

/**
 * Converts Spotify API error responses and connection exceptions into a unified [NetworkError] model.
 *
 * @property json The [Json] instance used to deserialize error response bodies.
 */
internal class NetworkErrorResponseParser(private val json: Json) {

    /**
     * Parses an unsuccessful Retrofit [Response] into a structured [NetworkError].
     *
     * It first attempts to parse the response body for a standard API error format.
     * If parsing fails, it falls back to using the raw HTTP status code and message.
     * The error type is always set to [NetworkErrorType.HttpException].
     */
    fun parse(errorBody: ResponseBody): NetworkError {
        val text = errorBody.string()
        return try {
            val errorJson = json.parseToJsonElement(text).jsonObject["error"]!!.jsonObject

            NetworkError(
                type = NetworkErrorType.HttpException,
                message = errorJson["message"]!!.jsonPrimitive.content,
                code = errorJson["status"]!!.jsonPrimitive.int,
                reason = errorJson["reason"]?.jsonPrimitive?.contentOrNull
            )
        } catch (e: SerializationException) {
            NetworkError(
                type = NetworkErrorType.SerializationError,
                message = e.localizedMessage
                    ?: "Couldn't deserialize '$text' to ${NetworkError::class.simpleName}",
                code = NetworkErrorType.SerializationError.ordinal
            )
        } catch (e: Exception) {
            NetworkError(
                type = NetworkErrorType.Unknown,
                message = e.localizedMessage ?: "Unknown error",
                code = NetworkErrorType.Unknown.ordinal
            )
        }
    }

    /**
     * Converts a [Throwable] (typically from a network call) into a [NetworkError].
     *
     * This method inspects the type of [throwable] and maps it to the appropriate
     * [NetworkErrorType], populating the [NetworkError.message] and [NetworkError.code]
     * accordingly. Known exception types include:
     *  - [HttpException]: HTTP error with 4xx/5xx code.
     *  - [ConnectException]: TCP connection could not be established.
     *  - [UnknownHostException]: DNS resolution failure.
     *  - [SocketTimeoutException]: Connection or read/write timed out.
     *  - [SSLException]: Any SSL/TLS handshake or certificate issue.
     *  - [IOException]: General I/O failure (no network, stream error).
     *  - [SerializationException]: Deserialization issue with some object.
     *  - Any other [Throwable] is treated as an unknown error.
     */
    fun parse(throwable: Throwable): NetworkError {
        return when (throwable) {
            is HttpException -> NetworkError(
                type = NetworkErrorType.HttpException,
                message = throwable.localizedMessage ?: "HTTP error",
                code = throwable.code()
            )

            is ConnectException -> NetworkError(
                type = NetworkErrorType.UnreachableHost,
                message = throwable.localizedMessage ?: "Host unreachable",
                code = NetworkErrorType.UnreachableHost.ordinal
            )

            is UnknownHostException -> NetworkError(
                type = NetworkErrorType.UnknownHost,
                message = throwable.localizedMessage ?: "Unknown host",
                code = NetworkErrorType.UnknownHost.ordinal
            )

            is SocketTimeoutException -> NetworkError(
                type = NetworkErrorType.Timeout,
                message = throwable.localizedMessage ?: "Request timed out",
                code = NetworkErrorType.Timeout.ordinal
            )

            is SSLException -> NetworkError(
                type = NetworkErrorType.SslException,
                message = throwable.localizedMessage ?: "Generic SSL error",
                code = NetworkErrorType.SslException.ordinal
            )

            is IOException -> NetworkError(
                type = NetworkErrorType.SomeConnectionError,
                message = throwable.localizedMessage ?: "I/O error or no internet",
                code = NetworkErrorType.SomeConnectionError.ordinal
            )

            is SerializationException -> NetworkError(
                type = NetworkErrorType.SerializationError,
                message = throwable.localizedMessage
                    ?: "Couldn't deserialize to ${NetworkError::class.simpleName}",
                code = NetworkErrorType.SerializationError.ordinal
            )

            else -> NetworkError(
                type = NetworkErrorType.Unknown,
                message = throwable.localizedMessage ?: "Unknown error",
                code = NetworkErrorType.Unknown.ordinal
            )
        }
    }
}