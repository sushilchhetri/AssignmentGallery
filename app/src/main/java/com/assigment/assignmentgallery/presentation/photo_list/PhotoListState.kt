package com.assigment.assignmentgallery.presentation.photo_list

import com.assigment.assignmentgallery.common.UiText
import com.assigment.assignmentgallery.domain.model.Photo

data class PhotoListState(

    val isLoading: Boolean = false,

    val photos: List<Photo> = emptyList(),

    val error: UiText? = null,

    val searchQuery: String = "",

    val isLoadingMore: Boolean = false,

    val hasMoreData: Boolean = true
)