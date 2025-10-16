package com.music.dzr.core.auth.data.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.Serializer
import androidx.datastore.dataStoreFile
import com.music.dzr.core.auth.data.local.model.AuthSession
import com.music.dzr.core.auth.data.local.model.AuthSessionSerializer
import com.music.dzr.core.auth.data.local.model.AuthToken
import com.music.dzr.core.auth.data.local.model.AuthTokenSerializer
import com.music.dzr.core.auth.data.local.security.Encryptor
import com.music.dzr.core.auth.data.local.security.KeystoreEncryptor
import com.music.dzr.core.auth.data.local.source.AuthSessionLocalDataSource
import com.music.dzr.core.auth.data.local.source.AuthSessionLocalDataSourceImpl
import com.music.dzr.core.auth.data.local.source.AuthTokenLocalDataSource
import com.music.dzr.core.auth.data.local.source.AuthTokenLocalDataSourceImpl
import com.music.dzr.core.auth.data.remote.api.AuthApi
import com.music.dzr.core.auth.data.remote.http.AuthInterceptor
import com.music.dzr.core.auth.data.remote.http.AuthTokenAuthenticator
import com.music.dzr.core.auth.data.remote.oauth.AuthorizationUrlBuilder
import com.music.dzr.core.auth.data.remote.oauth.OAuthSecurityProvider
import com.music.dzr.core.auth.data.remote.oauth.OAuthSecurityProviderImpl
import com.music.dzr.core.auth.data.remote.source.AuthTokenRemoteDataSource
import com.music.dzr.core.auth.data.remote.source.AuthTokenRemoteDataSourceImpl
import com.music.dzr.core.coroutine.ApplicationScope
import com.music.dzr.core.coroutine.DispatcherProvider
import com.music.dzr.core.network.di.AuthInterceptorQualifier
import com.music.dzr.core.network.di.AuthenticatorQualifier
import com.music.dzr.core.network.di.JsonConverterFactoryQualifier
import com.music.dzr.core.network.di.NetworkResponseCallAdapterFactoryQualifier
import kotlinx.coroutines.CoroutineScope
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.qualifier.named
import org.koin.core.scope.Scope
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.create
import com.music.dzr.core.auth.data.BuildConfig as AuthBuildConfig
import com.music.dzr.core.network.BuildConfig as NetworkBuildConfig

private const val AUTH_TOKEN_DS_FILE = "auth_token.pb"
private const val AUTH_SESSION_DS_FILE = "auth_session.pb"
private const val AUTH_TOKEN_KEY_ALIAS = "auth_token_master_key"
private const val AUTH_SESSION_KEY_ALIAS = "auth_session_master_key"

private val AuthTokenEncryptorQualifier = named("AuthTokenEncryptor")
private val AuthSessionEncryptorQualifier = named("AuthSessionEncryptor")
private val AuthRetrofitQualifier = named("AuthRetrofit")
private val AuthClientQualifier = named("AuthOkHttpClient")

val authModule = module {
    single(AuthInterceptorQualifier) { AuthInterceptor(get()) }

    single(AuthClientQualifier) {
        OkHttpClient.Builder()
            .addInterceptor(get<HttpLoggingInterceptor>())
            .build()
    }

    single(AuthenticatorQualifier) {
        AuthTokenAuthenticator(tokenRepository = get())
    }

    single(AuthRetrofitQualifier) {
        Retrofit.Builder()
            .baseUrl(AuthBuildConfig.SPOTIFY_AUTH_BASE_URL)
            .client(get(AuthClientQualifier))
            .addConverterFactory(get(JsonConverterFactoryQualifier))
            .addCallAdapterFactory(get(NetworkResponseCallAdapterFactoryQualifier))
            .build()
    }

    single {
        AuthorizationUrlBuilder(
            clientId = NetworkBuildConfig.SPOTIFY_CLIENT_ID,
            authBaseUrl = AuthBuildConfig.SPOTIFY_AUTH_BASE_URL,
        )
    }

    single<AuthApi> {
        get<Retrofit>(AuthRetrofitQualifier).create()
    }

    single<AuthTokenRemoteDataSource> {
        AuthTokenRemoteDataSourceImpl(authApi = get())
    }

    single<OAuthSecurityProvider> { OAuthSecurityProviderImpl() }

    single<Encryptor>(AuthTokenEncryptorQualifier) {
        KeystoreEncryptor(AUTH_TOKEN_KEY_ALIAS)
    }

    single<Encryptor>(AuthSessionEncryptorQualifier) {
        KeystoreEncryptor(AUTH_SESSION_KEY_ALIAS)
    }

    single<AuthTokenSerializer> {
        AuthTokenSerializer(encryptor = get())
    }

    single<DataStore<AuthToken>> {
        createDataStore(filename = AUTH_TOKEN_DS_FILE)
    }

    single<AuthTokenLocalDataSource> {
        AuthTokenLocalDataSourceImpl(dataStore = get())
    }

    single<AuthSessionSerializer> {
        AuthSessionSerializer(encryptor = get())
    }

    single<DataStore<AuthSession>> {
        createDataStore(filename = AUTH_SESSION_DS_FILE)
    }

    single<AuthSessionLocalDataSource> {
        AuthSessionLocalDataSourceImpl(dataStore = get())
    }
}

private inline fun <T, reified S : Serializer<T>> Scope.createDataStore(
    filename: String
): DataStore<T> {
    val context: Context = get()
    val ioDispatcher = get<DispatcherProvider>().io
    val serializer = get<S>()
    val scope = get<ApplicationScope>()

    return DataStoreFactory.create(
        serializer = serializer,
        scope = CoroutineScope(scope.coroutineContext + ioDispatcher),
        produceFile = { context.dataStoreFile(filename) }
    )
}