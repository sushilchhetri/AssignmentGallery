package com.assigment.assignmentgallery.domain.usecase


import com.assigment.assignmentgallery.common.Resource
import com.assigment.assignmentgallery.domain.model.Photo
import com.assigment.assignmentgallery.domain.repository.GalleryRepository
import javax.inject.Inject

class GetPhotosUseCase @Inject constructor(
    private val repository: GalleryRepository
) {

    suspend operator fun invoke(): Resource<List<Photo>> {
        return repository.getPhotos()
    }
}