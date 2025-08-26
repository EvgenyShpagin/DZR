package com.music.dzr.library.player.data.mapper

import com.music.dzr.core.data.mapper.toDomain
import com.music.dzr.core.model.AudioContent
import kotlin.time.Duration.Companion.milliseconds
import com.music.dzr.library.player.data.remote.dto.Actions as NetworkPlaybackActions
import com.music.dzr.library.player.data.remote.dto.Context as NetworkPlaybackContext
import com.music.dzr.library.player.data.remote.dto.PlaybackState as NetworkPlaybackState
import com.music.dzr.player.domain.model.PlaybackActions as DomainPlaybackActions
import com.music.dzr.player.domain.model.PlaybackContext as DomainPlaybackContext
import com.music.dzr.player.domain.model.PlaybackContextType as DomainPlaybackContextType
import com.music.dzr.player.domain.model.PlaybackState as DomainPlaybackState
import com.music.dzr.player.domain.model.RepeatMode as DomainRepeatMode


internal fun NetworkPlaybackState.toDomain(): DomainPlaybackState {
    return DomainPlaybackState(
        device = device.toDomain(),
        context = (context?.toDomain() ?: DomainPlaybackContext(
            id = "unknown",
            type = DomainPlaybackContextType.Unknown,
            externalUrl = ""
        )),
        repeatMode = DomainRepeatMode.fromNetwork(repeatState),
        isShuffling = shuffleState,
        isPlaying = isPlaying,
        progress = progressMs?.toLong()?.milliseconds,
        playingItem = item?.toDomain() ?: AudioContent.Unknown,
        actions = actions.toDomain()
    )
}

internal fun NetworkPlaybackContext.toDomain(): DomainPlaybackContext {
    val (type, id) = parseUriAndType(uri, type)
    return DomainPlaybackContext(
        id = id,
        type = type,
        externalUrl = externalUrls.spotify
    )
}

private fun NetworkPlaybackActions.toDomain(): DomainPlaybackActions {
    return DomainPlaybackActions(
        pausing = pausing == true,
        resuming = resuming == true,
        seeking = seeking == true,
        skippingNext = skippingNext == true,
        skippingPrev = skippingPrev == true,
        togglingRepeatContext = togglingRepeatContext == true,
        togglingShuffle = togglingShuffle == true,
        togglingRepeatTrack = togglingRepeatTrack == true,
        transferringPlayback = transferringPlayback == true
    )
}

private fun parseUriAndType(
    uri: String,
    typeString: String
): Pair<DomainPlaybackContextType, String> {
    val type = when (typeString.lowercase()) {
        "album" -> DomainPlaybackContextType.Album
        "playlist" -> DomainPlaybackContextType.Playlist
        "artist" -> DomainPlaybackContextType.Artist
        else -> DomainPlaybackContextType.Unknown
    }
    val id = uri.substringAfterLast(':', missingDelimiterValue = uri)
    return type to id
}
