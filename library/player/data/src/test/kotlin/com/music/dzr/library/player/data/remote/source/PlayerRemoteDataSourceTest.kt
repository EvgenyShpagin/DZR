package com.music.dzr.library.player.data.remote.source

import com.music.dzr.core.network.dto.NetworkResponse
import com.music.dzr.library.player.data.remote.api.PlayerApi
import com.music.dzr.library.player.data.remote.dto.PlaybackOptions
import com.music.dzr.library.player.data.remote.dto.PlaybackState
import com.music.dzr.library.player.data.remote.dto.RepeatMode
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertSame

internal class PlayerRemoteDataSourceTest {

    private lateinit var api: PlayerApi
    private lateinit var dataSource: PlayerRemoteDataSource

    @BeforeTest
    fun setUp() {
        api = mockk(relaxed = true)
        dataSource = PlayerRemoteDataSource(api)
    }

    @Test
    fun getPlaybackState_delegatesCall() = runTest {
        val market = "US"
        val expected = NetworkResponse(data = mockk<PlaybackState>())
        coEvery { api.getPlaybackState(market) } returns expected

        val actual = dataSource.getPlaybackState(market)

        assertSame(expected, actual)
        coVerify(exactly = 1) { api.getPlaybackState(market) }
    }

    @Test
    fun transferPlayback_buildsMap_andDelegates() = runTest {
        val deviceId = "test_device"
        val play = true
        val expected = NetworkResponse(data = Unit)
        val expectedDeviceIdsMap = mapOf("device_ids" to listOf(deviceId))
        coEvery { api.transferPlayback(expectedDeviceIdsMap, play) } returns expected

        val actual = dataSource.transferPlayback(deviceId, play)

        assertSame(expected, actual)
        coVerify(exactly = 1) { api.transferPlayback(expectedDeviceIdsMap, play) }
    }

    @Test
    fun startOrResumePlayback_delegatesCall() = runTest {
        val deviceId = "test_device"
        val options: PlaybackOptions? = null
        val expected = NetworkResponse(data = Unit)
        coEvery { api.startOrResumePlayback(deviceId, options) } returns expected

        val actual = dataSource.startOrResumePlayback(deviceId, options)

        assertSame(expected, actual)
        coVerify(exactly = 1) { api.startOrResumePlayback(deviceId, options) }
    }

    @Test
    fun setRepeatMode_delegatesCall() = runTest {
        val state = RepeatMode.Track
        val deviceId = "test_device"
        val expected = NetworkResponse(data = Unit)
        coEvery { api.setRepeatMode(state, deviceId) } returns expected

        val actual = dataSource.setRepeatMode(state, deviceId)

        assertSame(expected, actual)
        coVerify(exactly = 1) { api.setRepeatMode(state, deviceId) }
    }

    @Test
    fun toggleShuffle_delegatesCall() = runTest {
        val state = true
        val deviceId = "test_device"
        val expected = NetworkResponse(data = Unit)
        coEvery { api.toggleShuffle(state, deviceId) } returns expected

        val actual = dataSource.toggleShuffle(state, deviceId)

        assertSame(expected, actual)
        coVerify(exactly = 1) { api.toggleShuffle(state, deviceId) }
    }
}