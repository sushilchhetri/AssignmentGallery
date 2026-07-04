package com.assigment.assignmentgallery.data.model

import com.assigment.assignmentgallery.domain.model.UploadResult

fun UploadResponseDto.toDomain(): UploadResult {

    return UploadResult(

        isSuccess = !files.isNullOrEmpty(),

        imageUrl = files?.values?.firstOrNull()
    )
}