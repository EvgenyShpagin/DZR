package com.music.dzr.library.player.data.remote.serialization

import com.music.dzr.library.player.data.remote.dto.Offset
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals

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