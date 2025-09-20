package com.music.dzr.core.auth.data.local.error

import android.security.keystore.KeyPermanentlyInvalidatedException
import com.music.dzr.core.data.error.StorageError
import java.io.IOException
import java.security.GeneralSecurityException
import javax.crypto.AEADBadTagException
import javax.crypto.BadPaddingException

internal fun Throwable.toReadError(): StorageError = when (this) {
    is IOException, is IllegalStateException -> StorageError.ReadFailed(this)
    is NullPointerException -> StorageError.DataCorrupted(null)
    else -> StorageError.Unknown(this)
}

internal fun Throwable.toWriteError(): StorageError = when (this) {
    is IOException, is IllegalStateException -> StorageError.WriteFailed(this)
    else -> StorageError.Unknown(this)
}

internal fun Throwable.toCryptoError(): StorageError = when (this) {
    is IllegalArgumentException -> StorageError.DataCorrupted(this)
    is AEADBadTagException, is BadPaddingException -> AuthStorageError.IntegrityCheckFailed(this)
    is KeyPermanentlyInvalidatedException -> AuthStorageError.KeyInvalidated
    is GeneralSecurityException, is IllegalStateException -> AuthStorageError.CryptoFailure(this)
    else -> StorageError.Unknown(this)
}
