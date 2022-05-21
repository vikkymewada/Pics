package com.vikky.pics.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.vikky.pics.Constants
import com.vikky.pics.data.database.PhotoDatabase
import com.vikky.pics.data.model.Photo
import com.vikky.pics.network.PicsApi
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@ExperimentalPagingApi
class DataRepoImpl @Inject constructor(
    private val api: PicsApi,
    private val db: PhotoDatabase
) : DataRepo {

    override suspend fun getPhotos(): Flow<PagingData<Photo>> {
        return Pager(
            config = PagingConfig(pageSize = Constants.LIMIT, prefetchDistance = Constants.PREFETCH_DISTANCE),
            remoteMediator = PhotoRemoteMediator(api, db),
            pagingSourceFactory = { db.getPhotoDao().getPaginatedPhotos() }
        ).flow
    }
}