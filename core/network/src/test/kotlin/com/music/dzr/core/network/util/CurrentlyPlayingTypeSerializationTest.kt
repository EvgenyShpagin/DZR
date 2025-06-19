package com.music.dzr.core.network.util

import com.music.dzr.core.network.model.CurrentlyPlayingType
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Test

class CurrentlyPlayingTypeSerializationTest {
    private val json = Json

    @Test
    fun `deserializes track`() {
        val input = "\"track\""
        val result = json.decodeFromString<CurrentlyPlayingType>(input)
        assertEquals(CurrentlyPlayingType.Track, result)
    }

    @Test
    fun `serializes track`() {
        val input = CurrentlyPlayingType.Track
        val result = json.encodeToString(input)
        assertEquals("\"track\"", result)
    }

    @Test
    fun `deserializes episode`() {
        val input = "\"episode\""
        val result = json.decodeFromString<CurrentlyPlayingType>(input)
        assertEquals(CurrentlyPlayingType.Episode, result)
    }

    @Test
    fun `serializes episode`() {
        val input = CurrentlyPlayingType.Episode
        val result = json.encodeToString(input)
        assertEquals("\"episode\"", result)
    }

    @Test
    fun `deserializes ad`() {
        val input = "\"ad\""
        val result = json.decodeFromString<CurrentlyPlayingType>(input)
        assertEquals(CurrentlyPlayingType.Ad, result)
    }

    @Test
    fun `serializes ad`() {
        val input = CurrentlyPlayingType.Ad
        val result = json.encodeToString(input)
        assertEquals("\"ad\"", result)
    }

    @Test
    fun `deserializes unknown`() {
        val input = "\"unknown\""
        val result = json.decodeFromString<CurrentlyPlayingType>(input)
        assertEquals(CurrentlyPlayingType.Unknown, result)
    }

    @Test
    fun `serializes unknown`() {
        val input = CurrentlyPlayingType.Unknown
        val result = json.encodeToString(input)
        assertEquals("\"unknown\"", result)
    }
} 