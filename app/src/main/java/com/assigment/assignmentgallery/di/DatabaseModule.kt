package com.assigment.assignmentgallery.di

import android.content.Context
import androidx.room.Room
import com.assigment.assignmentgallery.data.local.AppDatabase
import com.assigment.assignmentgallery.data.local.FavoriteDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    private const val DATABASE_NAME = "gallery_database"

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {

        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideFavoriteDao(
        appDatabase: AppDatabase
    ): FavoriteDao {

        return appDatabase.favoriteDao()
    }
}