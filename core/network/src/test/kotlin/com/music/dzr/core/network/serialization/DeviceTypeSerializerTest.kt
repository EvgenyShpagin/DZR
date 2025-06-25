package com.music.dzr.core.network.serialization

import com.music.dzr.core.network.model.player.DeviceType
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Test

class DeviceTypeSerializerTest {
    private val json = Json

    @Test
    fun `deserializes and serializes all device types`() {
        val deviceTypeMap = mapOf(
            DeviceType.Computer to "\"computer\"",
            DeviceType.Tablet to "\"tablet\"",
            DeviceType.Smartphone to "\"smartphone\"",
            DeviceType.Speaker to "\"speaker\"",
            DeviceType.Tv to "\"tv\"",
            DeviceType.Avr to "\"avr\"",
            DeviceType.Stb to "\"stb\"",
            DeviceType.AudioDongle to "\"audio_dongle\"",
            DeviceType.GameConsole to "\"game_console\"",
            DeviceType.CastVideo to "\"cast_video\"",
            DeviceType.CastAudio to "\"cast_audio\"",
            DeviceType.Automobile to "\"automobile\"",
            DeviceType.Unknown to "\"unknown\"",
        )

        deviceTypeMap.forEach { (type, jsonValue) ->
            // Test deserialization
            assertEquals(type, json.decodeFromString(DeviceTypeSerializer, jsonValue))
            // Test serialization
            assertEquals(jsonValue, json.encodeToString(DeviceTypeSerializer, type))
        }
    }

    @Test
    fun `deserializes other to unknown`() {
        val input = "\"something_else\""
        val result = json.decodeFromString(DeviceTypeSerializer, input)
        assertEquals(DeviceType.Unknown, result)
    }
} 