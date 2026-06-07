package com.music.dzr.feature.auth.ui.di

import android.content.Intent
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.ui.platform.LocalContext
import androidx.core.net.toUri
import com.music.dzr.core.navigation.Navigator
import com.music.dzr.core.navigation.goBackToWithUpdate
import com.music.dzr.feature.auth.ui.navigation.AuthDestination
import com.music.dzr.feature.auth.ui.screen.permissions.PermissionsScreen
import com.music.dzr.feature.auth.ui.screen.permissions.PermissionsViewModel
import com.music.dzr.feature.auth.ui.screen.welcome.WelcomeScreen
import com.music.dzr.feature.auth.ui.screen.welcome.WelcomeViewModel
import org.koin.androidx.compose.koinViewModel
import org.koin.androidx.scope.dsl.activityRetainedScope
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import org.koin.dsl.navigation3.navigation


@OptIn(KoinExperimentalAPI::class)
val authModule = module {
    viewModelOf(::WelcomeViewModel)
    viewModelOf(::PermissionsViewModel)

    activityRetainedScope {
        navigation<AuthDestination.Welcome> {
            val context = LocalContext.current
            WelcomeScreen(
                viewModel = koinViewModel(),
                windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass,
                onRestrictAccess = {
                    val navigator = get<Navigator>()
                    navigator.goTo(
                        AuthDestination.Permissions(it.grantedPermissions)
                    )
                },
                onLogin = { url ->
                    val intent = Intent(Intent.ACTION_VIEW, url.toUri())
                    context.startActivity(intent)
                },
                // TODO: where to put modifier?
            )
        }

        navigation<AuthDestination.Permissions> {
            PermissionsScreen(
                viewModel = koinViewModel(),
                onPermissionsSave = {
                    val navigator = get<Navigator>()
                    navigator.goBackToWithUpdate(
                        AuthDestination.Welcome(it.grantedPermissions)
                    )
                },
                onDismiss = {
                    val navigator = get<Navigator>()
                    navigator.goBack()
                }
            )
        }
    }
}
