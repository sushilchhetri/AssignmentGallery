package com.assigment.assignmentgallery.data.remote

import com.assigment.assignmentgallery.data.model.UploadResponseDto
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface UploadApi {

    @Multipart
    @POST("post")
    suspend fun uploadPhoto(

        @Part
        photo: MultipartBody.Part

    ): Response<UploadResponseDto>
}