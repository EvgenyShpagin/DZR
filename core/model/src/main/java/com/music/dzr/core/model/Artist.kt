package com.music.dzr.core.model

/**
 * Artist representation for different contexts.
 */
abstract class Artist {
    abstract val id: String
    abstract val name: String
    abstract val externalUrl: String
}