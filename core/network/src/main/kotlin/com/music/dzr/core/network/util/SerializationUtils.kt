package com.music.dzr.core.network.util

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException

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