package com.music.dzr.core.data.mapper

import com.music.dzr.core.model.User as DomainUser
import com.music.dzr.core.network.dto.PublicUser as NetworkUser

fun NetworkUser.toDomain(): DomainUser {
    return DomainUser(
        id = id,
        displayName = displayName,
        followerCount = followers?.total ?: 0,
        images = images?.map { it.toDomain() } ?: emptyList(),
        webUrl = externalUrls.spotify
    )
}