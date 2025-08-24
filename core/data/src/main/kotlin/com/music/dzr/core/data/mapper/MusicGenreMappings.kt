package com.music.dzr.core.data.mapper

import com.music.dzr.core.model.MusicGenre


fun MusicGenre.Companion.fromNetwork(genre: String): MusicGenre {
    return MusicGenre(name = genre)
}