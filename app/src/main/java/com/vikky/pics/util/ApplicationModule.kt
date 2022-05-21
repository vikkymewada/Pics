package com.vikky.pics.util

import android.content.Context
import android.util.Log
import androidx.paging.ExperimentalPagingApi
import com.google.gson.GsonBuilder
import com.vikky.pics.BuildConfig
import com.vikky.pics.Constants
import com.vikky.pics.data.DataRepoImpl
import com.vikky.pics.data.database.PhotoDatabase
import com.vikky.pics.network.PicsApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
@OptIn(ExperimentalPagingApi::class)
object ApplicationModule {

    @Provides
    @Singleton
    fun provideOkHttpClient() = if (BuildConfig.DEBUG) {
        val loggingInterceptor = HttpLoggingInterceptor { message -> Log.d("Pics", message) }
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    } else OkHttpClient
        .Builder()
        .build()


    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient
    ): Retrofit =
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .baseUrl(Constants.BASE_URL)
            .client(okHttpClient)
            .build()

    @Provides
    @Singleton
    fun providePicsApi(retrofit: Retrofit): PicsApi = retrofit.create(PicsApi::class.java)

    @Singleton
    @Provides
    fun providePhotoDb(@ApplicationContext appContext: Context) = PhotoDatabase.getDatabase(appContext)

    @Singleton
    @Provides
    fun providePhotoDao(db: PhotoDatabase) = db.getPhotoDao()

    @Singleton
    @Provides
    fun provideRepository(
        api: PicsApi,
        photoDatabase: PhotoDatabase
    ) = DataRepoImpl(api, photoDatabase)
}