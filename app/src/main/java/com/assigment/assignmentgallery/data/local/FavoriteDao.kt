package com.assigment.assignmentgallery.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface FavoriteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFavorite(
        favorite: FavoriteEntity
    )

    @Query("DELETE FROM favorite_photos WHERE photoId = :photoId")
    suspend fun removeFavorite(
        photoId: Int
    )

    @Query("SELECT photoId FROM favorite_photos")
    suspend fun getFavoriteIds(): List<Int>

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_photos WHERE photoId = :photoId)")
    suspend fun isFavorite(
        photoId: Int
    ): Boolean

    @Query("DELETE FROM favorite_photos WHERE photoId = :photoId")
    suspend fun deleteFavorite(
        photoId: Int
    )
}