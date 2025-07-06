package com.music.dzr.core.network.serialization

import com.music.dzr.core.network.model.player.RepeatMode
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals

class RepeatModeSerializationTest {

    private val json = Json { ignoreUnknownKeys = true }

    @Test
    fun deserializes_track() {
        val input = "\"track\""
        val result = json.decodeFromString<RepeatMode>(input)
        assertEquals(RepeatMode.Track, result)
    }

    @Test
    fun deserializes_context() {
        val input = "\"context\""
        val result = json.decodeFromString<RepeatMode>(input)
        assertEquals(RepeatMode.Context, result)
    }

    @Test
    fun deserializes_off() {
        val input = "\"off\""
        val result = json.decodeFromString<RepeatMode>(input)
        assertEquals(RepeatMode.Off, result)
    }

    @Test
    fun serializes_track() {
        val output = json.encodeToString(RepeatMode.Track)
        assertEquals("\"track\"", output)
    }

    @Test
    fun serializes_context() {
        val output = json.encodeToString(RepeatMode.Context)
        assertEquals("\"context\"", output)
    }

    @Test
    fun serializes_off() {
        val output = json.encodeToString(RepeatMode.Off)
        assertEquals("\"off\"", output)
    }
} 