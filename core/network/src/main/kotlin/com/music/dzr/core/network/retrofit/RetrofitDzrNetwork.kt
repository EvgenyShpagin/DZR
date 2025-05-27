package com.music.dzr.core.network.retrofit

import com.music.dzr.core.network.model.Album
import com.music.dzr.core.network.model.AlbumBrief
import com.music.dzr.core.network.model.AlbumTrack
import com.music.dzr.core.network.model.Artist
import com.music.dzr.core.network.model.ArtistAlbum
import com.music.dzr.core.network.model.ArtistBrief
import com.music.dzr.core.network.model.ArtistBriefWithPicture
import com.music.dzr.core.network.model.ArtistPlaylist
import com.music.dzr.core.network.model.ArtistTopTrack
import com.music.dzr.core.network.model.Chart
import com.music.dzr.core.network.model.ChartAlbum
import com.music.dzr.core.network.model.ChartArtist
import com.music.dzr.core.network.model.ChartPlaylist
import com.music.dzr.core.network.model.ChartTrack
import com.music.dzr.core.network.model.Editorial
import com.music.dzr.core.network.model.EditorialReleasesAlbum
import com.music.dzr.core.network.model.EditorialSelectionAlbum
import com.music.dzr.core.network.model.CurrentUser
import com.music.dzr.core.network.model.Genre
import com.music.dzr.core.network.model.GenreArtist
import com.music.dzr.core.network.model.PaginatedList
import com.music.dzr.core.network.model.Permission
import com.music.dzr.core.network.model.Playlist
import com.music.dzr.core.network.model.PlaylistBrief
import com.music.dzr.core.network.model.PlaylistTrack
import com.music.dzr.core.network.model.User
import com.music.dzr.core.network.model.Radio
import com.music.dzr.core.network.model.RadioBrief
import com.music.dzr.core.network.model.RadioTrackBrief
import com.music.dzr.core.network.model.SearchAlbum
import com.music.dzr.core.network.model.SearchTrack
import com.music.dzr.core.network.model.SearchUser
import com.music.dzr.core.network.model.Track
import com.music.dzr.core.network.model.TrackBrief
import com.music.dzr.core.network.model.WholeList
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query


/**
 * Retrofit API interface for Deezer API endpoints.
 *
 * This interface defines all available endpoints for interacting with the Deezer API,
 * including album, artist, chart, editorial, genre, playlist, radio, search, track,
 * and user endpoints with both read and write operations.
 *
 * @see <a href="https://developers.deezer.com/api">Deezer API Documentation</a>
 */
private interface RetrofitDzrNetworkApi {

    // ========== ALBUM ENDPOINTS ========== //

    /**
     * Retrieves detailed information about a specific album.
     */
    @GET("album/{id}")
    suspend fun getAlbum(@Path("id") albumId: Long): Album

    /**
     * Retrieves all tracks from a specific album.
     */
    @GET("album/{id}/tracks")
    suspend fun getAlbumTracks(@Path("id") albumId: Long): PaginatedList<AlbumTrack>

    // ========== ARTIST ENDPOINTS ========== //

    /**
     * Retrieves detailed information about a specific artist.
     */
    @GET("artist/{id}")
    suspend fun getArtist(@Path("id") artistId: Long): Artist

    /**
     * Retrieves the top 5 tracks of an artist.
     */
    @GET("artist/{id}/top")
    suspend fun getArtistTopTracks(@Path("id") artistId: Long): PaginatedList<ArtistTopTrack>

    /**
     * Retrieves all albums by a specific artist.
     */
    @GET("artist/{id}/albums")
    suspend fun getArtistAlbums(@Path("id") artistId: Long): PaginatedList<ArtistAlbum>

    /**
     * Retrieves artists related to the specified artist.
     */
    @GET("artist/{id}/related")
    suspend fun getRelatedArtists(@Path("id") artistId: Long): PaginatedList<Artist>

    /**
     * Retrieves radio tracks based on an artist's style.
     */
    @GET("artist/{id}/radio")
    suspend fun getArtistRadioTracks(@Path("id") artistId: Long): WholeList<TrackBrief>

    /**
     * Retrieves playlists created by or featuring the artist.
     */
    @GET("artist/{id}/playlists")
    suspend fun getArtistPlaylists(@Path("id") artistId: Long): PaginatedList<ArtistPlaylist>

    // ========== CHART ENDPOINTS ==========

    /**
     * Retrieves general charts for a specified genre.
     */
    @GET("chart")
    suspend fun getCharts(): Chart

    /**
     * Retrieves the top tracks from charts.
     */
    @GET("chart/{genre_id}/tracks")
    suspend fun getTopTracks(@Path("genre_id") genreId: Int = 0): PaginatedList<ChartTrack>

    /**
     * Retrieves the top albums from charts.
     */
    @GET("chart/{genre_id}/albums")
    suspend fun getTopAlbums(@Path("genre_id") genreId: Int = 0): PaginatedList<ChartAlbum>

    /**
     * Retrieves the top artists from charts.
     */
    @GET("chart/{genre_id}/artists")
    suspend fun getTopArtists(@Path("genre_id") genreId: Int = 0): PaginatedList<ChartArtist>

    /**
     * Retrieves the top playlists from charts.
     */
    @GET("chart/{genre_id}/playlists")
    suspend fun getTopPlaylists(@Path("genre_id") genreId: Int = 0): PaginatedList<ChartPlaylist>

    // ========== EDITORIAL ENDPOINTS ==========

    /**
     * Retrieves all available editorial objects.
     */
    @GET("editorial")
    suspend fun getEditorials(): PaginatedList<Editorial>

    /**
     * Retrieves detailed information about a specific editorial.
     */
    @GET("editorial/{id}")
    suspend fun getEditorial(@Path("id") editorialId: Int): Editorial

    /**
     * Retrieves albums selected weekly by the Deezer Team.
     */
    @GET("editorial/{id}/selection")
    suspend fun getEditorialSelection(@Path("id") editorialId: Int): WholeList<EditorialSelectionAlbum>

    /**
     * Retrieves new releases per genre for the current country.
     */
    @GET("editorial/{id}/releases")
    suspend fun getEditorialReleases(@Path("id") editorialId: Int): PaginatedList<EditorialReleasesAlbum>

    // ========== GENRE ENDPOINTS ==========

    /**
     * Retrieves all available music genres.
     */
    @GET("genre")
    suspend fun getGenres(): WholeList<Genre>

    /**
     * Retrieves detailed information about a specific genre.
     */
    @GET("genre/{id}")
    suspend fun getGenre(@Path("id") genreId: Int): Genre

    /**
     * Retrieves all artists associated with a specific genre.
     */
    @GET("genre/{id}/artists")
    suspend fun getGenreArtists(@Path("id") genreId: Int): WholeList<GenreArtist>

    /**
     * Retrieves all radios associated with a specific genre.
     */
    @GET("genre/{id}/radios")
    suspend fun getGenreRadios(@Path("id") genreId: Int): WholeList<RadioBrief>

    // ========== PLAYLIST ENDPOINTS ==========

    /**
     * Retrieves detailed information about a specific playlist.
     */
    @GET("playlist/{id}")
    suspend fun getPlaylist(@Path("id") playlistId: Long): Playlist

    /**
     * Retrieves all tracks from a specific playlist.
     */
    @GET("playlist/{id}/tracks")
    suspend fun getPlaylistTracks(@Path("id") playlistId: Long): PaginatedList<PlaylistTrack>

    // ========== RADIO ENDPOINTS ==========

    /**
     * Retrieves all available radio stations.
     */
    @GET("radio")
    suspend fun getRadios(): PaginatedList<RadioBrief>

    /**
     * Retrieves detailed information about a specific radio station.
     */
    @GET("radio/{id}")
    suspend fun getRadio(@Path("id") radioId: Int): Radio

    /**
     * Retrieves radio stations organized by genre.
     */
    @GET("radio/genres")
    suspend fun getRadioGenres(): PaginatedList<RadioBrief>

    /**
     * Retrieves the top radio stations (default: 25 radios).
     */
    @GET("radio/top")
    suspend fun getTopRadios(): PaginatedList<RadioBrief>

    /**
     * Retrieves the first 40 tracks from a specific radio station.
     */
    @GET("radio/{id}/tracks")
    suspend fun getRadioTracks(@Path("id") radioId: Int): PaginatedList<RadioTrackBrief>

    /**
     * Retrieves personal radio lists organized by genre (similar to MIX on website).
     */
    @GET("radio/lists")
    suspend fun getRadioLists(): PaginatedList<RadioBrief>

    // ========== SEARCH ENDPOINTS ==========

    /**
     * Searches for tracks across the Deezer catalog.
     *
     * @param query The search query string
     * @param strict Disable fuzzy search mode (use "on" to enable strict mode)
     * @param order Sort order for results (see SearchOrder enum)
     */
    @GET("search")
    suspend fun searchTracks(
        @Query("q") query: String,
        @Query("strict") strict: String? = null,
        @Query("order") order: String? = null
    ): PaginatedList<SearchTrack>

    /**
     * Searches for albums across the Deezer catalog.
     *
     * @param query The search query string
     * @param strict Disable fuzzy search mode (use "on" to enable strict mode)
     * @param order Sort order for results (see SearchOrder enum)
     */
    @GET("search/album")
    suspend fun searchAlbums(
        @Query("q") query: String,
        @Query("strict") strict: String? = null,
        @Query("order") order: String? = null
    ): PaginatedList<SearchAlbum>

    /**
     * Searches for artists across the Deezer catalog.
     *
     * @param query The search query string
     * @param strict Disable fuzzy search mode (use "on" to enable strict mode)
     * @param order Sort order for results (see SearchOrder enum)
     */
    @GET("search/artist")
    suspend fun searchArtists(
        @Query("q") query: String,
        @Query("strict") strict: String? = null,
        @Query("order") order: String? = null
    ): PaginatedList<Artist>

    /**
     * Retrieves user's search history.
     *
     * @param query The search query string to filter history
     * @param strict Disable fuzzy search mode (use "on" to enable strict mode)
     * @param order Sort order for results (see SearchOrder enum)
     */
    @GET("search/history")
    suspend fun getSearchHistory(
        @Query("q") query: String,
        @Query("strict") strict: String? = null,
        @Query("order") order: String? = null
    ): WholeList<RadioBrief>

    /**
     * Searches for playlists across the Deezer catalog.
     *
     * @param query The search query string
     * @param strict Disable fuzzy search mode (use "on" to enable strict mode)
     * @param order Sort order for results (see SearchOrder enum)
     */
    @GET("search/playlist")
    suspend fun searchPlaylists(
        @Query("q") query: String,
        @Query("strict") strict: String? = null,
        @Query("order") order: String? = null
    ): PaginatedList<PlaylistBrief>

    /**
     * Searches for radio stations across the Deezer catalog.
     *
     * @param query The search query string
     * @param strict Disable fuzzy search mode (use "on" to enable strict mode)
     * @param order Sort order for results (see SearchOrder enum)
     */
    @GET("search/radio")
    suspend fun searchRadios(
        @Query("q") query: String,
        @Query("strict") strict: String? = null,
        @Query("order") order: String? = null
    ): PaginatedList<RadioBrief>

    /**
     * Searches for users across the Deezer platform.
     *
     * @param query The search query string
     * @param strict Disable fuzzy search mode (use "on" to enable strict mode)
     * @param order Sort order for results (see SearchOrder enum)
     */
    @GET("search/user")
    suspend fun searchUsers(
        @Query("q") query: String,
        @Query("strict") strict: String? = null,
        @Query("order") order: String? = null
    ): PaginatedList<SearchUser>

    // ========== TRACK ENDPOINTS ==========

    /**
     * Retrieves detailed information about a specific track.
     */
    @GET("track/{id}")
    suspend fun getTrack(@Path("id") trackId: Long): Track

    // ========== USER ENDPOINTS ==========
    /* API doesn't support authentication at the moment, so the implementation
    of user-related methods is imprecise and always returns OAuthException */

    /**
     * Retrieves information about the current authenticated user.
     */
    @GET("user/me")
    suspend fun getCurrentUser(): CurrentUser

    /**
     * Retrieves the current user's favorite albums.
     */
    @GET("user/me/albums")
    suspend fun getUserFavoriteAlbums(): PaginatedList<AlbumBrief>

    /**
     * Retrieves the current user's favorite artists.
     */
    @GET("user/me/artists")
    suspend fun getUserFavoriteArtists(): PaginatedList<ArtistBriefWithPicture>

    /**
     * Retrieves the current user's top 25 tracks.
     */
    @GET("user/me/charts/tracks")
    suspend fun getUserTopTracks(): PaginatedList<TrackBrief>

    /**
     * Retrieves the current user's top albums.
     */
    @GET("user/me/charts/albums")
    suspend fun getUserTopAlbums(): WholeList<AlbumBrief>

    /**
     * Retrieves the current user's top playlists.
     */
    @GET("user/me/charts/playlists")
    suspend fun getUserTopPlaylists(): WholeList<PlaylistBrief>

    /**
     * Retrieves the current user's top artists.
     */
    @GET("user/me/charts/artists")
    suspend fun getUserTopArtists(): WholeList<ArtistBriefWithPicture>

    /**
     * Retrieves the current user's flow tracks (personalized recommendations).
     */
    @GET("user/me/flow")
    suspend fun getUserFlowTracks(): WholeList<TrackBrief>

    /**
     * Retrieves the list of users that the current user is following.
     */
    @GET("user/me/followings")
    suspend fun getUserFollowings(): WholeList<User>

    /**
     * Retrieves the list of users that follows current user.
     */
    @GET("user/me/followers")
    suspend fun getUserFollowers(): WholeList<User>

    /**
     * Retrieves user's search history.
     */
    @GET("user/me/history")
    suspend fun getUserHistory(): WholeList<RadioBrief>

    /**
     * Provides information about what permissions the user has granted to the application.
     */
    @GET("user/me/permissions")
    suspend fun getUserPermissions(): WholeList<Permission>

    /**
     * Returns tracks that the user has uploaded or added as personal content.
     */
    @GET("user/me/personal_songs")
    suspend fun getUserPersonalSongs(): WholeList<TrackBrief>

    /**
     * Retrieves all playlists created by the user. Requires appropriate permissions for private playlists.
     */
    @GET("user/me/playlists")
    suspend fun getUserPlaylists(): WholeList<PlaylistBrief>

    /**
     * Returns radio stations that the user has marked as favorites.
     */
    @GET("user/me/radios")
    suspend fun getUserFavoriteRadios(): WholeList<RadioBrief>

    /**
     * Provides album recommendations tailored to the user's listening preferences.
     */
    @GET("user/me/recommendations/albums")
    suspend fun getUserRecommendedAlbums(): WholeList<AlbumBrief>

    /**
     * Returns newly released albums recommended specifically for the user.
     */
    @GET("user/me/recommendations/releases")
    suspend fun getUserRecommendedReleases(): WholeList<AlbumBrief>

    /**
     * Provides artist recommendations based on the user's listening history.
     */
    @GET("user/me/recommendations/artists")
    suspend fun getUserRecommendedArtists(): WholeList<ArtistBrief>

    /**
     * Returns playlist recommendations personalized for the user.
     */
    @GET("user/me/recommendations/playlists")
    suspend fun getUserRecommendedPlaylists(): WholeList<PlaylistBrief>

    /**
     * Provides track recommendations based on user preferences and listening patterns.
     */
    @GET("user/me/recommendations/tracks")
    suspend fun getUserRecommendedTracks(): WholeList<TrackBrief>

    /**
     * Returns radio station recommendations tailored to the user's taste.
     */
    @GET("user/me/recommendations/radios")
    suspend fun getUserRecommendedRadios(): WholeList<RadioBrief>

    /**
     * Retrieves all tracks that the user has marked as favorites.
     */
    @GET("user/me/tracks")
    suspend fun getUserFavoriteTracks(): WholeList<TrackBrief>

    /**
     * Modifies playlist information such as title and description.
     */
    @POST("playlist/{playlist_id}")
    @FormUrlEncoded
    suspend fun updatePlaylist(
        @Path("playlist_id") playlistId: Long,
        @Field("title") title: String? = null,
        @Field("description") description: String? = null
    )

    /**
     * Marks a playlist as viewed by the user.
     * Requires "basic_access" permissions.
     */
    @POST("playlist/{playlist_id}/seen")
    suspend fun markPlaylistAsSeen(@Path("playlist_id") playlistId: Long)

    /**
     * Adds one or more tracks to a specified playlist.
     * Requires "manage_library" permissions.
     */
    @POST("playlist/{playlist_id}/tracks")
    @FormUrlEncoded
    suspend fun addTracksToPlaylist(
        @Path("playlist_id") playlistId: Long,
        @Field("songs") trackIds: String
    )

    /**
     * Changes the order of tracks within a playlist.
     * Requires "manage_library" permissions.
     */
    @POST("playlist/{playlist_id}/tracks")
    @FormUrlEncoded
    suspend fun reorderPlaylistTracks(
        @Path("playlist_id") playlistId: Long,
        @Field("order") trackIds: String
    )


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
    )

    /**
     * Adds albums to the user's personal library.
     * Requires "manage_library" permissions.
     */
    @POST("user/{user_id}/albums")
    @FormUrlEncoded
    suspend fun addAlbumsToLibrary(
        @Path("user_id") userId: Long,
        @Field("album_id") albumId: Long
    )

    /**
     * Adds artists to the user's favorites list.
     * Requires "manage_library" permissions.
     */
    @POST("user/{user_id}/artists")
    @FormUrlEncoded
    suspend fun addArtistsToFavorites(
        @Path("user_id") userId: Long,
        @Field("artist_id") artistId: Long
    )

    /**
     * Follows another user on the platform.
     * Requires "manage_community" permissions.
     */
    @POST("user/{user_id}/followings")
    @FormUrlEncoded
    suspend fun followUser(
        @Path("user_id") userId: Long,
        @Field("user_id") targetUserId: Long
    )

    /**
     * Adds a notification to the user's activity feed.
     */
    @POST("user/{user_id}/notifications")
    @FormUrlEncoded
    suspend fun addNotification(
        @Path("user_id") userId: Long,
        @Field("message") message: String
    )

    /**
     * Creates a new playlist with the specified title.
     * Requires "manage_library" permissions.
     */
    @POST("user/{user_id}/playlists")
    @FormUrlEncoded
    suspend fun createPlaylist(
        @Path("user_id") userId: Long,
        @Field("title") title: String
    )

    /**
     * Adds playlists to the user's favorites.
     * Requires "manage_library" permissions.
     */
    @POST("user/{user_id}/playlists")
    @FormUrlEncoded
    suspend fun addPlaylistsToFavorites(
        @Path("user_id") userId: Long,
        @Field("playlist_id") playlistId: Long
    )

    /**
     * Adds a radio station to the user's favorites.
     * Requires "manage_library" permissions.
     */
    @POST("user/{user_id}/radios")
    @FormUrlEncoded
    suspend fun addRadioToFavorites(
        @Path("user_id") userId: Long,
        @Field("radio_id") radioId: Int
    )

    /**
     * Adds tracks to the user's favorites collection.
     * Requires "manage_library" permission.
     */
    @POST("user/{user_id}/tracks")
    @FormUrlEncoded
    suspend fun addTracksToFavorites(
        @Path("user_id") userId: Long,
        @Field("track_id") trackId: Long
    )

    /**
     * Deletes specified playlist.
     * Requires "delete_library" permission.
     */
    @DELETE("playlist/{playlist_id}")
    suspend fun deletePlaylist(
        @Path("playlist_id") playlistId: Long,
    )

    /**
     * Removes specified tracks from a playlist.
     * Requires "delete_library" permission.
     */
    @DELETE("playlist/{playlist_id}/tracks")
    suspend fun removeTracksFromPlaylist(
        @Path("playlist_id") playlistId: Long,
        @Query("songs") trackIds: String
    )

    /**
     * Removes albums from the user's personal library.
     * Requires "manage_library" and "delete_library" permissions.
     */
    @DELETE("user/{user_id}/albums")
    suspend fun removeAlbumsFromLibrary(
        @Path("user_id") userId: Long,
        @Query("album_id") albumId: Long
    )

    /**
     * Removes artists from the user's favorites list.
     * Requires "manage_library" and "delete_library" permissions.
     */
    @DELETE("user/{user_id}/artists")
    suspend fun removeArtistsFromFavorites(
        @Path("user_id") userId: Long,
        @Query("artist_id") artistId: Long
    )

    /**
     * Stops following a specific user on the platform.
     * Requires "manage_library" permission.
     */
    @DELETE("user/{user_id}/followings")
    suspend fun unfollowUser(
        @Path("user_id") userId: Long,
        @Query("user_id") targetUserId: Long
    )

    /**
     * Removes playlists from the user's favorites collection.
     * Requires "manage_library" and "delete_library" permissions.
     */
    @DELETE("user/{user_id}/playlists")
    suspend fun removePlaylistsFromFavorites(
        @Path("user_id") userId: Long,
        @Query("playlist_id") playlistId: Long
    )

    /**
     * Removes a radio station from the user's favorites.
     * Requires "manage_library" and "delete_library" permissions.
     */
    @DELETE("user/{user_id}/radios")
    suspend fun removeRadioFromFavorites(
        @Path("user_id") userId: Long,
        @Query("radio_id") radioId: Int
    )

    /**
     * Removes tracks from the user's favorites collection.
     * Requires delete and manage library permissions.
     */
    @DELETE("user/{user_id}/tracks")
    suspend fun removeTracksFromFavorites(
        @Path("user_id") userId: Long,
        @Query("track_id") trackId: Long
    )
}

private const val DZR_BASE_URL = "https://api.deezer.com/"