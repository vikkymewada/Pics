package com.vikky.pics.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.vikky.pics.Constants
import com.vikky.pics.data.database.PhotoDatabase
import com.vikky.pics.data.model.Paging
import com.vikky.pics.data.model.Photo
import com.vikky.pics.network.PicsApi


@OptIn(ExperimentalPagingApi::class)
class PhotoRemoteMediator(private val api: PicsApi, private val db: PhotoDatabase) : RemoteMediator<Int, Photo>() {
    private val photoDao = db.getPhotoDao()
    private val pagingDao = db.getPagingDao()

    override suspend fun load(loadType: LoadType, state: PagingState<Int, Photo>): MediatorResult {
        val page = when (loadType) {
            LoadType.REFRESH -> {
                val paging = getCurrentPaging(state)
                if (paging?.next != null) {
                    paging.next - 1
                } else {
                    Constants.DEFAULT_PAGE_NUMBER
                }
            }
            LoadType.PREPEND -> {
                val paging = getFirstPage(state)
                if (paging?.prev != null) {
                    paging.prev
                } else {
                    return MediatorResult.Success(endOfPaginationReached = paging != null)
                }
            }
            LoadType.APPEND -> {
                val paging = getLastPage(state)
                if (paging?.next != null) {
                    paging.next
                } else {
                    return MediatorResult.Success(endOfPaginationReached = paging != null)
                }
            }
        }
        try {
            val images = api.getPhotos(page).body()
            val hasReachedEnd = images == null || images.isEmpty()

            db.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    pagingDao.clearAll()
                    photoDao.deleteAllPhotos()
                }
                val prev = if (page == Constants.DEFAULT_PAGE_NUMBER) null else page - 1
                val next = if (hasReachedEnd) null else page + 1
                val keys = images?.map { Paging(id = it.id, prev = prev, next = next) }
                if (keys != null) {
                    pagingDao.insertAll(keys)
                }
                if (images != null) {
                    photoDao.insertPhotos(images)
                }
            }
            return MediatorResult.Success(endOfPaginationReached = hasReachedEnd)
        } catch (e: Exception) {
            return MediatorResult.Error(e)
        }
    }

    private suspend fun getLastPage(state: PagingState<Int, Photo>): Paging? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()?.let { item ->
            pagingDao.getPaging(item.id)
        }
    }

    private suspend fun getFirstPage(state: PagingState<Int, Photo>): Paging? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()?.let { item ->
            pagingDao.getPaging(item.id)
        }
    }

    private suspend fun getCurrentPaging(state: PagingState<Int, Photo>): Paging? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { id ->
                pagingDao.getPaging(id)
            }
        }
    }
}