package com.music.dzr.library.player.data.remote.source

import com.music.dzr.core.data.test.HasForcedError
import com.music.dzr.core.data.test.runUnlessForcedError
import com.music.dzr.core.data.test.toCursorPaginatedList
import com.music.dzr.core.network.dto.CursorPaginatedList
import com.music.dzr.core.network.dto.NetworkResponse
import com.music.dzr.core.network.dto.Track
import com.music.dzr.core.network.dto.error.NetworkError
import com.music.dzr.library.player.data.remote.dto.Actions
import com.music.dzr.library.player.data.remote.dto.CurrentlyPlayingContext
import com.music.dzr.library.player.data.remote.dto.CurrentlyPlayingType
import com.music.dzr.library.player.data.remote.dto.Device
import com.music.dzr.library.player.data.remote.dto.DeviceType
import com.music.dzr.library.player.data.remote.dto.Devices
import com.music.dzr.library.player.data.remote.dto.PlayHistory
import com.music.dzr.library.player.data.remote.dto.PlaybackOptions
import com.music.dzr.library.player.data.remote.dto.PlaybackState
import com.music.dzr.library.player.data.remote.dto.Queue
import com.music.dzr.library.player.data.remote.dto.RepeatMode
import kotlin.time.Clock
import kotlin.time.Instant

/**
 * In-memory Fake implementation of [PlayerRemoteDataSource].
 *
 * Mirrors the contract of the real remote source but keeps all state in memory so tests can
 * deterministically set up scenarios and observe effects without network.
 */
internal class FakePlayerRemoteDataSource : PlayerRemoteDataSource, HasForcedError<NetworkError> {

    override var forcedError: NetworkError? = null

    // In-memory state
    var devices: MutableList<Device> = mutableListOf(defaultDevice())
    var playbackState: PlaybackState = defaultPlaybackState(devices.first())
    var currentlyPlaying: CurrentlyPlayingContext = defaultCurrentlyPlaying()
    var queueTracks: MutableList<Track> = mutableListOf()
    var recentlyPlayed: MutableList<PlayHistory> = mutableListOf()

    override suspend fun getPlaybackState(
        market: String?
    ): NetworkResponse<PlaybackState> = runUnlessForcedError {
        playbackState.copy(timestamp = currentTimestamp())
    }

    override suspend fun transferPlayback(
        deviceId: String,
        play: Boolean
    ): NetworkResponse<Unit> = runUnlessForcedError {
        // Mark active device
        devices = devices.map { it.copy(isActive = it.id == deviceId) }.toMutableList()
        val active = devices.firstOrNull { it.isActive } ?: devices.first()
        // Move playback to the active device and set playing state
        playbackState = playbackState.copy(
            device = active,
            isPlaying = play,
            timestamp = currentTimestamp()
        )
        currentlyPlaying = currentlyPlaying.copy(
            isPlaying = play,
            timestamp = currentTimestamp()
        )
    }

    override suspend fun getAvailableDevices(): NetworkResponse<Devices> = runUnlessForcedError {
        Devices(list = devices.toList())
    }

    override suspend fun getCurrentlyPlayingTrack(
        market: String?
    ): NetworkResponse<CurrentlyPlayingContext> = runUnlessForcedError {
        currentlyPlaying.copy(timestamp = currentTimestamp())
    }

    override suspend fun startOrResumePlayback(
        deviceId: String?,
        options: PlaybackOptions?
    ): NetworkResponse<Unit> = runUnlessForcedError {
        // Optionally switch device
        if (deviceId != null) {
            devices = devices.map { it.copy(isActive = it.id == deviceId) }.toMutableList()
            playbackState = playbackState.copy(device = devices.firstOrDefaultActive())
        }
        // Apply options: we don't synthesize Tracks; we only update position and flags
        options?.positionMs?.let { pos ->
            playbackState = playbackState.copy(progressMs = pos)
            currentlyPlaying = currentlyPlaying.copy(progressMs = pos)
        }
        // Start/resume
        playbackState = playbackState.copy(isPlaying = true, timestamp = currentTimestamp())
        currentlyPlaying = currentlyPlaying.copy(isPlaying = true, timestamp = currentTimestamp())
    }

    override suspend fun pausePlayback(
        deviceId: String?
    ): NetworkResponse<Unit> = runUnlessForcedError {
        // Optionally ensure device context (no-op for position)
        deviceId?.let { id ->
            devices = devices.map { it.copy(isActive = it.id == id) }.toMutableList()
            playbackState = playbackState.copy(device = devices.firstOrDefaultActive())
        }
        playbackState = playbackState.copy(isPlaying = false, timestamp = currentTimestamp())
        currentlyPlaying = currentlyPlaying.copy(isPlaying = false, timestamp = currentTimestamp())
    }

    override suspend fun seekToPosition(
        positionMs: Int,
        deviceId: String?
    ): NetworkResponse<Unit> = runUnlessForcedError {
        deviceId?.let { id ->
            devices = devices.map { it.copy(isActive = it.id == id) }.toMutableList()
            playbackState = playbackState.copy(device = devices.firstOrDefaultActive())
        }
        playbackState = playbackState.copy(
            progressMs = positionMs,
            timestamp = currentTimestamp()
        )
        currentlyPlaying = currentlyPlaying.copy(
            progressMs = positionMs,
            timestamp = currentTimestamp()
        )
    }

    override suspend fun setRepeatMode(
        state: RepeatMode,
        deviceId: String?
    ): NetworkResponse<Unit> = runUnlessForcedError {
        deviceId?.let { id ->
            devices = devices.map { it.copy(isActive = it.id == id) }.toMutableList()
            playbackState = playbackState.copy(device = devices.firstOrDefaultActive())
        }

        playbackState = playbackState.copy(
            repeatState = state.toApiString(),
            timestamp = currentTimestamp()
        )
    }

    override suspend fun setPlaybackVolume(
        volumePercent: Int,
        deviceId: String?
    ): NetworkResponse<Unit> = runUnlessForcedError {
        val targetId = deviceId ?: devices.firstOrDefaultActive().id
        devices = devices.map {
            if (it.id == targetId) it.copy(volumePercent = volumePercent) else it
        }.toMutableList()
        playbackState = playbackState.copy(device = devices.firstOrDefaultActive())
    }

    override suspend fun toggleShuffle(
        state: Boolean,
        deviceId: String?
    ): NetworkResponse<Unit> = runUnlessForcedError {
        deviceId?.let { id ->
            devices = devices.map { it.copy(isActive = it.id == id) }.toMutableList()
            playbackState = playbackState.copy(device = devices.firstOrDefaultActive())
        }
        playbackState = playbackState.copy(
            shuffleState = state,
            timestamp = currentTimestamp()
        )
    }

    override suspend fun getRecentlyPlayedTracks(
        limit: Int?,
        after: Instant?,
        before: Instant?
    ): NetworkResponse<CursorPaginatedList<PlayHistory>> = runUnlessForcedError {
        // Simple in-memory filtering/slicing by timestamp
        val items = recentlyPlayed
            .sortedByDescending { it.playedAt }
            .filter {
                (after == null || it.playedAt > after) &&
                        (before == null || it.playedAt < before)
            }
            .let { list -> if (limit != null) list.take(limit) else list }
        items.toCursorPaginatedList()
    }

    override suspend fun getUserQueue(): NetworkResponse<Queue> = runUnlessForcedError {
        Queue(currentlyPlaying = currentlyPlaying.item, queue = queueTracks.toList())
    }

    override suspend fun addToQueue(
        uri: String,
        deviceId: String?
    ): NetworkResponse<Unit> = runUnlessForcedError {
        // This fake does not resolve URIs to Tracks; tests can directly mutate [queueTracks]
        // We still switch active device if requested
        deviceId?.let { id ->
            devices = devices.map { it.copy(isActive = it.id == id) }.toMutableList()
            playbackState = playbackState.copy(device = devices.firstOrDefaultActive())
        }
    }
}

private fun List<Device>.firstOrDefaultActive(): Device =
    this.firstOrNull { it.isActive } ?: this.first()

private fun RepeatMode.toApiString(): String = when (this) {
    RepeatMode.Track -> "track"
    RepeatMode.Context -> "context"
    RepeatMode.Off -> "off"
}

private fun currentTimestamp(): Long = Clock.System.now().toEpochMilliseconds()

private fun defaultDevice(): Device = Device(
    id = "fake-device",
    isActive = true,
    isPrivateSession = false,
    isRestricted = false,
    name = "Fake Device",
    type = DeviceType.Computer,
    volumePercent = 50,
    supportsVolume = true
)

private fun defaultActions(): Actions = Actions()

private fun defaultPlaybackState(device: Device): PlaybackState = PlaybackState(
    device = device,
    repeatState = "off",
    shuffleState = false,
    context = null,
    timestamp = currentTimestamp(),
    progressMs = 0,
    isPlaying = false,
    item = null,
    currentlyPlayingType = CurrentlyPlayingType.Unknown,
    actions = defaultActions()
)

private fun defaultCurrentlyPlaying(): CurrentlyPlayingContext = CurrentlyPlayingContext(
    context = null,
    timestamp = currentTimestamp(),
    progressMs = 0,
    isPlaying = false,
    item = null,
    currentlyPlayingType = CurrentlyPlayingType.Unknown,
    actions = defaultActions()
)
