package com.chililabs.giphysearch.data.repository

import androidx.paging.PagingSource
import com.chililabs.giphysearch.data.paging.GiphyPagingSource
import com.chililabs.giphysearch.data.remote.GiphyApi
import com.chililabs.giphysearch.data.remote.dto.*
import com.chililabs.giphysearch.domain.model.Gif
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class GiphyRepositoryTest {

    private lateinit var api: GiphyApi
    private lateinit var repository: GiphyRepositoryImpl

    @Before
    fun setup() {
        api = mockk()
        repository = GiphyRepositoryImpl(api)
    }

    @Test
    fun `searchGifs returns paging flow`() = runTest {
        val flow = repository.searchGifs("test")
        assertTrue(flow != null)
    }
}

class GiphyPagingSourceTest {

    private lateinit var api: GiphyApi
    private lateinit var pagingSource: GiphyPagingSource

    @Before
    fun setup() {
        api = mockk()
        pagingSource = GiphyPagingSource(api, "test")
    }

    @Test
    fun `load returns page when successful`() = runTest {
        val mockResponse = GiphyResponse(
            data = listOf(
                GifDto(
                    id = "1",
                    title = "Test GIF",
                    username = "testuser",
                    rating = "g",
                    images = Images(
                        fixedWidth = ImageData("url1", "200", "200"),
                        original = ImageData("url2", "400", "400")
                    ),
                    importDatetime = "2024-01-01"
                )
            ),
            pagination = Pagination(totalCount = 100, count = 1, offset = 0)
        )

        coEvery {
            api.searchGifs(any(), any(), any(), any(), any(), any())
        } returns mockResponse

        val result = pagingSource.load(
            PagingSource.LoadParams.Refresh(
                key = 0,
                loadSize = 25,
                placeholdersEnabled = false
            )
        )

        assertTrue(result is PagingSource.LoadResult.Page)
        val page = result as PagingSource.LoadResult.Page<Int, Gif>
        assertEquals(1, page.data.size)
        assertEquals("1", page.data[0].id)
        assertEquals("Test GIF", page.data[0].title)
    }

    @Test
    fun `load returns error on exception`() = runTest {
        coEvery {
            api.searchGifs(any(), any(), any(), any(), any(), any())
        } throws Exception("Network error")

        val result = pagingSource.load(
            PagingSource.LoadParams.Refresh(
                key = 0,
                loadSize = 25,
                placeholdersEnabled = false
            )
        )

        assertTrue(result is PagingSource.LoadResult.Error)
    }

    @Test
    fun `load with empty query calls trending endpoint`() = runTest {
        val emptyQueryPagingSource = GiphyPagingSource(api, "")

        val mockResponse = GiphyResponse(
            data = listOf(),
            pagination = Pagination(totalCount = 0, count = 0, offset = 0)
        )

        coEvery {
            api.getTrendingGifs(any(), any(), any(), any())
        } returns mockResponse

        val result = emptyQueryPagingSource.load(
            PagingSource.LoadParams.Refresh(
                key = 0,
                loadSize = 25,
                placeholdersEnabled = false
            )
        )

        assertTrue(result is PagingSource.LoadResult.Page)
    }
}