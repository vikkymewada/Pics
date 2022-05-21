package com.vikky.pics.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.vikky.pics.Constants
import com.vikky.pics.data.dao.PagingDao
import com.vikky.pics.data.dao.PhotoDao
import com.vikky.pics.data.model.Paging
import com.vikky.pics.data.model.Photo

@Database(
    entities = [Photo::class, Paging::class],
    version = 1,
    exportSchema = false
)
abstract class PhotoDatabase : RoomDatabase() {
    abstract fun getPhotoDao(): PhotoDao
    abstract fun getPagingDao(): PagingDao

    companion object {
        private var instance: PhotoDatabase? = null

        fun getDatabase(context: Context): PhotoDatabase {
            if (instance == null) {
                synchronized(this) {
                    if (instance == null) {
                        instance = Room.databaseBuilder(context, PhotoDatabase::class.java, Constants.DATABASE_NAME).build()
                    }
                }
            }
            return instance!!
        }
    }
}