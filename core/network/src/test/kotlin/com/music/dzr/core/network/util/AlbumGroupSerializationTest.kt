package com.music.dzr.core.network.util

import com.music.dzr.core.network.model.AlbumGroup
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Test

class AlbumGroupSerializationTest {

    private val json = Json { ignoreUnknownKeys = true }

    @Test
    fun albumGroupSerializer_deserializes_whenGotAlbum() {
        val input = "\"album\""
        val result = json.decodeFromString<AlbumGroup>(input)
        assertEquals(AlbumGroup.Album, result)
    }

    @Test
    fun albumGroupSerializer_deserializes_whenGotSingle() {
        val input = "\"single\""
        val result = json.decodeFromString<AlbumGroup>(input)
        assertEquals(AlbumGroup.Single, result)
    }

    @Test
    fun albumGroupSerializer_deserializes_whenGotCompilation() {
        val input = "\"compilation\""
        val result = json.decodeFromString<AlbumGroup>(input)
        assertEquals(AlbumGroup.Compilation, result)
    }

    @Test
    fun albumGroupSerializer_deserializes_whenGotAppearsOn() {
        val input = "\"appears_on\""
        val result = json.decodeFromString<AlbumGroup>(input)
        assertEquals(AlbumGroup.AppearsOn, result)
    }

    @Test
    fun albumGroupSerializer_serializes_whenAlbum() {
        val output = json.encodeToString(AlbumGroup.Album)
        assertEquals("\"album\"", output)
    }

    @Test
    fun albumGroupSerializer_serializes_whenSingle() {
        val output = json.encodeToString(AlbumGroup.Single)
        assertEquals("\"single\"", output)
    }

    @Test
    fun albumGroupSerializer_serializes_whenCompilation() {
        val output = json.encodeToString(AlbumGroup.Compilation)
        assertEquals("\"compilation\"", output)
    }

    @Test
    fun albumGroupSerializer_serializes_whenAppearsOn() {
        val output = json.encodeToString(AlbumGroup.AppearsOn)
        assertEquals("\"appears_on\"", output)
    }
}
