package com.assigment.assignmentgallery.presentation.photo_detail

import com.assigment.assignmentgallery.common.UiText
import com.assigment.assignmentgallery.domain.model.Photo

data class PhotoDetailState(

    val isLoading: Boolean = false,

    val photo: Photo? = null,

    val error: UiText? = null
)