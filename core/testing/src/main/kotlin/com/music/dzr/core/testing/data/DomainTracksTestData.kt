package com.music.dzr.core.testing.data

import com.music.dzr.core.model.AlbumOnTrack
import com.music.dzr.core.model.Availability
import com.music.dzr.core.model.ExternalIdentifiers
import com.music.dzr.core.model.Image
import com.music.dzr.core.model.PopularityLevel
import com.music.dzr.core.model.ReleaseDate
import com.music.dzr.core.model.ReleaseType
import com.music.dzr.core.model.SimplifiedArtist
import kotlin.time.Duration
import com.music.dzr.core.model.DetailedTrack as DomainDetailedTrack

val domainDetailedTracksTestData: List<DomainDetailedTrack> = listOf(
    DomainDetailedTrack(
        id = "track_1",
        name = "Bohemian Rhapsody",
        artists = listOf(
            SimplifiedArtist(
                id = "artist_1",
                name = "Queen",
                externalUrl = "https://open.spotify.com/artist/1dfeR4HaWDbWqFHLkxsg1d"
            )
        ),
        duration = Duration.parse("5:55"),
        trackNumber = 1,
        discNumber = 1,
        isExplicit = false,
        availability = Availability.Available,
        externalUrl = "https://open.spotify.com/track/3z8h0TU7ReDPLIbEnYhWZb",
        album = AlbumOnTrack(
            id = "album_1",
            availability = Availability.Available,
            name = "A Night at the Opera",
            releaseType = ReleaseType.ALBUM,
            totalTracks = 12,
            images = listOf(
                Image(
                    url = "https://i.scdn.co/image/ab67616d0000b273ce4f1737e8c31a12bea4b2a3",
                    width = 640,
                    height = 640
                )
            ),
            releaseDate = ReleaseDate(year = 1975, month = 11, day = 21),
            externalUrl = "https://open.spotify.com/album/1GbtB4nxMcyF2VSqfAD0l8",
            artists = listOf(
                SimplifiedArtist(
                    id = "artist_1",
                    name = "Queen",
                    externalUrl = "https://open.spotify.com/artist/1dfeR4HaWDbWqFHLkxsg1d"
                )
            ),
        ),
        popularity = PopularityLevel.VERY_HIGH,
        externalIds = ExternalIdentifiers(
            isrc = "GBUM71029601",
            ean = "075678262526",
            upc = "75678262526"
        )
    ),

    DomainDetailedTrack(
        id = "track_2",
        availability = Availability.Available,
        name = "Hotel California",
        artists = listOf(
            SimplifiedArtist(
                id = "artist_2",
                name = "Eagles",
                externalUrl = "https://open.spotify.com/artist/0ECwFtbIWE7340Sf1q7ZmD"
            )
        ),
        duration = Duration.parse("6:30"),
        trackNumber = 1,
        discNumber = 1,
        isExplicit = false,
        externalUrl = "https://open.spotify.com/track/40riOyOyWxJjGm1hYqaqqq",
        album = AlbumOnTrack(
            id = "album_2",
            availability = Availability.Available,
            name = "Hotel California",
            releaseType = ReleaseType.ALBUM,
            totalTracks = 9,
            images = listOf(
                Image(
                    url = "https://i.scdn.co/image/ab67616d0000b273ce4f1737e8c31a12bea4b2a4",
                    width = 640,
                    height = 640
                )
            ),
            releaseDate = ReleaseDate(year = 1976, month = 12, day = 8),
            externalUrl = "https://open.spotify.com/album/2o7KjiY6AhGOwtQeRzuaing",
            artists = listOf(
                SimplifiedArtist(
                    id = "artist_2",
                    name = "Eagles",
                    externalUrl = "https://open.spotify.com/artist/0ECwFtbIWE7340Sf1q7ZmD"
                )
            ),
        ),
        popularity = PopularityLevel.VERY_HIGH,
        externalIds = ExternalIdentifiers(
            isrc = "USRC17600201",
            ean = "075678262527",
            upc = "75678262527"
        )
    ),

    DomainDetailedTrack(
        id = "track_3",
        availability = Availability.Available,
        name = "Imagine",
        artists = listOf(
            SimplifiedArtist(
                id = "artist_3",
                name = "John Lennon",
                externalUrl = "https://open.spotify.com/artist/4F4M0JqG0eLmby9TIpoRqa"
            )
        ),
        duration = Duration.parse("3:03"),
        trackNumber = 1,
        discNumber = 1,
        isExplicit = false,
        externalUrl = "https://open.spotify.com/track/7pKfPomDEeI4TPT6EOYjn9",
        album = AlbumOnTrack(
            id = "album_3",
            availability = Availability.Available,
            name = "Imagine",
            releaseType = ReleaseType.ALBUM,
            totalTracks = 10,
            images = listOf(
                Image(
                    url = "https://i.scdn.co/image/ab67616d0000b273ce4f1737e8c31a12bea4b2a5",
                    width = 640,
                    height = 640
                )
            ),
            releaseDate = ReleaseDate(year = 1971, month = 9, day = 9),
            externalUrl = "https://open.spotify.com/album/0XRFSzImai4hJDCLVLgrf6",
            artists = listOf(
                SimplifiedArtist(
                    id = "artist_3",
                    name = "John Lennon",
                    externalUrl = "https://open.spotify.com/artist/4F4M0JqG0eLmby9TIpoRqa"
                )
            ),
        ),
        popularity = PopularityLevel.HIGH,
        externalIds = ExternalIdentifiers(
            isrc = "GBUM71029602",
            ean = "075678262528",
            upc = "75678262528"
        )
    ),

    DomainDetailedTrack(
        id = "track_4",
        availability = Availability.Available,
        name = "Stairway to Heaven",
        artists = listOf(
            SimplifiedArtist(
                id = "artist_4",
                name = "Led Zeppelin",
                externalUrl = "https://open.spotify.com/artist/36QJpDe2go2KgaRleHCDTp"
            )
        ),
        duration = Duration.parse("8:02"),
        trackNumber = 4,
        discNumber = 1,
        isExplicit = false,
        externalUrl = "https://open.spotify.com/track/5CQ30WqJwcep0pYcV4AMNc",
        album = AlbumOnTrack(
            id = "album_4",
            availability = Availability.Available,
            name = "Led Zeppelin IV",
            releaseType = ReleaseType.ALBUM,
            totalTracks = 8,
            images = listOf(
                Image(
                    url = "https://i.scdn.co/image/ab67616d0000b273ce4f1737e8c31a12bea4b2a6",
                    width = 640,
                    height = 640
                )
            ),
            releaseDate = ReleaseDate(year = 1971, month = 11, day = 8),
            externalUrl = "https://open.spotify.com/album/44Ig8dzqOkvkGDzaUof9lK",
            artists = listOf(
                SimplifiedArtist(
                    id = "artist_4",
                    name = "Led Zeppelin",
                    externalUrl = "https://open.spotify.com/artist/36QJpDe2go2KgaRleHCDTp"
                )
            ),
        ),
        popularity = PopularityLevel.VERY_HIGH,
        externalIds = ExternalIdentifiers(
            isrc = "GBUM71029603",
            ean = "075678262529",
            upc = "75678262529"
        )
    ),

    DomainDetailedTrack(
        id = "track_5",
        availability = Availability.Available,
        name = "Like a Rolling Stone",
        artists = listOf(
            SimplifiedArtist(
                id = "artist_5",
                name = "Bob Dylan",
                externalUrl = "https://open.spotify.com/artist/74ASZWbe4lXaubB36ztrGX"
            )
        ),
        duration = Duration.parse("6:13"),
        trackNumber = 1,
        discNumber = 1,
        isExplicit = false,
        externalUrl = "https://open.spotify.com/track/3AhXZa8sUQht0UEdBJgpGc",
        album = AlbumOnTrack(
            id = "album_5",
            availability = Availability.Available,
            name = "Highway 61 Revisited",
            releaseType = ReleaseType.ALBUM,
            totalTracks = 9,
            images = listOf(
                Image(
                    url = "https://i.scdn.co/image/ab67616d0000b273ce4f1737e8c31a12bea4b2a7",
                    width = 640,
                    height = 640
                )
            ),
            releaseDate = ReleaseDate(year = 1965, month = 8, day = 30),
            externalUrl = "https://open.spotify.com/album/0vEwmp9ycQz2divuEzbONC",
            artists = listOf(
                SimplifiedArtist(
                    id = "artist_5",
                    name = "Bob Dylan",
                    externalUrl = "https://open.spotify.com/artist/74ASZWbe4lXaubB36ztrGX"
                )
            ),
        ),
        popularity = PopularityLevel.HIGH,
        externalIds = ExternalIdentifiers(
            isrc = "USRC17600202",
            ean = "075678262530",
            upc = "75678262530"
        )
    )
)
