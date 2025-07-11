package com.music.dzr.core.network.serialization

import com.music.dzr.core.network.model.user.TimeRange
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class TimeRangeSerializerTest {
    private val json = Json { ignoreUnknownKeys = true }

    @Test
    fun deserializes_longTerm() {
        val input = "\"long_term\""
        val result = json.decodeFromString<TimeRange>(input)
        assertEquals(TimeRange.LongTerm, result)
    }

    @Test
    fun deserializes_mediumTerm() {
        val input = "\"medium_term\""
        val result = json.decodeFromString<TimeRange>(input)
        assertEquals(TimeRange.MediumTerm, result)
    }

    @Test
    fun deserializes_shortTerm() {
        val input = "\"short_term\""
        val result = json.decodeFromString<TimeRange>(input)
        assertEquals(TimeRange.ShortTerm, result)
    }

    @Test
    fun throwsException_whenGotUnknown() {
        val input = "\"unknown\""
        assertFailsWith<SerializationException> {
            json.decodeFromString<TimeRange>(input)
        }
    }

    @Test
    fun serializes_longTerm() {
        val output = json.encodeToString(TimeRange.LongTerm)
        assertEquals("\"long_term\"", output)
    }

    @Test
    fun serializes_mediumTerm() {
        val output = json.encodeToString(TimeRange.MediumTerm)
        assertEquals("\"medium_term\"", output)
    }

    @Test
    fun serializes_shortTerm() {
        val output = json.encodeToString(TimeRange.ShortTerm)
        assertEquals("\"short_term\"", output)
    }
}