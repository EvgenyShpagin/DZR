package com.music.dzr.library.user.data.remote.serialization

import com.music.dzr.library.user.data.remote.dto.SubscriptionLevel
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals

class SubscriptionLevelSerializationTest {

    @Test
    fun serializesAndDeserializes_premium() {
        val premium = SubscriptionLevel.Premium
        val premiumJson = "\"premium\""
        assertEquals(premiumJson, Json.Default.encodeToString(premium))
        assertEquals(premium, Json.Default.decodeFromString<SubscriptionLevel>(premiumJson))
    }

    @Test
    fun serializesAndDeserializes_free() {
        val free = SubscriptionLevel.Free
        val freeJson = "\"free\""
        assertEquals(freeJson, Json.Default.encodeToString(free))
        assertEquals(free, Json.Default.decodeFromString<SubscriptionLevel>(freeJson))
    }

    @Test
    fun serializesAndDeserializes_open() {
        val open = SubscriptionLevel.Open
        val openJson = "\"open\""
        assertEquals(openJson, Json.Default.encodeToString(open))
        assertEquals(open, Json.Default.decodeFromString<SubscriptionLevel>(openJson))
    }
}