package com.music.dzr.core.data.mapper

import com.music.dzr.core.model.MusicGenre


fun MusicGenre.Companion.parse(networkGenre: String): MusicGenre {
    return MusicGenre(name = networkGenre)
}