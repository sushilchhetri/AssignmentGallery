package com.assigment.assignmentgallery.domain.usecase

import com.assigment.assignmentgallery.domain.repository.GalleryRepository
import javax.inject.Inject

class ToggleFavoriteUseCase @Inject constructor(
    private val repository: GalleryRepository
) {

    suspend operator fun invoke(
        photoId: Int
    ) {

        repository.toggleFavorite(photoId)
    }
}