package com.example.files

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import com.example.files.data.Networking
import java.io.File
import java.io.ByteArrayOutputStream


class MainActivityRepository {

    private lateinit var sharedPrefers: SharedPreferences

    suspend fun getFileFromUrl(url: String, context: Context): String {
        sharedPrefers = context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE)

        if (getFromPreferences(url)) return ""
        val timeStamp = System.currentTimeMillis().toString()
        val nameAndType = url.split("/").last().split(".")
        val fileName = "${timeStamp}_${nameAndType.getOrNull(0)}.${nameAndType.getOrNull(1)}"
        return getTextFile(context, fileName, url)
    }

    private suspend fun getTextFile(
        context: Context,
        fileName: String,
        url: String,
        external: Boolean = true
    ): String {
        val file: File = if (external) {
            val downloadsDir = context.getExternalFilesDir("Downloads")
            File(downloadsDir, fileName)
        } else {
            val downloadsDir = context.filesDir
            File(downloadsDir, fileName)
        }
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

    @Suppress("BlockingMethodInNonBlockingContext")
    suspend fun firstLaunchDownload(context: Context) {
        sharedPrefers = context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE)

        try {
            context.resources.assets.open("download_list/download_list.txt")
                .bufferedReader()
                .use {
                    val line = it.readLines()
                    downloadAnImage(
                        context,
                        context.getString(R.string.among_us_png),
                        line[0]
                    )
                    downloadAnImage(
                        context,
                        context.getString(R.string.download_logo_png),
                        line[1]
                    )
                    getTextFile(
                        context,
                        context.getString(R.string.readme_txt),
                        line[2],
                        false
                    )

                }
        } catch (t: Throwable) {
            Log.e("tags", "error with save start content $t")
        }
    }

    @Suppress("BlockingMethodInNonBlockingContext")
    suspend fun downloadAnImage(
        context: Context,
        fileName: String,
        url: String
    ) {

        val internalFileDir = context.filesDir
        val image = File(internalFileDir, fileName)
        image.createNewFile()
        try {
            image.outputStream().use { fileOutputStream ->
                Networking.api.getFile(url)
                    .byteStream()
                    .buffered()
                    .use { inputStream ->
                        val png = BitmapFactory.decodeStream(inputStream)
                        val bos = ByteArrayOutputStream()
                        png.compress(Bitmap.CompressFormat.PNG, 100, bos)
                        val bitmapData = bos.toByteArray()
                        fileOutputStream.write(bitmapData)
                        fileOutputStream.flush()
                        fileOutputStream.close()
                    }
            }
        } catch (t: Throwable) {
            Log.e("tags", "Error with save file: $fileName")
            image.delete()
        }
    }


    companion object {
        private const val SHARED_PREFS_NAME = "files_prefs"
    }
}