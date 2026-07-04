package com.assigment.assignmentgallery.data.model

import com.google.gson.annotations.SerializedName

data class PostDto(

    @SerializedName("id")
    val id: Int,

    @SerializedName("title")
    val title: String,

    @SerializedName("body")
    val body: String,

    @SerializedName("userId")
    val userId: Int
)