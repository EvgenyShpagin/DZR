package com.music.dzr.feature.auth.ui.navigation

import com.music.dzr.core.auth.domain.model.PermissionScope
import kotlinx.serialization.Serializable

sealed interface AuthDestination {
    @Serializable
    data class Welcome(
        val grantedPermissions: Set<PermissionScope> = emptySet()
    ) : AuthDestination

    @Serializable
    data class Permissions(
        val grantedPermissions: Set<PermissionScope> = emptySet()
    ) : AuthDestination
}
