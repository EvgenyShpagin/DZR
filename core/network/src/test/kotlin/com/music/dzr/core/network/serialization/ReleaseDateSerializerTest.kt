package com.music.dzr.core.network.serialization

import com.music.dzr.core.network.dto.ReleaseDate
import com.music.dzr.core.network.dto.ReleaseDatePrecision
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class ReleaseDateSerializersTest {

    private val json = Json {
        // ensure we ignore unknown keys when using in broader context
        ignoreUnknownKeys = true
    }

    @Test
    fun deserializes_yearMonthAndDay() {
        val input = "\"1981-12-05\""
        val result = json.decodeFromString(ReleaseDateSerializer, input)
        assertEquals(1981, result.year)
        assertEquals(12, result.month)
        assertEquals(5, result.day)
    }

    @Test
    fun deserializes_yearAndMonth() {
        val input = "\"2000-07\""
        val result = json.decodeFromString(ReleaseDateSerializer, input)
        assertEquals(2000, result.year)
        assertEquals(7, result.month)
        assertNull(result.day)
    }

    @Test
    fun deserializes_year() {
        val input = "\"1999\""
        val result = json.decodeFromString(ReleaseDateSerializer, input)
        assertEquals(1999, result.year)
        assertNull(result.month)
        assertNull(result.day)
    }

    @Test
    fun serializes_yearMonthAndDay() {
        val value = ReleaseDate(year = 2021, month = 4, day = 9)
        val output = json.encodeToString(ReleaseDateSerializer, value)
        assertEquals("\"2021-4-9\"", output)
    }

    @Test
    fun serializes_yearAndMonth() {
        val value = ReleaseDate(year = 2021, month = 11, day = null)
        val output = json.encodeToString(ReleaseDateSerializer, value)
        assertEquals("\"2021-11\"", output)
    }

    @Test
    fun serializes_year() {
        val value = ReleaseDate(year = 2021, month = null, day = null)
        val output = json.encodeToString(ReleaseDateSerializer, value)
        assertEquals("\"2021\"", output)
    }

    @Test(expected = IllegalArgumentException::class)
    fun throwsException_onInvalidFormat() {
        val input = "\"20A1-13-99\""
        json.decodeFromString(ReleaseDateSerializer, input)
    }

    @Test
    fun deserializes_dayPrecision() {
        val input = "\"day\""
        val result = json.decodeFromString<ReleaseDatePrecision>(input)
        assertEquals(ReleaseDatePrecision.DAY, result)
    }

    @Test
    fun deserializes_monthPrecision() {
        val input = "\"month\""
        val result = json.decodeFromString<ReleaseDatePrecision>(input)
        assertEquals(ReleaseDatePrecision.MONTH, result)
    }

    @Test
    fun deserializes_yearPrecision() {
        val input = "\"year\""
        val result = json.decodeFromString<ReleaseDatePrecision>(input)
        assertEquals(ReleaseDatePrecision.YEAR, result)
    }

    @Test
    fun serializes_dayPrecision() {
        val output = json.encodeToString<ReleaseDatePrecision>(ReleaseDatePrecision.DAY)
        assertEquals("\"day\"", output)
    }

    @Test
    fun serializes_monthPrecision() {
        val output = json.encodeToString(ReleaseDatePrecision.MONTH)
        assertEquals("\"month\"", output)
    }

    @Test
    fun serializes_yearPrecision() {
        val output = json.encodeToString(ReleaseDatePrecision.YEAR)
        assertEquals("\"year\"", output)
    }
}