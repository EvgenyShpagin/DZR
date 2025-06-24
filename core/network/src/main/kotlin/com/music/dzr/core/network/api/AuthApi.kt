package com.music.dzr.core.network.api

import com.music.dzr.core.network.model.NetworkResponse
import com.music.dzr.core.network.model.Token
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

/**
 * The API for handling authorization with the Spotify accounts service using PKCE.
 */
internal interface AuthApi {

    /**
     * Exchanges an authorization code for an access token.
     * This is part of the Authorization Code Flow with PKCE.
     */
    @FormUrlEncoded
    @POST("api/token")
    suspend fun getToken(
        @Field("grant_type") grantType: String = "authorization_code",
        @Field("code") code: String,
        @Field("redirect_uri") redirectUri: String,
        @Field("client_id") clientId: String,
        @Field("code_verifier") codeVerifier: String
    ): NetworkResponse<Token>

    /**
     * Refreshes an expired access token using a refresh token.
     */
    @FormUrlEncoded
    @POST("api/token")
    suspend fun refreshToken(
        @Field("grant_type") grantType: String = "refresh_token",
        @Field("refresh_token") refreshToken: String,
        @Field("client_id") clientId: String
    ): NetworkResponse<Token>
}