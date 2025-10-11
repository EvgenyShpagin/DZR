package com.music.dzr.core.auth.data.mapper

import com.music.dzr.core.auth.data.local.model.authToken
import com.music.dzr.core.auth.domain.model.AuthScope
import com.music.dzr.core.auth.domain.model.AuthScope.Companion.join
import com.music.dzr.core.auth.data.local.model.AuthToken as LocalToken
import com.music.dzr.core.auth.data.remote.dto.AuthToken as NetworkToken
import com.music.dzr.core.auth.domain.model.AuthToken as DomainToken

internal fun NetworkToken.toDomain(): DomainToken {
    return DomainToken(
        accessToken = accessToken,
        tokenType = tokenType,
        expiresInSeconds = expiresIn,
        refreshToken = refreshToken,
        scopes = scope?.let { AuthScope.parse(it) } ?: emptyList()
    )
}

internal fun DomainToken.toNetwork(): NetworkToken {
    return NetworkToken(
        accessToken = accessToken,
        tokenType = tokenType,
        expiresIn = expiresInSeconds,
        refreshToken = refreshToken,
        scope = scopes.join()
    )
}

internal fun LocalToken.toDomain(): DomainToken {
    val refresh = if (this.hasRefreshToken()) this.refreshToken else null
    val scopes = if (this.hasScope()) AuthScope.parse(this.scope) else emptyList()
    return DomainToken(
        accessToken = this.accessToken,
        tokenType = this.tokenType,
        expiresInSeconds = this.expiresIn,
        refreshToken = refresh,
        scopes = scopes
    )
}

internal fun DomainToken.toLocal(): LocalToken = authToken {
    val domain = this@toLocal
    accessToken = domain.accessToken
    tokenType = domain.tokenType
    expiresIn = domain.expiresInSeconds

    domain.refreshToken?.let { refreshToken = it }

    if (domain.scopes.isNotEmpty()) {
        scope = domain.scopes.join()
    }
}
