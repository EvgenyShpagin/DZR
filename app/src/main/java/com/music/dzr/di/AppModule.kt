package com.music.dzr.di

import com.music.dzr.BuildConfig
import com.music.dzr.core.auth.domain.model.AuthConfig
import com.music.dzr.core.navigation.Navigator
import com.music.dzr.feature.auth.ui.navigation.AuthDestination
import org.koin.androidx.scope.dsl.activityRetainedScope
import org.koin.dsl.module

val appModule = module {
    single {
        AuthConfig(
            clientId = BuildConfig.SPOTIFY_CLIENT_ID,
            redirectUri = "TODO: not implemented"
        )
    }

    activityRetainedScope {
        scoped {
            Navigator(startDestination = AuthDestination.Welcome())
        }
    }
}
