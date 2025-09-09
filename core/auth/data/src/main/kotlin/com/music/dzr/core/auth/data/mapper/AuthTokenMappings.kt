package com.music.dzr.core.auth.data.mapper

import com.music.dzr.core.auth.domain.model.AuthScope
import com.music.dzr.core.auth.domain.model.AuthScope.Companion.join
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
