package com.assigment.assignmentgallery.data.model

import com.assigment.assignmentgallery.domain.model.Photo

fun PhotoDto.toDomain(
    isFavorite: Boolean = false
): Photo {

    return Photo(
        albumId = albumId,
        id = id,
        title = title,
        url = url,
        thumbnailUrl = thumbnailUrl,
        isFavorite = isFavorite
    )
}