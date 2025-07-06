package com.music.dzr.core.network.serialization

import com.music.dzr.core.network.model.user.SubscriptionLevel
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals

class SubscriptionLevelSerializationTest {

    @Test
    fun serializesAndDeserializes_premium() {
        val premium = SubscriptionLevel.Premium
        val premiumJson = "\"premium\""
        assertEquals(premiumJson, Json.encodeToString(premium))
        assertEquals(premium, Json.decodeFromString<SubscriptionLevel>(premiumJson))
    }

    @Test
    fun serializesAndDeserializes_free() {
        val free = SubscriptionLevel.Free
        val freeJson = "\"free\""
        assertEquals(freeJson, Json.encodeToString(free))
        assertEquals(free, Json.decodeFromString<SubscriptionLevel>(freeJson))
    }

    @Test
    fun serializesAndDeserializes_open() {
        val open = SubscriptionLevel.Open
        val openJson = "\"open\""
        assertEquals(openJson, Json.encodeToString(open))
        assertEquals(open, Json.decodeFromString<SubscriptionLevel>(openJson))
    }
} 