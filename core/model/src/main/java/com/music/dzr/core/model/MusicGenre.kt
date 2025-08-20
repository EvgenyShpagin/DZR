package com.music.dzr.core.model

/**
 * Music genre representation.
 */
@JvmInline
value class MusicGenre(val name: String) {
    override fun toString(): String = name

    companion object
}
