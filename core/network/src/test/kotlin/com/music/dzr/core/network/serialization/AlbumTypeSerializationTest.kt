package com.music.dzr.core.network.serialization

import com.music.dzr.core.network.dto.AlbumType
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals

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
