package com.music.dzr.core.network.retrofit

import com.music.dzr.core.network.model.error.NetworkError
import com.music.dzr.core.network.model.shared.NetworkResponse
import okhttp3.Request
import okio.Timeout
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

/**
 * A Retrofit [CallAdapter.Factory] that transforms a Retrofit [Response] into a [NetworkResponse].
 * It uses [NetworkErrorResponseParser] to parse errors into a consistent [NetworkError] model.
 */
internal class NetworkResponseCallAdapterFactory(
    private val errorParser: NetworkErrorResponseParser
) : CallAdapter.Factory() {

    override fun get(
        returnType: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit
    ): CallAdapter<*, *>? {
        if (getRawType(returnType) != Call::class.java) return null

        // Extract T of Call<T>
        val callType = getParameterUpperBound(0, returnType as ParameterizedType)

        if (getRawType(callType) != NetworkResponse::class.java) return null

        // Extract T of NetworkResponse<T>
        val responseType = getParameterUpperBound(0, callType as ParameterizedType)

        return NetworkResponseCallAdapter<Any>(errorParser, responseType)
    }
}

/**
 * [CallAdapter] that converts a Retrofit [Call] into a [Call] of [NetworkResponse].
 * It ensures that the success and error cases are wrapped in a sealed [NetworkResponse] class.
 */
private class NetworkResponseCallAdapter<T>(
    private val errorParser: NetworkErrorResponseParser,
    private val responseType: Type
) : CallAdapter<T, Call<NetworkResponse<T>>> {

    override fun responseType(): Type = responseType
    override fun adapt(call: Call<T>) = NetworkResponseCall(errorParser, call)
}

/**
 * Custom [Call] implementation that intercepts Retrofit's success and error responses
 * and wraps them into [NetworkResponse], using [errorParser] to extract API errors.
 */
private class NetworkResponseCall<T>(
    private val errorParser: NetworkErrorResponseParser,
    private val delegate: Call<T>
) : Call<NetworkResponse<T>> {

    override fun enqueue(callback: Callback<NetworkResponse<T>>) {

        delegate.enqueue(object : Callback<T> {
            override fun onResponse(call: Call<T>, response: Response<T>) {
                val apiResponse = response.toNetworkResponse()
                callback.onResponse(this@NetworkResponseCall, Response.success(apiResponse))
            }

            override fun onFailure(call: Call<T>, throwable: Throwable) {
                val networkResponse = NetworkResponse<T>(
                    error = errorParser.parse(throwable)
                )
                callback.onResponse(this@NetworkResponseCall, Response.success(networkResponse))
            }
        })
    }

    override fun execute(): Response<NetworkResponse<T>> {
        val networkResponse = try {
            delegate.execute().toNetworkResponse()
        } catch (exception: Exception) {
            NetworkResponse(error = errorParser.parse(exception))
        }
        return Response.success(networkResponse)
    }

    private fun Response<T>.toNetworkResponse(): NetworkResponse<T> {
        return if (isSuccessful) {
            // Return Unit on 'No Content' response instead of null
            @Suppress("UNCHECKED_CAST")
            NetworkResponse(data = if (code() == 204) Unit as? T else body())
        } else {
            NetworkResponse(error = errorParser.parse(errorBody()!!))
        }
    }

    override fun isExecuted() = delegate.isExecuted
    override fun cancel() = delegate.cancel()
    override fun clone() = NetworkResponseCall(errorParser, delegate.clone())
    override fun isCanceled() = delegate.isCanceled
    override fun request(): Request = delegate.request()
    override fun timeout(): Timeout = delegate.timeout()
}
