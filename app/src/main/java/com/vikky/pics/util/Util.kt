package com.vikky.pics.util

import android.content.ContentValues
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.core.content.FileProvider
import com.vikky.pics.PhotoApp
import java.io.BufferedInputStream
import java.io.File
import java.io.FileInputStream

class Util {
    companion object {
        fun isNetworkConnected(): Boolean {
            val result: Boolean
            val cm = PhotoApp.applicationContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                val networkInfo = cm.activeNetworkInfo ?: return false
                return networkInfo.isConnected
            } else {
                val networkCapabilities = cm.activeNetwork ?: return false
                val activeNetwork = cm.getNetworkCapabilities(networkCapabilities)?: return false
                result = when {
                    activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                    activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                    activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                    else -> false
                }
            }
            return result
        }

        private val DOWNLOAD_PATH = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)

        fun copyFileToDownloads(context: Context, downloadedFile: File): Uri? {
            val resolver = context.contentResolver
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val contentValues = ContentValues().apply {
                    put(MediaStore.MediaColumns.DISPLAY_NAME, downloadedFile.name)
                    put(MediaStore.MediaColumns.MIME_TYPE, "")
                    put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
                }
                resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)
            } else {
                val authority = "${context.packageName}.provider"
                val destinyFile = File(DOWNLOAD_PATH, downloadedFile.name)
                FileProvider.getUriForFile(context, authority, destinyFile)
            }?.also { downloadedUri ->
                resolver.openOutputStream(downloadedUri).use { outputStream ->
                    val brr = ByteArray(1024)
                    var len: Int
                    val bufferedInputStream =
                        BufferedInputStream(FileInputStream(downloadedFile.absoluteFile))
                    while ((bufferedInputStream.read(brr, 0, brr.size).also { len = it }) != -1) {
                        outputStream?.write(brr, 0, len)
                    }
                    outputStream?.flush()
                    bufferedInputStream.close()
                }
            }
        }
    }



}