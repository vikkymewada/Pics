package com.vikky.pics.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.vikky.pics.data.model.Paging

@Dao
interface PagingDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(remoteKey: List<Paging>)

    @Query("SELECT * FROM paging WHERE id LIKE :id")
    suspend fun getPaging(id: String): Paging?

    @Query("DELETE FROM paging")
    suspend fun clearAll()
}