package com.music.dzr.core.network.api

import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class BrowseCategoryApiTest {

    private lateinit var server: MockWebServer
    private lateinit var api: BrowseCategoryApi

    @Before
    fun setUp() {
        server = MockWebServer().apply { start() }
        api = createApi(server.url("/"))
    }

    @After
    fun tearDown() {
        server.shutdown()
    }

    @Test
    fun getMultipleBrowseCategories_returnsData_onRequest() = runTest {
        // Arrange
        server.enqueueResponseFromAssets("responses/browse-category/browse-categories.json")

        // Act
        val response = api.getMultipleBrowseCategories()

        // Assert
        assertNull(response.error)
        assertNotNull(response.data)
        with(response.data!!.categories) {
            assertEquals(20, items.count())
            assertEquals("Made For You", items.first().name)
            assertEquals(64, total)
        }
    }

    @Test
    fun getMultipleBrowseCategories_usesCorrectPathAndMethod_onRequestWithAllParams() = runTest {
        // Arrange
        server.enqueueResponseFromAssets("responses/browse-category/browse-categories.json")
        val locale = "es_MX"
        val limit = 10
        val offset = 5

        // Act
        api.getMultipleBrowseCategories(locale = locale, limit = limit, offset = offset)

        // Assert Request
        val recorded = server.takeRequest()
        val expectedPath = "/browse/categories?locale=$locale&limit=$limit&offset=$offset"
        assertEquals(expectedPath, recorded.path)
        assertEquals("GET", recorded.method)
    }

    @Test
    fun getSingleBrowseCategory_returnsData_onRequest() = runTest {
        // Arrange
        server.enqueueResponseFromAssets("responses/browse-category/browse-single-category.json")

        // Act
        val response = api.getSingleBrowseCategory("dummy id")

        // Assert
        assertNull(response.error)
        assertNotNull(response.data)
        with(response.data!!) {
            assertEquals("Made For You", name)
        }
    }

    @Test
    fun getSingleBrowseCategory_usesCorrectPathAndMethod_onRequestWithLocale() = runTest {
        // Arrange
        server.enqueueResponseFromAssets("responses/browse-category/browse-single-category.json")
        val categoryId = "pop"
        val locale = "fr_FR"

        // Act
        api.getSingleBrowseCategory(id = categoryId, locale = locale)

        // Assert Request
        val recorded = server.takeRequest()
        assertEquals("/browse/categories/$categoryId?locale=$locale", recorded.path)
        assertEquals("GET", recorded.method)
    }
} 