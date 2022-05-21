package com.vikky.pics.network

import com.vikky.pics.Constants
import com.vikky.pics.data.model.Photo
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface PicsApi {

    @GET("v2/list")
    suspend fun getPhotos(
        @Query("page") pageNumber: Int = Constants.DEFAULT_PAGE_NUMBER,
        @Query("limit") pageSize: Int = Constants.LIMIT
    ): Response<List<Photo>>
}