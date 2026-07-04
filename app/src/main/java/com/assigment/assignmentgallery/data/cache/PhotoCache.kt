package com.assigment.assignmentgallery.data.cache

import com.assigment.assignmentgallery.domain.model.Photo
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PhotoCache @Inject constructor() {

    val photos = mutableListOf<Photo>()

}