package com.music.dzr.core.network.retrofit

import com.music.dzr.core.network.model.NetworkError
import com.music.dzr.core.network.model.NetworkErrorType
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.serializer
import okhttp3.ResponseBody
import retrofit2.HttpException
import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.net.ssl.SSLException

/**
 * Converts Deezer API error responses and connection exceptions into a unified [NetworkError] model.
 *
 * @property json The [Json] instance used to deserialize error response bodies.
 */
internal class NetworkErrorResponseConverter(private val json: Json) {

    /**
     * Parses the HTTP response body into a [NetworkError].
     *
     * The response body is expected to be a JSON object with a top-level "error" field.
     * This method reads the entire body as a string, converts it to a JSON element,
     * then decodes the "error" field into the [NetworkError] data class using the provided [json] instance.
     */
    fun parse(errorBody: ResponseBody): NetworkError {
        val text = errorBody.string()
        val root = json.parseToJsonElement(text).jsonObject
        root["error"]!!.let { errorElement ->
            val errorSerializer: KSerializer<NetworkError> = json.serializersModule.serializer()
            val apiError = json.decodeFromJsonElement(errorSerializer, errorElement)
            return apiError
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

            else -> NetworkError(
                type = NetworkErrorType.Unknown,
                message = throwable.localizedMessage ?: "Unknown exception",
                code = NetworkErrorType.Unknown.ordinal
            )
        }
    }
}