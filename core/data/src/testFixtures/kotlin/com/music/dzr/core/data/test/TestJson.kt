package com.music.dzr.core.data.test

import kotlinx.serialization.json.Json

/**
 * Test-only JSON serializer with lenient settings.
 */
val TestJson = Json { ignoreUnknownKeys = true }