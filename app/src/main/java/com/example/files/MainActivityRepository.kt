package com.example.files

import android.content.Context
import android.util.Log
import com.example.files.data.Networking
import java.io.File

class MainActivityRepository {

    suspend fun getFileFromUrl(url: String, context: Context): String {
        val timeStamp = System.currentTimeMillis().toString()
        val nameAndType = url.split("/").last().split(".")
        val fileName = "${nameAndType.getOrNull(0)}.${nameAndType.getOrNull(1)}"
        val downloadsDir = context.getExternalFilesDir("Downloads")
        val file = File(downloadsDir, "${timeStamp}_$fileName")
        Log.e("tags", "file name: ${timeStamp}_$fileName")
        try {
            file.outputStream().use { fileOutputStream ->
                Networking.api.getFile(url)
                    .byteStream()
                    .buffered()
                    .use { inputStream ->
                        inputStream.copyTo(fileOutputStream)
                    }
            }
        } catch (t: Throwable) {
            file.delete()
            Log.e("tags", "Error: $t")
        }
        context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE)
            .edit()
            .putString(url, fileName)
            .apply()
        return fileName
    }

    companion object {
        private const val SHARED_PREFS_NAME = "files_prefs"
    }
}