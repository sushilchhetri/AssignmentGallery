package com.assigment.assignmentgallery.domain.usecase

import com.assigment.assignmentgallery.common.Resource
import com.assigment.assignmentgallery.domain.model.Post
import com.assigment.assignmentgallery.domain.repository.GalleryRepository
import javax.inject.Inject

class CreatePostUseCase @Inject constructor(
    private val repository: GalleryRepository
) {

    suspend operator fun invoke(
        title: String,
        body: String,
        imageUri: String?
    ): Resource<Post> {

        return repository.createPost(
            title = title,
            body = body,
            imageUri = imageUri
        )
    }
}