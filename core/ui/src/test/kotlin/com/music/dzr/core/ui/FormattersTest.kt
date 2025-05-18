package com.music.dzr.core.ui

import junit.framework.TestCase.assertEquals
import org.junit.Test

class FormattersTest {

    @Test
    fun formatContributors_returnsOriginalName_whenProvidedSingle() {
        val author = "Author"
        val actual = formatContributors(listOf(author))
        assertEquals(author, actual)
    }

    @Test
    fun formatContributors_returnsStringWithCommas_whenProvidedMultiple() {
        val contributors = listOf("Contributor 1", "Contributor 2", "Contributor 3")
        val actual = formatContributors(contributors)
        val expected = "Contributor 1, Contributor 2, Contributor 3"
        assertEquals(expected, actual)
    }
}