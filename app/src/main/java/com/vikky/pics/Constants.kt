package com.vikky.pics

import com.vikky.pics.data.model.Photo
import java.lang.StringBuilder

class Constants {
    companion object {
        const val LIMIT = 20
        const val PREFETCH_DISTANCE = 3
        const val DEFAULT_PAGE_NUMBER = 1
        const val DATABASE_NAME = "pics_photos_db"
        const val BASE_URL = "https://picsum.photos/"
        const val SHARED_ELEMENT_TAG = "photo"
        const val fixHeight = 600
        private const val PHOTO_BASE_URL = BASE_URL + "id/"

        fun getPhotoUrl(photo: Photo): String {
            val maxRatio: Float = photo.width!!.toFloat() / photo.height!!.toFloat()
            val sb = StringBuilder()
            sb.append(PHOTO_BASE_URL)
            sb.append(photo.id)
            sb.append("/").append((fixHeight * maxRatio).toInt()).append("/").append(fixHeight)
            return sb.toString()
        }
    }


}