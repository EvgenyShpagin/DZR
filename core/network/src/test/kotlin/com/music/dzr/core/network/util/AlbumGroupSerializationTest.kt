package com.music.dzr.core.network.util

import com.music.dzr.core.network.model.AlbumGroup
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Test

class AlbumGroupSerializationTest {

    private val json = Json { ignoreUnknownKeys = true }

    @Test
    fun deserializes_album() {
        val input = "\"album\""
        val result = json.decodeFromString<AlbumGroup>(input)
        assertEquals(AlbumGroup.Album, result)
    }

    @Test
    fun deserializes_single() {
        val input = "\"single\""
        val result = json.decodeFromString<AlbumGroup>(input)
        assertEquals(AlbumGroup.Single, result)
    }

    @Test
    fun deserializes_compilation() {
        val input = "\"compilation\""
        val result = json.decodeFromString<AlbumGroup>(input)
        assertEquals(AlbumGroup.Compilation, result)
    }

    @Test
    fun deserializes_appearsOn() {
        val input = "\"appears_on\""
        val result = json.decodeFromString<AlbumGroup>(input)
        assertEquals(AlbumGroup.AppearsOn, result)
    }

    @Test
    fun serializes_album() {
        val output = json.encodeToString(AlbumGroup.Album)
        assertEquals("\"album\"", output)
    }

    @Test
    fun serializes_single() {
        val output = json.encodeToString(AlbumGroup.Single)
        assertEquals("\"single\"", output)
    }

    @Test
    fun serializes_compilation() {
        val output = json.encodeToString(AlbumGroup.Compilation)
        assertEquals("\"compilation\"", output)
    }

    @Test
    fun serializes_appearsOn() {
        val output = json.encodeToString(AlbumGroup.AppearsOn)
        assertEquals("\"appears_on\"", output)
    }
}
