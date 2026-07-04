package com.assigment.assignmentgallery.presentation.create_post

import android.net.Uri
import com.assigment.assignmentgallery.R
import com.assigment.assignmentgallery.common.Resource
import com.assigment.assignmentgallery.common.UiText
import com.assigment.assignmentgallery.domain.model.Post
import com.assigment.assignmentgallery.domain.usecase.CreatePostUseCase
import com.assigment.assignmentgallery.domain.usecase.UploadPhotoUseCase
import com.assigment.assignmentgallery.domain.repository.GalleryRepository
import com.assigment.assignmentgallery.util.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CreatePostViewModelTest {

    @get:Rule
    val dispatcherRule = MainDispatcherRule()

    private val createPostUseCase = mockk<CreatePostUseCase>()

    private val uploadPhotoUseCase = mockk<UploadPhotoUseCase>(relaxed = true)

    private val repository = mockk<GalleryRepository>(relaxed = true)

    @Test
    fun `createPost should show error when title is empty`() = runTest {

        val viewModel = CreatePostViewModel(
            createPostUseCase,
            uploadPhotoUseCase,
            repository
        )

        viewModel.updateBody("Compose Body")

        viewModel.createPost()

        advanceUntilIdle()

        val state = viewModel.state.value

        assertNotNull(state.error)

        assertTrue(state.error is UiText.StringResource)

        assertEquals(
            R.string.error_title_required,
            (state.error as UiText.StringResource).resId
        )
    }

    @Test
    fun `createPost should show error when body is empty`() = runTest {

        val viewModel = CreatePostViewModel(
            createPostUseCase,
            uploadPhotoUseCase,
            repository
        )

        viewModel.updateTitle("Android")

        viewModel.createPost()

        advanceUntilIdle()

        val state = viewModel.state.value

        assertNotNull(state.error)

        assertTrue(state.error is UiText.StringResource)

        assertEquals(
            R.string.error_body_required,
            (state.error as UiText.StringResource).resId
        )

    }

    @Test
    fun `onImageSelected should update selected image`() {

        val viewModel = CreatePostViewModel(
            createPostUseCase,
            uploadPhotoUseCase,
            repository
        )

        val uri = mockk<Uri>()

        viewModel.onImageSelected(uri)

        assertEquals(
            uri,
            viewModel.state.value.selectedImageUri
        )
    }

    @Test
    fun `clearSelectedImage should remove selected image`() {

        val viewModel = CreatePostViewModel(
            createPostUseCase,
            uploadPhotoUseCase,
            repository
        )

        val uri = mockk<Uri>()

        viewModel.onImageSelected(uri)

        viewModel.clearSelectedImage()

        assertNull(
            viewModel.state.value.selectedImageUri
        )
    }

    @Test
    fun `createPost should create post successfully`() = runTest {

        val post = Post(
            id = 101,
            title = "Android",
            body = "Compose",
            userId = 1
        )

        coEvery {

            createPostUseCase(
                title = any(),
                body = any(),
                imageUri = any()
            )

        } returns Resource.Success(post)

        coEvery {

            repository.getCreatedPosts()

        } returns listOf(post)

        val viewModel = CreatePostViewModel(
            createPostUseCase,
            uploadPhotoUseCase,
            repository
        )

        viewModel.updateTitle("Android")

        viewModel.updateBody("Compose")

        viewModel.createPost()

        advanceUntilIdle()

        val state = viewModel.state.value

        assertEquals("", state.title)

        assertEquals("", state.body)

        assertFalse(state.isLoading)

        assertNull(state.error)

        assertEquals(
            1,
            state.createdPosts.size
        )

        assertEquals(
            "Android",
            state.createdPosts.first().title
        )
    }

    @Test
    fun `updateTitle should update title`() {

        val viewModel = CreatePostViewModel(
            createPostUseCase,
            uploadPhotoUseCase,
            repository
        )

        viewModel.updateTitle("Jetpack Compose")

        assertEquals(
            "Jetpack Compose",
            viewModel.state.value.title
        )
    }

    @Test
    fun `updateBody should update body`() {

        val viewModel = CreatePostViewModel(
            createPostUseCase,
            uploadPhotoUseCase,
            repository
        )

        viewModel.updateBody("Modern Android UI")

        assertEquals(
            "Modern Android UI",
            viewModel.state.value.body
        )
    }
}