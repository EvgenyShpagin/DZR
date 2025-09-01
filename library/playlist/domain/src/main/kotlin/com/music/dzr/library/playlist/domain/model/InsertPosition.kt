package com.music.dzr.library.playlist.domain.model

sealed interface InsertPosition {
    data object Append : InsertPosition
    data class At(val index: Int) : InsertPosition
}