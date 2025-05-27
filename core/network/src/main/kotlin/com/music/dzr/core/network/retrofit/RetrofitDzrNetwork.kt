package com.music.dzr.core.network.retrofit

import com.music.dzr.core.network.api.AlbumApi
import com.music.dzr.core.network.api.ArtistApi
import com.music.dzr.core.network.api.ChartApi
import com.music.dzr.core.network.api.EditorialApi
import com.music.dzr.core.network.api.GenreApi
import com.music.dzr.core.network.api.PlaylistApi
import com.music.dzr.core.network.api.RadioApi
import com.music.dzr.core.network.api.SearchApi
import com.music.dzr.core.network.api.TrackApi
import com.music.dzr.core.network.api.UserApi


/**
 * Retrofit API interface for Deezer API endpoints.
 *
 * This interface defines all available endpoints for interacting with the Deezer API,
 * including album, artist, chart, editorial, genre, playlist, radio, search, track,
 * and user endpoints with both read and write operations.
 *
 * @see <a href="https://developers.deezer.com/api">Deezer API Documentation</a>
 */
private interface RetrofitDzrNetworkApi : AlbumApi, ArtistApi, ChartApi,
    EditorialApi, GenreApi, PlaylistApi, RadioApi, SearchApi, TrackApi, UserApi

private const val DZR_BASE_URL = "https://api.deezer.com/"
