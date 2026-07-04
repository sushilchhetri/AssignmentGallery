package com.assigment.assignmentgallery.domain.model

data class Post(

    val id: Int,

    val title: String,

    val body: String,

    val userId: Int,
    val imageUri: String? = null
)