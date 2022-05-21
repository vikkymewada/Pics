package com.vikky.pics.data

import androidx.paging.PagingData
import com.vikky.pics.data.model.Photo
import kotlinx.coroutines.flow.Flow

interface DataRepo {
    suspend fun getPhotos(): Flow<PagingData<Photo>>
}