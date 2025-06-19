package com.music.dzr.core.network.util

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.serializer

/**
 * Throws a [SerializationException] with a standardized error message.
 * This is a convenience method to be used within [KSerializer] implementations.
 *
 * @param rawValue The raw string value that could not be deserialized.
 */
@OptIn(ExperimentalSerializationApi::class)
internal fun KSerializer<*>.throwDeserializationException(rawValue: String): Nothing {
    throw SerializationException(
        "Couldn't deserialize '$rawValue' to type ${descriptor.serialName}"
    )
}

/**
 * An extension function for enums to get their serialized name based on the @SerialName annotation.
 * This provides a reusable and type-safe way to avoid string duplication.
 */
@OptIn(ExperimentalSerializationApi::class)
internal inline fun <reified T : Enum<T>> Enum<T>.getSerializedName(): String {
    return serializer<T>().descriptor.getElementName(ordinal)
}