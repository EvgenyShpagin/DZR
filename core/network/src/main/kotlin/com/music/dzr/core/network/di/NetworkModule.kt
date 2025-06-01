package com.music.dzr.core.network.di

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
import com.music.dzr.core.network.retrofit.NetworkErrorResponseParser
import com.music.dzr.core.network.retrofit.NetworkResponseCallAdapterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

private const val DZR_BASE_URL = "https://api.deezer.com/"

val networkModule = module {

    single {
        Json { ignoreUnknownKeys = true }
    }

    single {
        NetworkErrorResponseParser(get())
    }

    single {
        Retrofit.Builder()
            .baseUrl(DZR_BASE_URL)
            .addConverterFactory(
                get<Json>().asConverterFactory("application/json".toMediaType())
            )
            .addCallAdapterFactory(NetworkResponseCallAdapterFactory(get()))
            .build()
    }

    single<AlbumApi> { get<Retrofit>().create(AlbumApi::class.java) }

    single<ArtistApi> { get<Retrofit>().create(ArtistApi::class.java) }

    single<ChartApi> { get<Retrofit>().create(ChartApi::class.java) }

    single<EditorialApi> { get<Retrofit>().create(EditorialApi::class.java) }

    single<GenreApi> { get<Retrofit>().create(GenreApi::class.java) }

    single<PlaylistApi> { get<Retrofit>().create(PlaylistApi::class.java) }

    single<RadioApi> { get<Retrofit>().create(RadioApi::class.java) }

    single<SearchApi> { get<Retrofit>().create(SearchApi::class.java) }

    single<TrackApi> { get<Retrofit>().create(TrackApi::class.java) }

    single<UserApi> { get<Retrofit>().create(UserApi::class.java) }

}