package com.music.dzr.core.network.util

import com.music.dzr.core.network.model.RepeatMode
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Test

class RepeatModeSerializationTest {

    private val json = Json { ignoreUnknownKeys = true }

    @Test
    fun repeatModeSerializer_deserializes_whenGotTrack() {
        val input = "\"track\""
        val result = json.decodeFromString<RepeatMode>(input)
        assertEquals(RepeatMode.Track, result)
    }

    @Test
    fun repeatModeSerializer_deserializes_whenGotContext() {
        val input = "\"context\""
        val result = json.decodeFromString<RepeatMode>(input)
        assertEquals(RepeatMode.Context, result)
    }

    @Test
    fun repeatModeSerializer_deserializes_whenGotOff() {
        val input = "\"off\""
        val result = json.decodeFromString<RepeatMode>(input)
        assertEquals(RepeatMode.Off, result)
    }

    @Test
    fun repeatModeSerializer_serializes_whenTrack() {
        val output = json.encodeToString(RepeatMode.Track)
        assertEquals("\"track\"", output)
    }

    @Test
    fun repeatModeSerializer_serializes_whenContext() {
        val output = json.encodeToString(RepeatMode.Context)
        assertEquals("\"context\"", output)
    }

    @Test
    fun repeatModeSerializer_serializes_whenOff() {
        val output = json.encodeToString(RepeatMode.Off)
        assertEquals("\"off\"", output)
    }
} 