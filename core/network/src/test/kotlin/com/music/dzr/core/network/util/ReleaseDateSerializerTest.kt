package com.music.dzr.core.network.util

import com.music.dzr.core.network.model.ReleaseDate
import com.music.dzr.core.network.model.ReleaseDatePrecision
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class ReleaseDateSerializersTest {

    private val json = Json {
        // ensure we ignore unknown keys when using in broader context
        ignoreUnknownKeys = true
    }

    @Test
    fun releaseDateSerializer_deserializes_whenGotYearMonthAndDay() {
        val input = "\"1981-12-05\""
        val result = json.decodeFromString(ReleaseDateSerializer, input)
        assertEquals(1981, result.year)
        assertEquals(12, result.month)
        assertEquals(5, result.day)
    }

    @Test
    fun releaseDateSerializer_deserializes_whenGotYearAndMonth() {
        val input = "\"2000-07\""
        val result = json.decodeFromString(ReleaseDateSerializer, input)
        assertEquals(2000, result.year)
        assertEquals(7, result.month)
        assertNull(result.day)
    }

    @Test
    fun releaseDateSerializer_deserializes_whenGotOnlyYear() {
        val input = "\"1999\""
        val result = json.decodeFromString(ReleaseDateSerializer, input)
        assertEquals(1999, result.year)
        assertNull(result.month)
        assertNull(result.day)
    }

    @Test
    fun releaseDateSerializer_serializes_whenGotYearMonthAndDay() {
        val value = ReleaseDate(year = 2021, month = 4, day = 9)
        val output = json.encodeToString(ReleaseDateSerializer, value)
        assertEquals("\"2021-4-9\"", output)
    }

    @Test
    fun releaseDateSerializer_serializes_whenGotYearAndMonth() {
        val value = ReleaseDate(year = 2021, month = 11, day = null)
        val output = json.encodeToString(ReleaseDateSerializer, value)
        assertEquals("\"2021-11\"", output)
    }

    @Test
    fun releaseDateSerializer_serializes_whenGotOnlyYear() {
        val value = ReleaseDate(year = 2021, month = null, day = null)
        val output = json.encodeToString(ReleaseDateSerializer, value)
        assertEquals("\"2021\"", output)
    }

    @Test(expected = IllegalArgumentException::class)
    fun releaseDateSerializer_throwsException_whenInvalidFormatGot() {
        val input = "\"20A1-13-99\""
        json.decodeFromString(ReleaseDateSerializer, input)
    }

    @Test
    fun releaseDatePrecisionSerializer_deserializes_whenGotDay() {
        val input = "\"day\""
        val result = json.decodeFromString(ReleaseDatePrecisionSerializer, input)
        assertEquals(ReleaseDatePrecision.DAY, result)
    }

    @Test
    fun releaseDatePrecisionSerializer_deserializes_whenGotMonth() {
        val input = "\"month\""
        val result = json.decodeFromString(ReleaseDatePrecisionSerializer, input)
        assertEquals(ReleaseDatePrecision.MONTH, result)
    }

    @Test
    fun releaseDatePrecisionSerializer_deserializes_whenGotYear() {
        val input = "\"year\""
        val result = json.decodeFromString(ReleaseDatePrecisionSerializer, input)
        assertEquals(ReleaseDatePrecision.YEAR, result)
    }

    @Test
    fun releaseDatePrecisionSerializer_serializes_whenGotDay() {
        val output = json.encodeToString(ReleaseDatePrecisionSerializer, ReleaseDatePrecision.DAY)
        assertEquals("\"day\"", output)
    }

    @Test
    fun releaseDatePrecisionSerializer_serializes_whenGotMonth() {
        val output = json.encodeToString(ReleaseDatePrecisionSerializer, ReleaseDatePrecision.MONTH)
        assertEquals("\"month\"", output)
    }

    @Test
    fun releaseDatePrecisionSerializer_serializes_whenGotYear() {
        val output = json.encodeToString(ReleaseDatePrecisionSerializer, ReleaseDatePrecision.YEAR)
        assertEquals("\"year\"", output)
    }
}