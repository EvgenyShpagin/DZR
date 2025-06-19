package com.music.dzr.core.network.util

import com.music.dzr.core.network.model.Offset
import kotlinx.serialization.SerializationException
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Test

class OffsetSerializerTest {

    @Test
    fun serializesAndDeserializes_offsetByPosition() {
        val offset = Offset.ByPosition(5)
        val jsonString = Json.encodeToString(offset)
        val decodedOffset = Json.decodeFromString<Offset>(jsonString)
        assertEquals(offset, decodedOffset)
    }

    @Test
    fun serializesAndDeserializes_OffsetByUri() {
        val offset = Offset.ByUri("spotify:track:12345")
        val jsonString = Json.encodeToString(offset)
        val decodedOffset = Json.decodeFromString<Offset>(jsonString)
        assertEquals(offset, decodedOffset)
    }

    @Test(expected = SerializationException::class)
    fun throwsException_onInvalidJson() {
        val invalidJson = """{"invalid_key":"invalid_value"}"""
        Json.decodeFromString<Offset>(invalidJson)
    }
}