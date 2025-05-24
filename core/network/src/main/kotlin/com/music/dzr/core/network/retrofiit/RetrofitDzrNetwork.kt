package com.music.dzr.core.network.retrofiit

import com.music.dzr.core.network.model.Album
import com.music.dzr.core.network.model.AlbumTrack
import com.music.dzr.core.network.model.Artist
import com.music.dzr.core.network.model.ArtistAlbum
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
import com.music.dzr.core.network.model.Genre
import com.music.dzr.core.network.model.GenreArtist
import com.music.dzr.core.network.model.PaginatedList
import com.music.dzr.core.network.model.Playlist
import com.music.dzr.core.network.model.PlaylistTrack
import com.music.dzr.core.network.model.Radio
import com.music.dzr.core.network.model.RadioBrief
import com.music.dzr.core.network.model.RadioTrackBrief
import com.music.dzr.core.network.model.TrackBrief
import com.music.dzr.core.network.model.WholeList
import retrofit2.http.GET
import retrofit2.http.Path


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
     *
     * @param albumId The unique identifier of the album
     * @return An [Album] object containing album details
     */
    @GET("album/{id}")
    suspend fun getAlbum(@Path("id") albumId: Long): Album

    /**
     * Retrieves all tracks from a specific album.
     *
     * @param albumId The unique identifier of the album
     * @return A paginated list of [AlbumTrack] objects representing the album's tracks
     */
    @GET("album/{id}/tracks")
    suspend fun getAlbumTracks(@Path("id") albumId: Long): PaginatedList<AlbumTrack>

    // ========== ARTIST ENDPOINTS ========== //

    /**
     * Retrieves detailed information about a specific artist.
     *
     * @param artistId The unique identifier of the artist
     * @return A [Artist] object containing artist details
     */
    @GET("artist/{id}")
    suspend fun getArtist(@Path("id") artistId: Long): Artist

    /**
     * Retrieves the top 5 tracks of an artist.
     *
     * @param artistId The unique identifier of the artist
     * @return A paginated list of the artist's most popular [ArtistTopTrack] objects
     */
    @GET("artist/{id}/top")
    suspend fun getArtistTopTracks(@Path("id") artistId: Long): PaginatedList<ArtistTopTrack>

    /**
     * Retrieves all albums by a specific artist.
     *
     * @param artistId The unique identifier of the artist
     * @return A paginated list of [ArtistAlbum] objects representing the artist's albums
     */
    @GET("artist/{id}/albums")
    suspend fun getArtistAlbums(@Path("id") artistId: Long): PaginatedList<ArtistAlbum>

    /**
     * Retrieves artists related to the specified artist.
     *
     * @param artistId The unique identifier of the artist
     * @return A paginated list of related [Artist] objects
     */
    @GET("artist/{id}/related")
    suspend fun getRelatedArtists(@Path("id") artistId: Long): PaginatedList<Artist>

    /**
     * Retrieves radio tracks based on an artist's style.
     *
     * @param artistId The unique identifier of the artist
     * @return A list of [TrackBrief] objects for the artist's radio
     */
    @GET("artist/{id}/radio")
    suspend fun getArtistRadioTracks(@Path("id") artistId: Long): WholeList<TrackBrief>

    /**
     * Retrieves playlists created by or featuring the artist.
     *
     * @param artistId The unique identifier of the artist
     * @return A paginated list of [ArtistPlaylist] objects associated with the artist
     */
    @GET("artist/{id}/playlists")
    suspend fun getArtistPlaylists(@Path("id") artistId: Long): PaginatedList<ArtistPlaylist>

    // ========== CHART ENDPOINTS ==========

    /**
     * Retrieves general charts for a specified genre.
     *
     * @return A [Chart] object containing various chart data
     */
    @GET("chart")
    suspend fun getCharts(): Chart

    /**
     * Retrieves the top tracks from charts.
     *
     * @param genreId The genre identifier (default: 0 for all genres)
     * @return A paginated list of top [ChartTrack] objects
     */
    @GET("chart/{genre_id}/tracks")
    suspend fun getTopTracks(@Path("genre_id") genreId: Int = 0): PaginatedList<ChartTrack>

    /**
     * Retrieves the top albums from charts.
     *
     * @param genreId The genre identifier (default: 0 for all genres)
     * @return A paginated list of top [ChartAlbum] objects
     */
    @GET("chart/{genre_id}/albums")
    suspend fun getTopAlbums(@Path("genre_id") genreId: Int = 0): PaginatedList<ChartAlbum>

    /**
     * Retrieves the top artists from charts.
     *
     * @param genreId The genre identifier (default: 0 for all genres)
     * @return A paginated list of top [ChartArtist] objects
     */
    @GET("chart/{genre_id}/artists")
    suspend fun getTopArtists(@Path("genre_id") genreId: Int = 0): PaginatedList<ChartArtist>

    /**
     * Retrieves the top playlists from charts.
     *
     * @param genreId The genre identifier (default: 0 for all genres)
     * @return A paginated list of top [ChartPlaylist] objects
     */
    @GET("chart/{genre_id}/playlists")
    suspend fun getTopPlaylists(@Path("genre_id") genreId: Int = 0): PaginatedList<ChartPlaylist>

    // ========== EDITORIAL ENDPOINTS ==========

    /**
     * Retrieves all available editorial objects.
     *
     * @return A paginated list of [Editorial] objects
     */
    @GET("editorial")
    suspend fun getEditorials(): PaginatedList<Editorial>

    /**
     * Retrieves detailed information about a specific editorial.
     *
     * @param editorialId The unique identifier of the editorial (same as genre id)
     * @return A [Editorial] object containing editorial details
     */
    @GET("editorial/{id}")
    suspend fun getEditorial(@Path("id") editorialId: Int): Editorial

    /**
     * Retrieves albums selected weekly by the Deezer Team.
     *
     * @param editorialId The unique identifier of the editorial (same as genre id)
     * @return A list of [EditorialSelectionAlbum] objects representing the weekly selection
     */
    @GET("editorial/{id}/selection")
    suspend fun getEditorialSelection(@Path("id") editorialId: Int): WholeList<EditorialSelectionAlbum>

    /**
     * Retrieves new releases per genre for the current country.
     *
     * @param editorialId The unique identifier of the editorial (same as genre id)
     * @return A paginated list of [EditorialReleasesAlbum] objects representing new releases
     */
    @GET("editorial/{id}/releases")
    suspend fun getEditorialReleases(@Path("id") editorialId: Int): PaginatedList<EditorialReleasesAlbum>

    // ========== GENRE ENDPOINTS ==========

    /**
     * Retrieves all available music genres.
     *
     * @return A list of [Genre] objects
     */
    @GET("genre")
    suspend fun getGenres(): WholeList<Genre>

    /**
     * Retrieves detailed information about a specific genre.
     *
     * @param genreId The unique identifier of the genre
     * @return A [Genre] object containing genre details
     */
    @GET("genre/{id}")
    suspend fun getGenre(@Path("id") genreId: Int): Genre

    /**
     * Retrieves all artists associated with a specific genre.
     *
     * @param genreId The unique identifier of the genre
     * @return A list of [GenreArtist] objects from the specified genre
     */
    @GET("genre/{id}/artists")
    suspend fun getGenreArtists(@Path("id") genreId: Int): WholeList<GenreArtist>

    /**
     * Retrieves all radios associated with a specific genre.
     *
     * @param genreId The unique identifier of the genre
     * @return A list of [RadioBrief] objects from the specified genre
     */
    @GET("genre/{id}/radios")
    suspend fun getGenreRadios(@Path("id") genreId: Int): WholeList<RadioBrief>

    // ========== PLAYLIST ENDPOINTS ==========

    /**
     * Retrieves detailed information about a specific playlist.
     *
     * @param playlistId The unique identifier of the playlist
     * @return A [Playlist] object containing playlist details
     */
    @GET("playlist/{id}")
    suspend fun getPlaylist(@Path("id") playlistId: Long): Playlist

    /**
     * Retrieves all tracks from a specific playlist.
     *
     * @param playlistId The unique identifier of the playlist
     * @return A paginated list of [PlaylistTrack] objects representing the playlist's tracks
     */
    @GET("playlist/{id}/tracks")
    suspend fun getPlaylistTracks(@Path("id") playlistId: Long): PaginatedList<PlaylistTrack>

    // ========== RADIO ENDPOINTS ==========

    /**
     * Retrieves all available radio stations.
     *
     * @return A paginated list of [RadioBrief] objects
     */
    @GET("radio")
    suspend fun getRadios(): PaginatedList<RadioBrief>

    /**
     * Retrieves detailed information about a specific radio station.
     *
     * @param radioId The unique identifier of the radio station
     * @return A [Radio] object containing radio details
     */
    @GET("radio/{id}")
    suspend fun getRadio(@Path("id") radioId: Int): Radio

    /**
     * Retrieves radio stations organized by genre.
     *
     * @return A paginated list of [RadioBrief] objects grouped by genre
     */
    @GET("radio/genres")
    suspend fun getRadioGenres(): PaginatedList<RadioBrief>

    /**
     * Retrieves the top radio stations (default: 25 radios).
     *
     * @return A list of top [RadioBrief] objects
     */
    @GET("radio/top")
    suspend fun getTopRadios(): PaginatedList<RadioBrief>

    /**
     * Retrieves the first 40 tracks from a specific radio station.
     *
     * @param radioId The unique identifier of the radio station
     * @return A paginated list of [RadioTrackBrief] objects from the radio
     */
    @GET("radio/{id}/tracks")
    suspend fun getRadioTracks(@Path("id") radioId: Int): PaginatedList<RadioTrackBrief>

    /**
     * Retrieves personal radio lists organized by genre (similar to MIX on website).
     *
     * @return A paginated list of personalized [RadioBrief] objects
     */
    @GET("radio/lists")
    suspend fun getRadioLists(): PaginatedList<RadioBrief>

}

private const val DZR_BASE_URL = "https://api.deezer.com/"