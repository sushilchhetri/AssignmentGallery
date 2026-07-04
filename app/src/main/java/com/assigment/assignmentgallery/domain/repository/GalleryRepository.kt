package com.assigment.assignmentgallery.domain.repository

import com.assigment.assignmentgallery.common.Resource
import com.assigment.assignmentgallery.domain.model.Photo
import com.assigment.assignmentgallery.domain.model.Post
import android.net.Uri
import com.assigment.assignmentgallery.domain.model.UploadResult

interface GalleryRepository {

    suspend fun getPhotos(): Resource<List<Photo>>


    suspend fun getPhotoById(
        photoId: Int
    ): Resource<Photo>

    suspend fun toggleFavorite(
        photoId: Int
    )

    suspend fun isFavorite(
        photoId: Int
    ): Boolean


    suspend fun createPost(
        title: String,
        body: String,
        imageUri: String?
    ): Resource<Post>

    fun getCreatedPosts(): List<Post>


    suspend fun uploadPhoto(
        imageUri: Uri
    ): Resource<UploadResult>
}