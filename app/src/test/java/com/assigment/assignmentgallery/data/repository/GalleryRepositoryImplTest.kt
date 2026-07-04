package com.assigment.assignmentgallery.data.repository

import android.content.Context
import com.assigment.assignmentgallery.common.ErrorHandler
import com.assigment.assignmentgallery.common.SafeApiCall
import com.assigment.assignmentgallery.data.local.FavoriteDao
import com.assigment.assignmentgallery.data.remote.PhotoApi
import com.assigment.assignmentgallery.data.remote.PostApi
import com.assigment.assignmentgallery.data.remote.UploadApi
import io.mockk.mockk
import org.junit.Before
import com.assigment.assignmentgallery.common.Resource
import com.assigment.assignmentgallery.data.model.PostDto
import io.mockk.coEvery
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import retrofit2.Response
import com.assigment.assignmentgallery.data.model.PhotoDto
import io.mockk.coVerify
import com.assigment.assignmentgallery.data.local.FavoriteEntity

class GalleryRepositoryImplTest {

    private lateinit var repository: GalleryRepositoryImpl

    private val context = mockk<Context>(relaxed = true)

    private val photoApi = mockk<PhotoApi>()

    private val postApi = mockk<PostApi>()

    private val uploadApi = mockk<UploadApi>()

    private val favoriteDao = mockk<FavoriteDao>(relaxed = true)

    private val errorHandler = mockk<ErrorHandler>(relaxed = true)

    private lateinit var safeApiCall: SafeApiCall

    @Before
    fun setup() {

        safeApiCall = SafeApiCall(errorHandler)

        repository = GalleryRepositoryImpl(
            context = context,
            photoApi = photoApi,
            postApi = postApi,
            uploadApi = uploadApi,
            favoriteDao = favoriteDao,
            safeApiCall = safeApiCall
        )
    }


    @Test
    fun `createPost should return success and save post`() = runTest {

        // Arrange

        val postDto = PostDto(
            id = 101,
            title = "Android",
            body = "Compose",
            userId = 1
        )

        coEvery {

            postApi.createPost(any())

        } returns Response.success(postDto)

        // Act

        val result = repository.createPost(
            title = "Android",
            body = "Compose",
            imageUri = null
        )

        // Assert

        assertTrue(result is Resource.Success)

        val post = (result as Resource.Success).data

        assertEquals(101, post.id)

        assertEquals("Android", post.title)

        assertEquals("Compose", post.body)

        assertEquals(
            1,
            repository.getCreatedPosts().size
        )

    }

    @Test
    fun `getPhotoById should return photo when photo exists`() = runTest {

        // Arrange

        val photos = listOf(

            PhotoDto(
                albumId = 1,
                id = 1,
                title = "Photo One",
                url = "url1",
                thumbnailUrl = "thumb1"
            ),

            PhotoDto(
                albumId = 1,
                id = 2,
                title = "Photo Two",
                url = "url2",
                thumbnailUrl = "thumb2"
            )
        )

        coEvery {

            photoApi.getPhotos()

        } returns Response.success(photos)

        coEvery {

            favoriteDao.getFavoriteIds()

        } returns emptyList()

        // Act

        repository.getPhotos()

        val result = repository.getPhotoById(2)

        // Assert

        assertTrue(result is Resource.Success)

        val photo = (result as Resource.Success).data

        assertEquals(2, photo.id)

        assertEquals("Photo Two", photo.title)

    }

    @Test
    fun `toggleFavorite should add favorite when photo is not favorite`() = runTest {

        // Arrange

        val photos = listOf(

            PhotoDto(
                albumId = 1,
                id = 10,
                title = "Android",
                url = "url",
                thumbnailUrl = "thumb"
            )
        )

        coEvery {
            photoApi.getPhotos()
        } returns Response.success(photos)

        coEvery {
            favoriteDao.getFavoriteIds()
        } returns emptyList()

        coEvery {
            favoriteDao.isFavorite(10)
        } returns false

        coEvery {
            favoriteDao.addFavorite(any())
        } returns Unit

        // Load cache

        repository.getPhotos()

        // Act

        repository.toggleFavorite(10)

        // Assert

        coVerify(exactly = 1) {

            favoriteDao.addFavorite(
                FavoriteEntity(10)
            )

        }

        val result = repository.getPhotoById(10)

        assertTrue(result is Resource.Success)

        val photo = (result as Resource.Success).data

        assertTrue(photo.isFavorite)

    }
}