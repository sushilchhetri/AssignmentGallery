package com.assigment.assignmentgallery.domain.usecase

import android.net.Uri
import com.assigment.assignmentgallery.common.Resource
import com.assigment.assignmentgallery.domain.model.UploadResult
import com.assigment.assignmentgallery.domain.repository.GalleryRepository
import javax.inject.Inject

class UploadPhotoUseCase @Inject constructor(
    private val repository: GalleryRepository
) {

    suspend operator fun invoke(
        imageUri: Uri
    ): Resource<UploadResult> {

        return repository.uploadPhoto(imageUri)
    }
}