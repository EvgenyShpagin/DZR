package com.music.dzr.library.playlist.domain.model

/**
 * Defines the position where a new item should be inserted.
 *
 * Use [Append] to add an item to the end of a collection,
 * or [At] to insert it at the given index.
 */
sealed interface InsertPosition {
    data object Append : InsertPosition
    data class At(val index: Int) : InsertPosition
}