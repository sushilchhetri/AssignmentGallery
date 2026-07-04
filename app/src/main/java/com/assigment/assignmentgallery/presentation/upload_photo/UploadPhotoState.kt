package com.assigment.assignmentgallery.presentation.upload_photo

import android.net.Uri
import com.assigment.assignmentgallery.common.UiText
import com.assigment.assignmentgallery.domain.model.UploadResult

data class UploadPhotoState(

    val selectedImageUri: Uri? = null,

    val isUploading: Boolean = false,

    val uploadResult: UploadResult? = null,

    val error: UiText? = null
)