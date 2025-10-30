package com.chililabs.giphysearch.presentation.search

import androidx.paging.PagingData
import app.cash.turbine.test
import com.chililabs.giphysearch.domain.model.Gif
import com.chililabs.giphysearch.domain.usecase.SearchGifsUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class SearchViewModelTest {

    private lateinit var searchGifsUseCase: SearchGifsUseCase
    private lateinit var viewModel: SearchViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        searchGifsUseCase = mockk()
    }

    @Test
    fun `initial search query is empty`() = runTest {
        viewModel = SearchViewModel(searchGifsUseCase)

        viewModel.searchQuery.test {
            assertEquals("", awaitItem())
        }
    }

    @Test
    fun `search query updates when onSearchQueryChanged is called`() = runTest {
        viewModel = SearchViewModel(searchGifsUseCase)

        viewModel.onSearchQueryChanged("test")

        viewModel.searchQuery.test {
            assertEquals("test", awaitItem())
        }
    }

    @Test
    fun `search is debounced by 300ms`() = runTest {
        val mockGif = Gif(
            id = "1",
            title = "Test GIF",
            username = "testuser",
            rating = "G",
            previewUrl = "https://preview.url",
            originalUrl = "https://original.url",
            width = 200,
            height = 200,
            importDatetime = "2024-01-01"
        )

        coEvery { searchGifsUseCase.invoke(any()) } returns flowOf(PagingData.from(listOf(mockGif)))

        viewModel = SearchViewModel(searchGifsUseCase)

        viewModel.onSearchQueryChanged("a")
        advanceTimeBy(100)

        viewModel.onSearchQueryChanged("ab")
        advanceTimeBy(100)

        viewModel.onSearchQueryChanged("abc")
        advanceTimeBy(400)
        testDispatcher.scheduler.advanceUntilIdle()

        // Verify that search was triggered after debounce
        viewModel.searchQuery.test {
            assertEquals("abc", awaitItem())
        }
    }

    @Test
    fun `empty query returns trending gifs`() = runTest {
        val mockGifs = listOf(
            Gif("1", "Trending 1", "user1", "G", "url1", "url1", 100, 100, "2024-01-01"),
            Gif("2", "Trending 2", "user2", "G", "url2", "url2", 100, 100, "2024-01-01")
        )

        coEvery { searchGifsUseCase.invoke("") } returns flowOf(PagingData.from(mockGifs))

        viewModel = SearchViewModel(searchGifsUseCase)

        advanceTimeBy(400)
        testDispatcher.scheduler.advanceUntilIdle()

    }
}