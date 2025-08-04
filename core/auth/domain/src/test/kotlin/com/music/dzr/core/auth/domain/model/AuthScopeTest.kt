package com.music.dzr.core.auth.domain.model

import com.music.dzr.core.auth.domain.model.AuthScope.Companion.join
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class AuthScopeTest {
    @Test
    fun createsValidScope() {
        val scope = AuthScope("user-read-private")
        assertEquals("user-read-private", scope.value)
    }

    @Test
    fun throwsException_whenBlankScope() {
        val exception = assertFailsWith<IllegalArgumentException> {
            AuthScope("")
        }
        assertEquals("Scope value cannot be blank", exception.message)
    }

    @Test
    fun throwsException_whenScopeWithInvalidCharacters() {
        assertFailsWith<IllegalArgumentException> {
            AuthScope("user read")
        }
        assertFailsWith<IllegalArgumentException> {
            AuthScope("user\"read")
        }
        assertFailsWith<IllegalArgumentException> {
            AuthScope("user\\read")
        }
    }

    @Test
    fun parsesAndJoinsScopesCorrectly() {
        val original = "user-read-private user-read-email"
        val scopes = AuthScope.parse(original)
        val result = scopes.join()
        assertEquals(original, result)
    }
}