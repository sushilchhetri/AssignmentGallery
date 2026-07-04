package com.assigment.assignmentgallery.presentation.create_post

import android.net.Uri
import com.assigment.assignmentgallery.common.UiText
import com.assigment.assignmentgallery.domain.model.Post
import com.assigment.assignmentgallery.domain.model.UploadResult

data class CreatePostState(

    val title: String = "",

    val body: String = "",

    val selectedImageUri: Uri? = null,

    val isLoading: Boolean = false,

    val isUploading: Boolean = false,

    val uploadResult: UploadResult? = null,

    val error: UiText? = null,

    val createdPosts: List<Post> = emptyList()
)