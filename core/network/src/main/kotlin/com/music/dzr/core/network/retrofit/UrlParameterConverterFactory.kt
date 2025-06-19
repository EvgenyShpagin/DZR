package com.music.dzr.core.network.retrofit

import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type

/**
 * A [Converter.Factory] for Retrofit that converts objects implementing the [UrlParameter]
 * interface into their string representation for URL parameters.
 *
 * This factory checks if a type used in an annotation ([retrofit2.http.Query],
 * [retrofit2.http.Path], [retrofit2.http.Header], [retrofit2.http.Field]) implements [UrlParameter].
 * If it does, it uses the value from the [UrlParameter.urlValue] property as the string representation.
 *
 * For any other type, it returns `null`, allowing other converter factories to handle them.
 */
class UrlParameterConverterFactory : Converter.Factory() {

    override fun stringConverter(
        type: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit
    ): Converter<*, String>? {
        return if (type is Class<*> && UrlParameter::class.java.isAssignableFrom(type)) {
            Converter<UrlParameter, String> { value -> value.urlValue }
        } else {
            null
        }
    }

}