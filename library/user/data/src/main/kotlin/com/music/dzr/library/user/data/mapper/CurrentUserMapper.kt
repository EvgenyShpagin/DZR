package com.music.dzr.library.user.data.mapper

import com.music.dzr.core.data.mapper.toDomain
import com.music.dzr.library.user.domain.model.ExplicitContentSettings
import com.music.dzr.library.user.data.remote.dto.CurrentUser as NetworkCurrentUser
import com.music.dzr.library.user.data.remote.dto.SubscriptionLevel as NetworkSubscriptionLevel
import com.music.dzr.library.user.domain.model.CurrentUser as DomainCurrentUser
import com.music.dzr.library.user.domain.model.SubscriptionLevel as DomainSubscriptionLevel

internal fun NetworkCurrentUser.toDomain(): DomainCurrentUser {
    return DomainCurrentUser(
        id = id,
        displayName = displayName,
        followerCount = followers.total,
        images = images.map { it.toDomain() },
        webUrl = externalUrls.spotify,
        country = country,
        email = email,
        explicitContentSettings = ExplicitContentSettings(
            filterEnabled = explicitContent.filterEnabled,
            filterLocked = explicitContent.filterLocked
        ),
        subscriptionLevel = product.toDomain(),
    )
}

private fun NetworkSubscriptionLevel?.toDomain(): DomainSubscriptionLevel {
    val level = this ?: return DomainSubscriptionLevel.Free
    return when (level) {
        NetworkSubscriptionLevel.Premium -> DomainSubscriptionLevel.Premium
        else -> DomainSubscriptionLevel.Free
    }
}