package com.example.files

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.util.Log
import com.example.files.data.Networking
import java.io.File

class MainActivityRepository {

    private lateinit var sharedPrefers: SharedPreferences

    suspend fun getFileFromUrl(url: String, context: Context): String {
        sharedPrefers = context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE)
        if (getFromPreferences(url)) return ""
        val timeStamp = System.currentTimeMillis().toString()
        val nameAndType = url.split("/").last().split(".")
        val fileName = "${timeStamp}_${nameAndType.getOrNull(0)}.${nameAndType.getOrNull(1)}"
        val downloadsDir = context.getExternalFilesDir("Downloads")
        val file = File(downloadsDir, fileName)
        Log.e("tags", "file name: $fileName")
        try {
            file.outputStream().use { fileOutputStream ->
                Networking.api.getFile(url)
                    .byteStream()
                    .buffered()
                    .use { inputStream ->
                        inputStream.copyTo(fileOutputStream)
                    }
            }
            saveToPreferences(url, fileName)
            return fileName
        } catch (t: Throwable) {
            file.delete()
            Log.e("tags", "Error: $t")
            return t.toString()
        }
    }

    private fun saveToPreferences(url: String, fileName: String) {
        sharedPrefers
            .edit()
            .putString(url, fileName)
            .apply()
    }

    private fun getFromPreferences(key: String): Boolean {
        val result = sharedPrefers.getString(key, null)
        return result != null
    }

    suspend fun firstLaunchDownload(context: Context) {
        val internalFileDir = context.filesDir
        Log.e("tags", internalFileDir.absolutePath)
        val image = File(internalFileDir, "download_image.png")
        val downloadsDir = context.getExternalFilesDir("Downloads")
        Log.e("tags", downloadsDir?.absolutePath.toString())
        val file = File(downloadsDir, "download_image.png")
        try {
            context.resources.assets.open("download_list/download_list.txt")
                .bufferedReader()
                .use {
                    for (line in it.readLines()) {
                        image.outputStream().use { fileOutputStream ->
                            Networking.api.getFile("https://wonder-day.com/wp-content/uploads/2020/10/wonder-day-among-us-21.png")
                                .byteStream()
                                .buffered()
                                .use { inputStream ->
                                    val png = BitmapFactory.decodeStream(inputStream)
                                    png.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream)
                                }
                        }
                    }
                }
        } catch (t: Throwable) {
            Log.e("tags", "error with save image $t")
        }
    }

    companion object {
        private const val SHARED_PREFS_NAME = "files_prefs"
    }
}