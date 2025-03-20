package org.penakelex.ratingphysics.feature_rating.presentation.util

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import android.util.Log
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

fun getFileNameByUri(uri: Uri, context: Context): String? = context.contentResolver
    .query(uri, null, null, null)?.use { cursor ->
        try {
            val nameIndex = cursor.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME)
            cursor.moveToFirst()
            cursor.getString(nameIndex)
        } catch (exception: IllegalArgumentException) {
            exception.printStackTrace()
            null
        }
    }

@Throws(IOException::class)
fun saveFileToCache(uri: Uri, fileName: String, context: Context): String {
    val destinationFile = File(context.cacheDir, fileName)

    context.contentResolver.openInputStream(uri)
        ?.use { inputStream ->
            FileOutputStream(destinationFile).use { outputStream ->
                inputStream.copyTo(outputStream)
            }
        }

    return destinationFile.absolutePath.also {
        Log.d("FILE_PATH", it)
    }
}