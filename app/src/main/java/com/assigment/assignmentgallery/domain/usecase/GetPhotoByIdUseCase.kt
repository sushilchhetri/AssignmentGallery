package com.assigment.assignmentgallery.domain.usecase

import com.assigment.assignmentgallery.common.Resource
import com.assigment.assignmentgallery.domain.model.Photo
import com.assigment.assignmentgallery.domain.repository.GalleryRepository
import javax.inject.Inject

class GetPhotoByIdUseCase @Inject constructor(
    private val repository: GalleryRepository
) {

    suspend operator fun invoke(
        photoId: Int
    ): Resource<Photo> {

        return repository.getPhotoById(photoId)
    }
}