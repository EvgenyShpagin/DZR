package com.music.dzr.core.network.model

/**
 * Scopes provide Spotify users using third-party apps the confidence that only the information
 * they choose to share will be shared, and nothing more.
 *
 * @see <a href="https://developer.spotify.com/documentation/web-api/concepts/scopes">Spotify Scopes</a>
 */
@JvmInline
value class PermissionScope private constructor(val value: String) {
    companion object {
        /**
         * Write access to user-provided images.
         */
        val UgcImageUpload = PermissionScope("ugc-image-upload")


        /**
         * Read access to a user's player state.
         */
        val UserReadPlaybackState = PermissionScope("user-read-playback-state")


        /**
         * Write access to a user's playback state
         */
        val UserModifyPlaybackState = PermissionScope("user-modify-playback-state")


        /**
         * Read access to a user's currently playing content.
         */
        val UserReadCurrentlyPlaying = PermissionScope("user-read-currently-playing")


        /**
         * 	Remote control playback of Spotify.
         */
        val AppRemoteControl = PermissionScope("app-remote-control")


        /**
         * 	Control playback of a Spotify track. The user must have a Spotify Premium account.
         */
        val Streaming = PermissionScope("streaming")


        /**
         * Read access to user's private playlists.
         */
        val PlaylistReadPrivate = PermissionScope("playlist-read-private")


        /**
         * Include collaborative playlists when requesting a user's playlists.
         */
        val PlaylistReadCollaborative = PermissionScope("playlist-read-collaborative")


        /**
         * Write access to a user's private playlists.
         */
        val PlaylistModifyPrivate = PermissionScope("playlist-modify-private")


        /**
         * Write access to a user's public playlists.
         */
        val PlaylistModifyPublic = PermissionScope("playlist-modify-public")


        /**
         * Write/delete access to the list of artists and other users that the user follows.
         */
        val UserFollowModify = PermissionScope("user-follow-modify")


        /**
         * Read access to the list of artists and other users that the user follows.
         */
        val UserFollowRead = PermissionScope("user-follow-read")


        /**
         * Read access to a user's top artists and tracks.
         */
        val UserTopRead = PermissionScope("user-top-read")


        /**
         * Read access to a user's recently played tracks.
         */
        val UserReadRecentlyPlayed = PermissionScope("user-read-recently-played")


        /**
         * Write/delete access to a user's "Your Music" library.
         */
        val UserLibraryModify = PermissionScope("user-library-modify")


        /**
         * Read access to a user's library.
         */
        val UserLibraryRead = PermissionScope("user-library-read")


        /**
         * Read access to user's email address.
         */
        val UserReadEmail = PermissionScope("user-read-email")


        /**
         * Read access to user's subscription details (type of user account).
         */
        val UserReadPrivate = PermissionScope("user-read-private")

    }
}