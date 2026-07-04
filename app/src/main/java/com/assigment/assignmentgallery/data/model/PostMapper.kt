package com.assigment.assignmentgallery.data.model

import com.assigment.assignmentgallery.domain.model.Post

fun PostDto.toDomain(): Post {

    return Post(
        id = id,
        title = title,
        body = body,
        userId = userId
    )
}