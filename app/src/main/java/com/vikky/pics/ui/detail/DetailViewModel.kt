package com.vikky.pics.ui.detail

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import androidx.lifecycle.ViewModel
import com.bumptech.glide.Glide
import com.vikky.pics.PhotoApp
import com.vikky.pics.R
import com.vikky.pics.data.model.Photo
import com.vikky.pics.util.Util
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor() : ViewModel() {

    private var navigator: DetailNavigator? = null
    interface DetailNavigator {
        fun share()
        fun error(resId: Int)
        fun openIntent(intent: Intent)
    }

    fun setNavigator(navigator: DetailNavigator) {
        this.navigator = navigator
    }

    fun handleShare() {
        navigator?.share()
    }
    fun handleDownload(photo: Photo) {
        CoroutineScope(Dispatchers.IO).launch {
            saveImage(photo.id, Glide.with(PhotoApp.applicationContext()).asBitmap().load(photo.download_url).submit().get())
        }
    }

    private fun saveImage(id: String, image: Bitmap) {
        val imageFile = File(PhotoApp.applicationContext().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).toString() + File.separator + "${id}.jpg")
        if (!imageFile.exists()) {
            try {
                val fOut: OutputStream = FileOutputStream(imageFile)
                image.compress(Bitmap.CompressFormat.JPEG, 100, fOut)
                fOut.close()
            } catch (e: Exception) {
                navigator?.error(R.string.error_occurred)
                // TODO add in firebase logs
                e.printStackTrace()
            }
        }
        saveToGallery(imageFile.absolutePath)
    }

    private fun saveToGallery(imagePath: String?) {
        imagePath?.let {
            var file = File(it)
            val finalUri: Uri? = Util.copyFileToDownloads(PhotoApp.applicationContext(), file)
            finalUri?.path?.let { path ->
                file = File(path)
                try {
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.setDataAndType(finalUri, "image/jpeg")
                    intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
                    navigator?.openIntent(intent)
                } catch (e: Exception) {
                    navigator?.error(R.string.error_occurred)
                    // TODO add in firebase logs
                    e.printStackTrace()
                }
            }
        }
    }
}