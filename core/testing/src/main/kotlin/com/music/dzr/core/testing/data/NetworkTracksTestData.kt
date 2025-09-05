package com.music.dzr.core.testing.data

import com.music.dzr.core.network.dto.AlbumType
import com.music.dzr.core.network.dto.ExternalIds
import com.music.dzr.core.network.dto.ExternalUrls
import com.music.dzr.core.network.dto.Image
import com.music.dzr.core.network.dto.ReleaseDate
import com.music.dzr.core.network.dto.ReleaseDatePrecision
import com.music.dzr.core.network.dto.SimplifiedArtist
import com.music.dzr.core.network.dto.Track
import com.music.dzr.core.network.dto.TrackAlbum

val networkDetailedTracksTestData: List<Track> = listOf(
    Track(
        id = "track_1",
        name = "Bohemian Rhapsody",
        artists = listOf(
            SimplifiedArtist(
                externalUrls = ExternalUrls(
                    spotify = "https://open.spotify.com/artist/1dfeR4HaWDbWqFHLkxsg1d"
                ),
                href = "https://api.spotify.com/v1/artists/artist_1",
                id = "artist_1",
                name = "Queen",
                type = "artist",
                uri = "spotify:artist:artist_1"
            )
        ),
        album = TrackAlbum(
            albumType = AlbumType.Album,
            totalTracks = 12,
            availableMarkets = listOf("US", "GB", "DE"),
            externalUrls = ExternalUrls(
                spotify = "https://open.spotify.com/album/1GbtB4nxMcyF2VSqfAD0l8"
            ),
            href = "https://api.spotify.com/v1/albums/album_1",
            id = "album_1",
            images = listOf(
                Image(
                    url = "https://i.scdn.co/image/ab67616d0000b273ce4f1737e8c31a12bea4b2a3",
                    height = 640,
                    width = 640
                )
            ),
            name = "A Night at the Opera",
            releaseDate = ReleaseDate(
                year = 1975,
                month = 11,
                day = 21
            ),
            releaseDatePrecision = ReleaseDatePrecision.DAY,
            restrictions = null,
            type = "album",
            uri = "spotify:album:album_1",
            artists = listOf(
                SimplifiedArtist(
                    externalUrls = ExternalUrls(
                        spotify = "https://open.spotify.com/artist/1dfeR4HaWDbWqFHLkxsg1d"
                    ),
                    href = "https://api.spotify.com/v1/artists/artist_1",
                    id = "artist_1",
                    name = "Queen",
                    type = "artist",
                    uri = "spotify:artist:artist_1"
                )
            ),
            copyrights = null,
            externalIds = null,
            genres = null,
            label = null,
            popularity = null,
            isPlayable = true
        ),
        availableMarkets = listOf("US", "GB", "DE"),
        discNumber = 1,
        durationMs = 355000, // 5:55 в миллисекундах
        explicit = false,
        externalIds = ExternalIds(
            isrc = "GBUM71029601",
            ean = "075678262526",
            upc = "75678262526"
        ),
        externalUrls = ExternalUrls(
            spotify = "https://open.spotify.com/track/3z8h0TU7ReDPLIbEnYhWZb"
        ),
        href = "https://api.spotify.com/v1/tracks/track_1",
        isPlayable = true,
        linkedFrom = null,
        restrictions = null,
        popularity = 95,
        trackNumber = 1,
        type = "track",
        uri = "spotify:track:track_1",
        isLocal = false
    ),

    Track(
        id = "track_2",
        name = "Hotel California",
        artists = listOf(
            SimplifiedArtist(
                externalUrls = ExternalUrls(
                    spotify = "https://open.spotify.com/artist/0ECwFtbIWE7340Sf1q7ZmD"
                ),
                href = "https://api.spotify.com/v1/artists/artist_2",
                id = "artist_2",
                name = "Eagles",
                type = "artist",
                uri = "spotify:artist:artist_2"
            )
        ),
        album = TrackAlbum(
            albumType = AlbumType.Album,
            totalTracks = 9,
            availableMarkets = listOf("US", "GB", "CA"),
            externalUrls = ExternalUrls(
                spotify = "https://open.spotify.com/album/2o7KjiY6AhGOwtQeRzuaing"
            ),
            href = "https://api.spotify.com/v1/albums/album_2",
            id = "album_2",
            images = listOf(
                Image(
                    url = "https://i.scdn.co/image/ab67616d0000b273ce4f1737e8c31a12bea4b2a4",
                    height = 640,
                    width = 640
                )
            ),
            name = "Hotel California",
            releaseDate = ReleaseDate(year = 1976, month = 12, day = 8),
            releaseDatePrecision = ReleaseDatePrecision.DAY,
            restrictions = null,
            type = "album",
            uri = "spotify:album:album_2",
            artists = listOf(
                SimplifiedArtist(
                    externalUrls = ExternalUrls(
                        spotify = "https://open.spotify.com/artist/0ECwFtbIWE7340Sf1q7ZmD"
                    ),
                    href = "https://api.spotify.com/v1/artists/artist_2",
                    id = "artist_2",
                    name = "Eagles",
                    type = "artist",
                    uri = "spotify:artist:artist_2"
                )
            ),
            copyrights = null,
            externalIds = null,
            genres = null,
            label = null,
            popularity = null,
            isPlayable = true
        ),
        availableMarkets = listOf("US", "GB", "CA"),
        discNumber = 1,
        durationMs = 390000, // 6:30 в миллисекундах
        explicit = false,
        externalIds = ExternalIds(
            isrc = "USRC17600201",
            ean = "075678262527",
            upc = "75678262527"
        ),
        externalUrls = ExternalUrls(
            spotify = "https://open.spotify.com/track/40riOyOyWxJjGm1hYqaqqq"
        ),
        href = "https://api.spotify.com/v1/tracks/track_2",
        isPlayable = true,
        linkedFrom = null,
        restrictions = null,
        popularity = 95,
        trackNumber = 1,
        type = "track",
        uri = "spotify:track:track_2",
        isLocal = false
    ),

    Track(
        id = "track_3",
        name = "Imagine",
        artists = listOf(
            SimplifiedArtist(
                externalUrls = ExternalUrls(
                    spotify = "https://open.spotify.com/artist/4F4M0JqG0eLmby9TIpoRqa"
                ),
                href = "https://api.spotify.com/v1/artists/artist_3",
                id = "artist_3",
                name = "John Lennon",
                type = "artist",
                uri = "spotify:artist:artist_3"
            )
        ),
        album = TrackAlbum(
            albumType = AlbumType.Album,
            totalTracks = 10,
            availableMarkets = listOf("US", "GB", "JP"),
            externalUrls = ExternalUrls(
                spotify = "https://open.spotify.com/album/0XRFSzImai4hJDCLVLgrf6"
            ),
            href = "https://api.spotify.com/v1/albums/album_3",
            id = "album_3",
            images = listOf(
                Image(
                    url = "https://i.scdn.co/image/ab67616d0000b273ce4f1737e8c31a12bea4b2a5",
                    height = 640,
                    width = 640
                )
            ),
            name = "Imagine",
            releaseDate = ReleaseDate(year = 1971, month = 9, day = 9),
            releaseDatePrecision = ReleaseDatePrecision.DAY,
            restrictions = null,
            type = "album",
            uri = "spotify:album:album_3",
            artists = listOf(
                SimplifiedArtist(
                    externalUrls = ExternalUrls(
                        spotify = "https://open.spotify.com/artist/4F4M0JqG0eLmby9TIpoRqa"
                    ),
                    href = "https://api.spotify.com/v1/artists/artist_3",
                    id = "artist_3",
                    name = "John Lennon",
                    type = "artist",
                    uri = "spotify:artist:artist_3"
                )
            ),
            copyrights = null,
            externalIds = null,
            genres = null,
            label = null,
            popularity = null,
            isPlayable = true
        ),
        availableMarkets = listOf("US", "GB", "JP"),
        discNumber = 1,
        durationMs = 183000, // 3:03 в миллисекундах
        explicit = false,
        externalIds = ExternalIds(
            isrc = "GBUM71029602",
            ean = "075678262528",
            upc = "75678262528"
        ),
        externalUrls = ExternalUrls(
            spotify = "https://open.spotify.com/track/7pKfPomDEeI4TPT6EOYjn9"
        ),
        href = "https://api.spotify.com/v1/tracks/track_3",
        isPlayable = true,
        linkedFrom = null,
        restrictions = null,
        popularity = 80,
        trackNumber = 1,
        type = "track",
        uri = "spotify:track:track_3",
        isLocal = false
    ),

    Track(
        id = "track_4",
        name = "Stairway to Heaven",
        artists = listOf(
            SimplifiedArtist(
                externalUrls = ExternalUrls(
                    spotify = "https://open.spotify.com/artist/36QJpDe2go2KgaRleHCDTp"
                ),
                href = "https://api.spotify.com/v1/artists/artist_4",
                id = "artist_4",
                name = "Led Zeppelin",
                type = "artist",
                uri = "spotify:artist:artist_4"
            )
        ),
        album = TrackAlbum(
            albumType = AlbumType.Album,
            totalTracks = 8,
            availableMarkets = listOf("US", "GB", "DE"),
            externalUrls = ExternalUrls(
                spotify = "https://open.spotify.com/album/44Ig8dzqOkvkGDzaUof9lK"
            ),
            href = "https://api.spotify.com/v1/albums/album_4",
            id = "album_4",
            images = listOf(
                Image(
                    url = "https://i.scdn.co/image/ab67616d0000b273ce4f1737e8c31a12bea4b2a6",
                    height = 640,
                    width = 640
                )
            ),
            name = "Led Zeppelin IV",
            releaseDate = ReleaseDate(
                year = 1971,
                month = 11,
                day = 8
            ),
            releaseDatePrecision = ReleaseDatePrecision.DAY,
            restrictions = null,
            type = "album",
            uri = "spotify:album:album_4",
            artists = listOf(
                SimplifiedArtist(
                    externalUrls = ExternalUrls(
                        spotify = "https://open.spotify.com/artist/36QJpDe2go2KgaRleHCDTp"
                    ),
                    href = "https://api.spotify.com/v1/artists/artist_4",
                    id = "artist_4",
                    name = "Led Zeppelin",
                    type = "artist",
                    uri = "spotify:artist:artist_4"
                )
            ),
            copyrights = null,
            externalIds = null,
            genres = null,
            label = null,
            popularity = null,
            isPlayable = true
        ),
        availableMarkets = listOf("US", "GB", "DE"),
        discNumber = 1,
        durationMs = 482000, // 8:02 в миллисекундах
        explicit = false,
        externalIds = ExternalIds(
            isrc = "GBUM71029603",
            ean = "075678262529",
            upc = "75678262529"
        ),
        externalUrls = ExternalUrls(
            spotify = "https://open.spotify.com/track/5CQ30WqJwcep0pYcV4AMNc"
        ),
        href = "https://api.spotify.com/v1/tracks/track_4",
        isPlayable = true,
        linkedFrom = null,
        restrictions = null,
        popularity = 95,
        trackNumber = 4,
        type = "track",
        uri = "spotify:track:track_4",
        isLocal = false
    ),

    Track(
        id = "track_5",
        name = "Like a Rolling Stone",
        artists = listOf(
            SimplifiedArtist(
                externalUrls = ExternalUrls(
                    spotify = "https://open.spotify.com/artist/74ASZWbe4lXaubB36ztrGX"
                ),
                href = "https://api.spotify.com/v1/artists/artist_5",
                id = "artist_5",
                name = "Bob Dylan",
                type = "artist",
                uri = "spotify:artist:artist_5"
            )
        ),
        album = TrackAlbum(
            albumType = AlbumType.Album,
            totalTracks = 9,
            availableMarkets = listOf("US", "GB", "CA"),
            externalUrls = ExternalUrls(
                spotify = "https://open.spotify.com/album/0vEwmp9ycQz2divuEzbONC"
            ),
            href = "https://api.spotify.com/v1/albums/album_5",
            id = "album_5",
            images = listOf(
                Image(
                    url = "https://i.scdn.co/image/ab67616d0000b273ce4f1737e8c31a12bea4b2a7",
                    height = 640,
                    width = 640
                )
            ),
            name = "Highway 61 Revisited",
            releaseDate = ReleaseDate(
                year = 1965,
                month = 8,
                day = 30
            ),
            releaseDatePrecision = ReleaseDatePrecision.DAY,
            restrictions = null,
            type = "album",
            uri = "spotify:album:album_5",
            artists = listOf(
                SimplifiedArtist(
                    externalUrls = ExternalUrls(
                        spotify = "https://open.spotify.com/artist/74ASZWbe4lXaubB36ztrGX"
                    ),
                    href = "https://api.spotify.com/v1/artists/artist_5",
                    id = "artist_5",
                    name = "Bob Dylan",
                    type = "artist",
                    uri = "spotify:artist:artist_5"
                )
            ),
            copyrights = null,
            externalIds = null,
            genres = null,
            label = null,
            popularity = null,
            isPlayable = true
        ),
        availableMarkets = listOf("US", "GB", "CA"),
        discNumber = 1,
        durationMs = 373000, // 6:13 в миллисекундах
        explicit = false,
        externalIds = ExternalIds(
            isrc = "USRC17600202",
            ean = "075678262530",
            upc = "75678262530"
        ),
        externalUrls = ExternalUrls(
            spotify = "https://open.spotify.com/track/3AhXZa8sUQht0UEdBJgpGc"
        ),
        href = "https://api.spotify.com/v1/tracks/track_5",
        isPlayable = true,
        linkedFrom = null,
        restrictions = null,
        popularity = 80,
        trackNumber = 1,
        type = "track",
        uri = "spotify:track:track_5",
        isLocal = false
    )
)