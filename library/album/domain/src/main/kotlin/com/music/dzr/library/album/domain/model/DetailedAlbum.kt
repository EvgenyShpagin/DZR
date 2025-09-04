package com.music.dzr.library.album.domain.model

import com.music.dzr.core.model.Album
import com.music.dzr.core.model.AlbumAvailability
import com.music.dzr.core.model.Copyright
import com.music.dzr.core.model.ExternalIdentifiers
import com.music.dzr.core.model.Image
import com.music.dzr.core.model.MusicGenre
import com.music.dzr.core.model.PopularityLevel
import com.music.dzr.core.model.ReleaseDate
import com.music.dzr.core.model.ReleaseType
import com.music.dzr.core.model.SimplifiedArtist

/**
 * Album with full information including tracks.
 */
data class DetailedAlbum(
    override val id: String,
    override val availability: AlbumAvailability,
    override val name: String,
    override val releaseType: ReleaseType,
    override val totalTracks: Int,
    override val images: List<Image>,
    override val releaseDate: ReleaseDate,
    override val externalUrl: String,
    override val artists: List<SimplifiedArtist>,
    val tracks: List<TrackOnAlbum>,
    val copyrights: List<Copyright>,
    val externalIds: ExternalIdentifiers,
    val genres: List<MusicGenre>,
    val label: String,
    val popularity: PopularityLevel
) : Album()