package com.music.dzr.core.network.api

import com.music.dzr.core.network.model.AlbumBrief
import com.music.dzr.core.network.model.NetworkResponse
import com.music.dzr.core.network.model.ArtistBrief
import com.music.dzr.core.network.model.ArtistBriefWithPicture
import com.music.dzr.core.network.model.CurrentUser
import com.music.dzr.core.network.model.PaginatedList
import com.music.dzr.core.network.model.Permission
import com.music.dzr.core.network.model.PlaylistBrief
import com.music.dzr.core.network.model.RadioBrief
import com.music.dzr.core.network.model.TrackBrief
import com.music.dzr.core.network.model.User
import com.music.dzr.core.network.model.WholeList
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Retrofit API interface for Deezer user endpoints
 *
 * API doesn't support authentication at the moment, so the implementation
 * of user-related methods is imprecise and always returns OAuthException
 *
 * @see <a href="https://developers.deezer.com/api/user">Deezer User API Documentation</a>
 */
internal interface UserApi {
    /**
     * Retrieves information about the current authenticated user.
     */
    @GET("user/me")
    suspend fun getCurrentUser(): NetworkResponse<CurrentUser>

    /**
     * Retrieves the current user's favorite albums.
     */
    @GET("user/me/albums")
    suspend fun getUserFavoriteAlbums(): NetworkResponse<PaginatedList<AlbumBrief>>

    /**
     * Retrieves the current user's favorite artists.
     */
    @GET("user/me/artists")
    suspend fun getUserFavoriteArtists(): NetworkResponse<PaginatedList<ArtistBriefWithPicture>>

    /**
     * Retrieves the current user's top 25 tracks.
     */
    @GET("user/me/charts/tracks")
    suspend fun getUserTopTracks(): NetworkResponse<PaginatedList<TrackBrief>>

    /**
     * Retrieves the current user's top albums.
     */
    @GET("user/me/charts/albums")
    suspend fun getUserTopAlbums(): NetworkResponse<WholeList<AlbumBrief>>

    /**
     * Retrieves the current user's top playlists.
     */
    @GET("user/me/charts/playlists")
    suspend fun getUserTopPlaylists(): NetworkResponse<WholeList<PlaylistBrief>>

    /**
     * Retrieves the current user's top artists.
     */
    @GET("user/me/charts/artists")
    suspend fun getUserTopArtists(): NetworkResponse<WholeList<ArtistBriefWithPicture>>

    /**
     * Retrieves the current user's flow tracks (personalized recommendations).
     */
    @GET("user/me/flow")
    suspend fun getUserFlowTracks(): NetworkResponse<WholeList<TrackBrief>>

    /**
     * Retrieves the list of users that the current user is following.
     */
    @GET("user/me/followings")
    suspend fun getUserFollowings(): NetworkResponse<WholeList<User>>

    /**
     * Retrieves the list of users that follows current user.
     */
    @GET("user/me/followers")
    suspend fun getUserFollowers(): NetworkResponse<WholeList<User>>

    /**
     * Retrieves user's search history.
     */
    @GET("user/me/history")
    suspend fun getUserHistory(): NetworkResponse<WholeList<RadioBrief>>

    /**
     * Provides information about what permissions the user has granted to the application.
     */
    @GET("user/me/permissions")
    suspend fun getUserPermissions(): NetworkResponse<WholeList<Permission>>

    /**
     * Returns tracks that the user has uploaded or added as personal content.
     */
    @GET("user/me/personal_songs")
    suspend fun getUserPersonalSongs(): NetworkResponse<WholeList<TrackBrief>>

    /**
     * Retrieves all playlists created by the user. Requires appropriate permissions for private playlists.
     */
    @GET("user/me/playlists")
    suspend fun getUserPlaylists(): NetworkResponse<WholeList<PlaylistBrief>>

    /**
     * Returns radio stations that the user has marked as favorites.
     */
    @GET("user/me/radios")
    suspend fun getUserFavoriteRadios(): NetworkResponse<WholeList<RadioBrief>>

    /**
     * Provides album recommendations tailored to the user's listening preferences.
     */
    @GET("user/me/recommendations/albums")
    suspend fun getUserRecommendedAlbums(): NetworkResponse<WholeList<AlbumBrief>>

    /**
     * Returns newly released albums recommended specifically for the user.
     */
    @GET("user/me/recommendations/releases")
    suspend fun getUserRecommendedReleases(): NetworkResponse<WholeList<AlbumBrief>>

    /**
     * Provides artist recommendations based on the user's listening history.
     */
    @GET("user/me/recommendations/artists")
    suspend fun getUserRecommendedArtists(): NetworkResponse<WholeList<ArtistBrief>>

    /**
     * Returns playlist recommendations personalized for the user.
     */
    @GET("user/me/recommendations/playlists")
    suspend fun getUserRecommendedPlaylists(): NetworkResponse<WholeList<PlaylistBrief>>

    /**
     * Provides track recommendations based on user preferences and listening patterns.
     */
    @GET("user/me/recommendations/tracks")
    suspend fun getUserRecommendedTracks(): NetworkResponse<WholeList<TrackBrief>>

    /**
     * Returns radio station recommendations tailored to the user's taste.
     */
    @GET("user/me/recommendations/radios")
    suspend fun getUserRecommendedRadios(): NetworkResponse<WholeList<RadioBrief>>

    /**
     * Retrieves all tracks that the user has marked as favorites.
     */
    @GET("user/me/tracks")
    suspend fun getUserFavoriteTracks(): NetworkResponse<WholeList<TrackBrief>>

    /**
     * Modifies playlist information such as title and description.
     */
    @POST("playlist/{playlist_id}")
    @FormUrlEncoded
    suspend fun updatePlaylist(
        @Path("playlist_id") playlistId: Long,
        @Field("title") title: String? = null,
        @Field("description") description: String? = null
    ): NetworkResponse<Unit>

    /**
     * Marks a playlist as viewed by the user.
     * Requires "basic_access" permissions.
     */
    @POST("playlist/{playlist_id}/seen")
    suspend fun markPlaylistAsSeen(@Path("playlist_id") playlistId: Long): NetworkResponse<Unit>

    /**
     * Adds one or more tracks to a specified playlist.
     * Requires "manage_library" permissions.
     */
    @POST("playlist/{playlist_id}/tracks")
    @FormUrlEncoded
    suspend fun addTracksToPlaylist(
        @Path("playlist_id") playlistId: Long,
        @Field("songs") trackIds: String
    ): NetworkResponse<Unit>

    /**
     * Changes the order of tracks within a playlist.
     * Requires "manage_library" permissions.
     */
    @POST("playlist/{playlist_id}/tracks")
    @FormUrlEncoded
    suspend fun reorderPlaylistTracks(
        @Path("playlist_id") playlistId: Long,
        @Field("order") trackIds: String
    ): NetworkResponse<Unit>

    /**
     * Modifies information for a user's personal track.
     * Requires "manage_library" permissions.
     */
    @POST("track/{track_id}")
    @FormUrlEncoded
    suspend fun updatePersonalTrack(
        @Path("track_id") trackId: Long,
        @Field("title") title: String? = null,
        @Field("artist") artist: String? = null
    ): NetworkResponse<Unit>

    /**
     * Adds albums to the user's personal library.
     * Requires "manage_library" permissions.
     */
    @POST("user/{user_id}/albums")
    @FormUrlEncoded
    suspend fun addAlbumsToLibrary(
        @Path("user_id") userId: Long,
        @Field("album_id") albumId: Long
    ): NetworkResponse<Unit>

    /**
     * Adds artists to the user's favorites list.
     * Requires "manage_library" permissions.
     */
    @POST("user/{user_id}/artists")
    @FormUrlEncoded
    suspend fun addArtistsToFavorites(
        @Path("user_id") userId: Long,
        @Field("artist_id") artistId: Long
    ): NetworkResponse<Unit>

    /**
     * Follows another user on the platform.
     * Requires "manage_community" permissions.
     */
    @POST("user/{user_id}/followings")
    @FormUrlEncoded
    suspend fun followUser(
        @Path("user_id") userId: Long,
        @Field("user_id") targetUserId: Long
    ): NetworkResponse<Unit>

    /**
     * Adds a notification to the user's activity feed.
     */
    @POST("user/{user_id}/notifications")
    @FormUrlEncoded
    suspend fun addNotification(
        @Path("user_id") userId: Long,
        @Field("message") message: String
    ): NetworkResponse<Unit>

    /**
     * Creates a new playlist with the specified title.
     * Requires "manage_library" permissions.
     */
    @POST("user/{user_id}/playlists")
    @FormUrlEncoded
    suspend fun createPlaylist(
        @Path("user_id") userId: Long,
        @Field("title") title: String
    ): NetworkResponse<Unit>

    /**
     * Adds playlists to the user's favorites.
     * Requires "manage_library" permissions.
     */
    @POST("user/{user_id}/playlists")
    @FormUrlEncoded
    suspend fun addPlaylistsToFavorites(
        @Path("user_id") userId: Long,
        @Field("playlist_id") playlistId: Long
    ): NetworkResponse<Unit>

    /**
     * Adds a radio station to the user's favorites.
     * Requires "manage_library" permissions.
     */
    @POST("user/{user_id}/radios")
    @FormUrlEncoded
    suspend fun addRadioToFavorites(
        @Path("user_id") userId: Long,
        @Field("radio_id") radioId: Long
    ): NetworkResponse<Unit>

    /**
     * Adds tracks to the user's favorites collection.
     * Requires "manage_library" permission.
     */
    @POST("user/{user_id}/tracks")
    @FormUrlEncoded
    suspend fun addTracksToFavorites(
        @Path("user_id") userId: Long,
        @Field("track_id") trackId: Long
    ): NetworkResponse<Unit>

    /**
     * Deletes specified playlist.
     * Requires "delete_library" permission.
     */
    @DELETE("playlist/{playlist_id}")
    suspend fun deletePlaylist(
        @Path("playlist_id") playlistId: Long,
    ): NetworkResponse<Unit>

    /**
     * Removes specified tracks from a playlist.
     * Requires "delete_library" permission.
     */
    @DELETE("playlist/{playlist_id}/tracks")
    suspend fun removeTracksFromPlaylist(
        @Path("playlist_id") playlistId: Long,
        @Query("songs") trackIds: String
    ): NetworkResponse<Unit>

    /**
     * Removes albums from the user's personal library.
     * Requires "manage_library" and "delete_library" permissions.
     */
    @DELETE("user/{user_id}/albums")
    suspend fun removeAlbumsFromLibrary(
        @Path("user_id") userId: Long,
        @Query("album_id") albumId: Long
    ): NetworkResponse<Unit>

    /**
     * Removes artists from the user's favorites list.
     * Requires "manage_library" and "delete_library" permissions.
     */
    @DELETE("user/{user_id}/artists")
    suspend fun removeArtistsFromFavorites(
        @Path("user_id") userId: Long,
        @Query("artist_id") artistId: Long
    ): NetworkResponse<Unit>

    /**
     * Stops following a specific user on the platform.
     * Requires "manage_library" permission.
     */
    @DELETE("user/{user_id}/followings")
    suspend fun unfollowUser(
        @Path("user_id") userId: Long,
        @Query("user_id") targetUserId: Long
    ): NetworkResponse<Unit>

    /**
     * Removes playlists from the user's favorites collection.
     * Requires "manage_library" and "delete_library" permissions.
     */
    @DELETE("user/{user_id}/playlists")
    suspend fun removePlaylistsFromFavorites(
        @Path("user_id") userId: Long,
        @Query("playlist_id") playlistId: Long
    ): NetworkResponse<Unit>

    /**
     * Removes a radio station from the user's favorites.
     * Requires "manage_library" and "delete_library" permissions.
     */
    @DELETE("user/{user_id}/radios")
    suspend fun removeRadioFromFavorites(
        @Path("user_id") userId: Long,
        @Query("radio_id") radioId: Long
    ): NetworkResponse<Unit>

    /**
     * Removes tracks from the user's favorites collection.
     * Requires delete and manage library permissions.
     */
    @DELETE("user/{user_id}/tracks")
    suspend fun removeTracksFromFavorites(
        @Path("user_id") userId: Long,
        @Query("track_id") trackId: Long
    ): NetworkResponse<Unit>
}