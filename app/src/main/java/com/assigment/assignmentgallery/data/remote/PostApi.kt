package com.assigment.assignmentgallery.data.remote

import com.assigment.assignmentgallery.data.model.PostDto
import com.assigment.assignmentgallery.data.model.PostRequestDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface PostApi {

    @POST("posts")
    suspend fun createPost(
        @Body request: PostRequestDto
    ): Response<PostDto>
}