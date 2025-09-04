package com.music.dzr.library.artist.domain.model

import com.music.dzr.core.model.Album
import com.music.dzr.core.model.AlbumAvailability
import com.music.dzr.core.model.Image
import com.music.dzr.core.model.ReleaseDate
import com.music.dzr.core.model.ReleaseType
import com.music.dzr.core.model.SimplifiedArtist

/**
 * Album in artist's discography.
 */
data class AlbumInDiscography(
    override val id: String,
    override val availability: AlbumAvailability,
    override val name: String,
    override val releaseType: ReleaseType,
    override val totalTracks: Int,
    override val images: List<Image>,
    override val releaseDate: ReleaseDate,
    override val externalUrl: String,
    override val artists: List<SimplifiedArtist>,
    val justAppearsOn: Boolean
) : Album()