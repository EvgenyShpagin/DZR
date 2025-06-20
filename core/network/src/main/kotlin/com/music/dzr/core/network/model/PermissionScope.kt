package com.music.dzr.core.network.model

/**
 * Scopes provide Spotify users using third-party apps the confidence that only the information
 * they choose to share will be shared, and nothing more.
 *
 * @see <a href="https://developer.spotify.com/documentation/web-api/concepts/scopes">Spotify Scopes</a>
 */
enum class PermissionScope {
    /**
     * Write access to user-provided images.
     */
    UgcImageUpload,

    /**
     * Read access to a user’s player state.
     */
    UserReadPlaybackState,

    /**
     * Write access to a user’s playback state
     */
    UserModifyPlaybackState,

    /**
     * Read access to a user’s currently playing content.
     */
    UserReadCurrentlyPlaying,

    /**
     * 	Remote control playback of Spotify.
     */
    AppRemoteControl,

    /**
     * 	Control playback of a Spotify track. The user must have a Spotify Premium account.
     */
    Streaming,

    /**
     * Read access to user's private playlists.
     */
    PlaylistReadPrivate,

    /**
     * Include collaborative playlists when requesting a user's playlists.
     */
    PlaylistReadCollaborative,

    /**
     * Write access to a user's private playlists.
     */
    PlaylistModifyPrivate,

    /**
     * Write access to a user's public playlists.
     */
    PlaylistModifyPublic,

    /**
     * Write/delete access to the list of artists and other users that the user follows.
     */
    UserFollowModify,

    /**
     * Read access to the list of artists and other users that the user follows.
     */
    UserFollowRead,

    /**
     * Read access to a user's top artists and tracks.
     */
    UserTopRead,

    /**
     * Read access to a user's recently played tracks.
     */
    UserReadRecentlyPlayed,

    /**
     * Write/delete access to a user's "Your Music" library.
     */
    UserLibraryModify,

    /**
     * Read access to a user's library.
     */
    UserLibraryRead,

    /**
     * Read access to user's email address.
     */
    UserReadEmail,

    /**
     * Read access to user's subscription details (type of user account).
     */
    UserReadPrivate
}