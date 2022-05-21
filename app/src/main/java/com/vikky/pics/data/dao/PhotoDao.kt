package com.vikky.pics.data.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.vikky.pics.data.model.Photo

@Dao
interface PhotoDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun upsert(imageItem: Photo): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPhotos(photos: List<Photo>)

    @Query("SELECT * FROM photo")
    fun getPaginatedPhotos(): PagingSource<Int, Photo>

    @Query("SELECT * FROM photo")
    fun getPhotos(): List<Photo>

    @Query("Delete FROM photo")
    suspend fun deleteAllPhotos()
}