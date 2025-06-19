package com.music.dzr.core.network.util

import com.music.dzr.core.network.model.AlbumType
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Test

class AlbumTypeSerializationTest {

    private val json = Json { ignoreUnknownKeys = true }

    @Test
    fun deserializes_album() {
        val input = "\"album\""
        val result = json.decodeFromString<AlbumType>(input)
        assertEquals(AlbumType.Album, result)
    }

    @Test
    fun deserializes_single() {
        val input = "\"single\""
        val result = json.decodeFromString<AlbumType>(input)
        assertEquals(AlbumType.Single, result)
    }

    @Test
    fun deserializes_compilation() {
        val input = "\"compilation\""
        val result = json.decodeFromString<AlbumType>(input)
        assertEquals(AlbumType.Compilation, result)
    }

    @Test
    fun serializes_album() {
        val output = json.encodeToString(AlbumType.Album)
        assertEquals("\"album\"", output)
    }

    @Test
    fun serializes_single() {
        val output = json.encodeToString(AlbumType.Single)
        assertEquals("\"single\"", output)
    }

    @Test
    fun serializes_compilation() {
        val output = json.encodeToString(AlbumType.Compilation)
        assertEquals("\"compilation\"", output)
    }
}
