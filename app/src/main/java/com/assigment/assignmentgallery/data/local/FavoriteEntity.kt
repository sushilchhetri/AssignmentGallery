package com.assigment.assignmentgallery.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_photos")
data class FavoriteEntity(

    @PrimaryKey
    val photoId: Int
)