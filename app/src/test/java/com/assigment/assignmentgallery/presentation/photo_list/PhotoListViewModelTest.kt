package com.assigment.assignmentgallery.presentation.photo_list

import com.assigment.assignmentgallery.common.Resource
import com.assigment.assignmentgallery.domain.model.Photo
import com.assigment.assignmentgallery.domain.usecase.GetPhotosUseCase
import com.assigment.assignmentgallery.util.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Rule
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test


@OptIn(ExperimentalCoroutinesApi::class)
class PhotoListViewModelTest {

    @get:Rule
    val dispatcherRule = MainDispatcherRule()

    private val getPhotosUseCase = mockk<GetPhotosUseCase>()

    @Test
    fun `getPhotos should load first page successfully`() = runTest {

        // Arrange

        val photos = (1..100).map { index ->

            Photo(

                albumId = 1,

                id = index,

                title = "Photo $index",

                url = "https://example.com/photo$index.jpg",

                thumbnailUrl = "https://example.com/thumb$index.jpg"

            )

        }

        coEvery {

            getPhotosUseCase()

        } returns Resource.Success(photos)

        // Act

        val viewModel = PhotoListViewModel(

            getPhotosUseCase

        )

        advanceUntilIdle()

        // Assert

        val state = viewModel.state.value

        // Loading should finish

        assertFalse(state.isLoading)

        // First page should contain only 50 items

        assertEquals(50, state.photos.size)

        // First photo

        assertEquals(1, state.photos.first().id)

        // Last photo on first page

        assertEquals(50, state.photos.last().id)

        // There should still be more data to load

        assertTrue(state.hasMoreData)

        // Search query should be empty

        assertEquals("", state.searchQuery)

        // No error should exist

        assertNull(state.error)

    }

    @Test
    fun `onSearchQueryChanged should filter photos by title`() = runTest {

        // Arrange

        val photos = listOf(

            Photo(
                albumId = 1,
                id = 1,
                title = "Android Development",
                url = "",
                thumbnailUrl = ""
            ),

            Photo(
                albumId = 1,
                id = 2,
                title = "Jetpack Compose",
                url = "",
                thumbnailUrl = ""
            ),

            Photo(
                albumId = 1,
                id = 3,
                title = "Kotlin Coroutines",
                url = "",
                thumbnailUrl = ""
            )

        )

        coEvery {

            getPhotosUseCase()

        } returns Resource.Success(photos)

        val viewModel = PhotoListViewModel(getPhotosUseCase)

        advanceUntilIdle()

        // Act

        viewModel.onSearchQueryChanged("Jetpack")

        advanceUntilIdle()

        // Assert

        val state = viewModel.state.value

        assertEquals("Jetpack", state.searchQuery)

        assertEquals(1, state.photos.size)

        assertEquals(
            "Jetpack Compose",
            state.photos.first().title
        )

    }

    @Test
    fun `onSearchQueryChanged should filter photos by id`() = runTest {

        // Arrange

        val photos = listOf(

            Photo(
                albumId = 1,
                id = 101,
                title = "Android",
                url = "",
                thumbnailUrl = ""
            ),

            Photo(
                albumId = 1,
                id = 205,
                title = "Compose",
                url = "",
                thumbnailUrl = ""
            ),

            Photo(
                albumId = 1,
                id = 999,
                title = "Kotlin",
                url = "",
                thumbnailUrl = ""
            )

        )

        coEvery {

            getPhotosUseCase()

        } returns Resource.Success(photos)

        val viewModel = PhotoListViewModel(getPhotosUseCase)

        advanceUntilIdle()

        // Act

        viewModel.onSearchQueryChanged("205")

        advanceUntilIdle()

        // Assert

        val state = viewModel.state.value

        assertEquals("205", state.searchQuery)

        assertEquals(1, state.photos.size)

        assertEquals(205, state.photos.first().id)

        assertEquals("Compose", state.photos.first().title)

    }

    @Test
    fun `loadMorePhotos should load next page successfully`() = runTest {

        // Arrange

        val photos = (1..100).map { index ->

            Photo(

                albumId = 1,

                id = index,

                title = "Photo $index",

                url = "",

                thumbnailUrl = ""

            )

        }

        coEvery {

            getPhotosUseCase()

        } returns Resource.Success(photos)

        val viewModel = PhotoListViewModel(getPhotosUseCase)

        advanceUntilIdle()

        // Assert First Page

        assertEquals(50, viewModel.state.value.photos.size)

        // Act

        viewModel.loadMorePhotos()

        advanceUntilIdle()

        // Assert Second Page

        val state = viewModel.state.value

        assertEquals(100, state.photos.size)

        assertFalse(state.hasMoreData)

        assertEquals(1, state.photos.first().id)

        assertEquals(100, state.photos.last().id)

    }

    @Test
    fun `empty search query should restore all photos`() = runTest {

        // Arrange

        val photos = listOf(

            Photo(
                albumId = 1,
                id = 1,
                title = "Android",
                url = "",
                thumbnailUrl = ""
            ),

            Photo(
                albumId = 1,
                id = 2,
                title = "Jetpack Compose",
                url = "",
                thumbnailUrl = ""
            ),

            Photo(
                albumId = 1,
                id = 3,
                title = "Kotlin Coroutines",
                url = "",
                thumbnailUrl = ""
            )

        )

        coEvery {

            getPhotosUseCase()

        } returns Resource.Success(photos)

        val viewModel = PhotoListViewModel(getPhotosUseCase)

        advanceUntilIdle()

        // Search first

        viewModel.onSearchQueryChanged("Jetpack")

        advanceUntilIdle()

        assertEquals(1, viewModel.state.value.photos.size)

        // Act

        viewModel.onSearchQueryChanged("")

        advanceUntilIdle()

        // Assert

        val state = viewModel.state.value

        assertEquals("", state.searchQuery)

        assertEquals(3, state.photos.size)

        assertTrue(state.hasMoreData.not())

    }

}