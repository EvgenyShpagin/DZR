package com.music.dzr.core.oauth.model

import com.music.dzr.core.oauth.model.OAuthScope.Companion.join
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class OAuthScopeTest {
    @Test
    fun createsValidScope() {
        val scope = OAuthScope("user-read-private")
        assertEquals("user-read-private", scope.value)
    }

    @Test
    fun throwsException_whenBlankScope() {
        val exception = assertFailsWith<IllegalArgumentException> {
            OAuthScope("")
        }
        assertEquals("Scope value cannot be blank", exception.message)
    }

    @Test
    fun throwsException_whenScopeWithInvalidCharacters() {
        assertFailsWith<IllegalArgumentException> {
            OAuthScope("user read")
        }
        assertFailsWith<IllegalArgumentException> {
            OAuthScope("user\"read")
        }
        assertFailsWith<IllegalArgumentException> {
            OAuthScope("user\\read")
        }
    }

    @Test
    fun parsesAndJoinsScopesCorrectly() {
        val original = "user-read-private user-read-email"
        val scopes = OAuthScope.parse(original)
        val result = scopes.join()
        assertEquals(original, result)
    }
}