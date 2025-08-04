package com.music.dzr.core.auth.data.mapper

import com.music.dzr.core.auth.domain.model.AuthScope
import com.music.dzr.core.auth.data.remote.dto.AuthToken as DataToken
import com.music.dzr.core.auth.domain.model.AuthToken as DomainToken

internal fun DataToken.toDomain(): DomainToken {
    return DomainToken(
        accessToken = accessToken,
        tokenType = tokenType,
        expiresInSeconds = expiresIn,
        refreshToken = refreshToken,
        scopes = AuthScope.parse(scope)
    )
}
