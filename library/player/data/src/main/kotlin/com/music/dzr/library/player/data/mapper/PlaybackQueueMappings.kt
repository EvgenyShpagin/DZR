package com.music.dzr.library.player.data.mapper

import com.music.dzr.core.data.mapper.toDomain
import com.music.dzr.library.player.data.remote.dto.Queue as NetworkQueue
import com.music.dzr.player.domain.model.PlaybackQueue as DomainPlaybackQueue

internal fun NetworkQueue.toDomain(): DomainPlaybackQueue {
    return DomainPlaybackQueue(
        currentlyPlaying = currentlyPlaying?.toDomain(),
        upcoming = queue.map { it.toDomain() }
    )
}
