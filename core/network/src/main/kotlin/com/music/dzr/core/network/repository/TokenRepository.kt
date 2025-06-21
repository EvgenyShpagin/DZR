package com.music.dzr.core.network.repository

import com.music.dzr.core.network.model.NetworkError
import com.music.dzr.core.network.model.Token

interface TokenRepository {
    fun getAccessToken(): String?
    fun getRefreshToken(): String?
    fun onTokenUpdated(token: Token)
    fun onUpdateFailed(networkError: NetworkError)
}