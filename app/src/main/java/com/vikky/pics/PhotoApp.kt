package com.vikky.pics

import android.app.Application
import android.content.Context
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class PhotoApp : Application() {
    init {
        instance = this
    }

    companion object {
        private var instance: PhotoApp? = null
        fun applicationContext(): Context {
            return instance!!.applicationContext
        }
    }
}