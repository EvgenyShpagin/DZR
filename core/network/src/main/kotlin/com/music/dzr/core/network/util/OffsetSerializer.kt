package com.music.dzr.core.network.util

import com.music.dzr.core.network.model.Offset
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject

/**
 * A custom serializer for [Offset].
 */
object OffsetSerializer : JsonContentPolymorphicSerializer<Offset>(Offset::class) {
    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<Offset> {
        val jsonObject = element.jsonObject
        return when {
            "position" in jsonObject -> Offset.ByPosition.serializer()
            "uri" in jsonObject -> Offset.ByUri.serializer()
            else -> throwDeserializationException(jsonObject.toString())
        }
    }
}