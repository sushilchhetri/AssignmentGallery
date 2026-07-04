package com.assigment.assignmentgallery.data.remote

import com.assigment.assignmentgallery.data.model.PhotoDto
import retrofit2.Response
import retrofit2.http.GET

interface PhotoApi {

    @GET("photos")
    suspend fun getPhotos(): Response<List<PhotoDto>>
}