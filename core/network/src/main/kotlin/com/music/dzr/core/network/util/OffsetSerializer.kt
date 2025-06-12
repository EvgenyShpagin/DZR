package com.music.dzr.core.network.util

import com.music.dzr.core.network.model.Offset
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonEncoder
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

/**
 * A custom serializer for [Offset].
 */
object OffsetSerializer : KSerializer<Offset> {
    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("Offset")

    override fun serialize(encoder: Encoder, value: Offset) {
        val jsonEncoder = encoder as? JsonEncoder
            ?: throw IllegalStateException("This serializer can be used only with Json")
        val jsonObject = when (value) {
            is Offset.ByPosition -> JsonObject(mapOf("position" to JsonPrimitive(value.position)))
            is Offset.ByUri -> JsonObject(mapOf("uri" to JsonPrimitive(value.uri)))
        }
        jsonEncoder.encodeJsonElement(jsonObject)
    }

    override fun deserialize(decoder: Decoder): Offset {
        val jsonDecoder = decoder as? JsonDecoder
            ?: throw IllegalStateException("This serializer can be used only with Json")
        val jsonObject = jsonDecoder.decodeJsonElement().jsonObject

        return when {
            "position" in jsonObject -> Offset.ByPosition(jsonObject["position"]!!.jsonPrimitive.int)
            "uri" in jsonObject -> Offset.ByUri(jsonObject["uri"]!!.jsonPrimitive.content)
            else -> throw IllegalStateException("Invalid Offset object")
        }
    }
} 