package com.assigment.assignmentgallery.data.model

import com.google.gson.annotations.SerializedName

data class UploadResponseDto(

    @SerializedName("files")
    val files: Map<String, String>?
)