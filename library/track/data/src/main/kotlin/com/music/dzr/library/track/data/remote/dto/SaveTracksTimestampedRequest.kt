package com.music.dzr.library.track.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Request body for saving tracks with explicit timestamps via the Spotify
 * "Save Tracks for Current User" endpoint.
 *
 * When "timestamped_ids" is present, the API ignores any "ids" provided in the query or body.
 *
 * @property timestampedIds List of track IDs with their corresponding timestamps.
 *
 * @see <a href="https://developer.spotify.com/documentation/web-api/reference/save-tracks-user">Save Tracks for User</a>
 */
@Serializable
internal data class SaveTracksTimestampedRequest(
    @SerialName("timestamped_ids")
    val timestampedIds: List<TimestampedId>
)
