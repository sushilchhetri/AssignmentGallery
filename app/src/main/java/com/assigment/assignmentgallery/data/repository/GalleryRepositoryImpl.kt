package com.assigment.assignmentgallery.data.repository

import android.content.Context
import android.net.Uri
import com.assigment.assignmentgallery.R
import com.assigment.assignmentgallery.common.Resource
import com.assigment.assignmentgallery.common.SafeApiCall
import com.assigment.assignmentgallery.common.UiText
import com.assigment.assignmentgallery.data.local.FavoriteDao
import com.assigment.assignmentgallery.data.local.FavoriteEntity
import com.assigment.assignmentgallery.data.model.PostRequestDto
import com.assigment.assignmentgallery.data.model.toDomain
import com.assigment.assignmentgallery.data.remote.PhotoApi
import com.assigment.assignmentgallery.data.remote.PostApi
import com.assigment.assignmentgallery.data.remote.UploadApi
import com.assigment.assignmentgallery.domain.model.Photo
import com.assigment.assignmentgallery.domain.model.Post
import com.assigment.assignmentgallery.domain.model.UploadResult
import com.assigment.assignmentgallery.domain.repository.GalleryRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import javax.inject.Inject
import javax.inject.Singleton
import java.io.File
import okhttp3.RequestBody.Companion.asRequestBody

@Singleton
class GalleryRepositoryImpl @Inject constructor(
    @ApplicationContext
    private val context: Context,

    private val photoApi: PhotoApi,

    private val postApi: PostApi,

    private val uploadApi: UploadApi,

    private val favoriteDao: FavoriteDao,

    private val safeApiCall: SafeApiCall
) : GalleryRepository {

    /**
     * Cache
     */
    private val cachedPhotos = mutableListOf<Photo>()
    private val createdPosts = mutableListOf<Post>()

    override suspend fun getPhotos(): Resource<List<Photo>> {

        return when (val result = safeApiCall.execute {

            photoApi.getPhotos()

        }) {

            is Resource.Success -> {

                val favoriteIds = favoriteDao.getFavoriteIds()

                val photos = result.data.map {

                    it.toDomain(
                        isFavorite = favoriteIds.contains(it.id)
                    )
                }

                cachedPhotos.clear()
                cachedPhotos.addAll(photos)

                Resource.Success(photos)
            }

            is Resource.Error -> result

            Resource.Loading -> Resource.Loading
        }
    }

    override suspend fun getPhotoById(
        photoId: Int
    ): Resource<Photo> {

        val photo = cachedPhotos.find {

            it.id == photoId

        }

        return if (photo != null) {

            Resource.Success(photo)

        } else {

            Resource.Error(
                message =  UiText.StringResource(R.string.photo_not_found),
                code = 404
            )
        }
    }

    override suspend fun toggleFavorite(
        photoId: Int
    ) {

        val isFavorite = favoriteDao.isFavorite(photoId)

        if (isFavorite) {

            favoriteDao.removeFavorite(photoId)

        } else {

            favoriteDao.addFavorite(
                FavoriteEntity(photoId)
            )
        }

        val index = cachedPhotos.indexOfFirst {

            it.id == photoId

        }

        if (index != -1) {

            cachedPhotos[index] = cachedPhotos[index].copy(
                isFavorite = !isFavorite
            )
        }
    }

    override suspend fun isFavorite(
        photoId: Int
    ): Boolean {

        return favoriteDao.isFavorite(photoId)
    }

    override suspend fun createPost(
        title: String,
        body: String,
        imageUri: String?
    ): Resource<Post> {

        return when (

            val result = safeApiCall.execute {

                postApi.createPost(

                    PostRequestDto(
                        title = title,
                        body = body
                    )

                )

            }

        ) {

            is Resource.Success -> {

                 val post = result.data.toDomain().copy(
                    imageUri = imageUri
                )

                createdPosts.add(0, post)

                Resource.Success(post)
            }

            is Resource.Error -> {

                result
            }

            Resource.Loading -> {

                Resource.Loading
            }
        }
    }

    override fun getCreatedPosts(): List<Post> {

        return createdPosts
    }

    override suspend fun uploadPhoto(
        imageUri: Uri
    ): Resource<UploadResult> {

        val multipart = createMultipartBody(imageUri)

        return when (

            val result = safeApiCall.execute {

                uploadApi.uploadPhoto(multipart)

            }

        ) {

            is Resource.Success -> {

                Resource.Success(
                    result.data.toDomain()
                )
            }

            is Resource.Error -> {

                result
            }

            Resource.Loading -> {

                Resource.Loading
            }
        }
    }

    private fun createMultipartBody(
        imageUri: Uri
    ): MultipartBody.Part {

        val inputStream =
            context.contentResolver.openInputStream(imageUri)

        val tempFile = File.createTempFile(
            "upload",
            ".jpg",
            context.cacheDir
        )

        inputStream?.use { input ->

            tempFile.outputStream().use { output ->

                input.copyTo(output)

            }
        }

        val requestBody = tempFile
            .asRequestBody("image/*".toMediaTypeOrNull())

        return MultipartBody.Part.createFormData(

            name = "photo",

            filename = tempFile.name,

            body = requestBody

        )
    }
}