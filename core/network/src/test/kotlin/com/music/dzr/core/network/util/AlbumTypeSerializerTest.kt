package com.music.dzr.core.network.util

import com.music.dzr.core.network.model.AlbumType
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Test

class AlbumTypeSerializerTest {

    private val json = Json { ignoreUnknownKeys = true }

    @Test
    fun albumTypeSerializer_deserializes_whenGotAlbum() {
        val input = "\"album\""
        val result = json.decodeFromString(AlbumTypeSerializer, input)
        assertEquals(AlbumType.Album, result)
    }

    @Test
    fun albumTypeSerializer_deserializes_whenGotSingle() {
        val input = "\"single\""
        val result = json.decodeFromString(AlbumTypeSerializer, input)
        assertEquals(AlbumType.Single, result)
    }

    @Test
    fun albumTypeSerializer_deserializes_whenGotCompilation() {
        val input = "\"compilation\""
        val result = json.decodeFromString(AlbumTypeSerializer, input)
        assertEquals(AlbumType.Compilation, result)
    }

    @Test
    fun albumTypeSerializer_deserializes_whenGotUnknown() {
        val input = "\"ep\""
        val result = json.decodeFromString(AlbumTypeSerializer, input)
        assertEquals(AlbumType.Compilation, result)
    }

    @Test
    fun albumTypeSerializer_serializes_whenAlbum() {
        val output = json.encodeToString(AlbumTypeSerializer, AlbumType.Album)
        assertEquals("\"album\"", output)
    }

    @Test
    fun albumTypeSerializer_serializes_whenSingle() {
        val output = json.encodeToString(AlbumTypeSerializer, AlbumType.Single)
        assertEquals("\"single\"", output)
    }

    @Test
    fun albumTypeSerializer_serializes_whenCompilation() {
        val output = json.encodeToString(AlbumTypeSerializer, AlbumType.Compilation)
        assertEquals("\"compilation\"", output)
    }
}
