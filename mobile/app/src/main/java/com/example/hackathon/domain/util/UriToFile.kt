package com.example.hackathon.domain.util

import android.content.Context
import android.net.Uri
import java.io.File
import java.io.FileOutputStream

fun uriToFile(context: Context, uri: Uri): File? {
    return try {
        val contentResolver = context.contentResolver
        // Создаем временный файл
        val file = File.createTempFile("upload_", ".jpg", context.cacheDir)
        val inputStream = contentResolver.openInputStream(uri) ?: return null
        val outputStream = FileOutputStream(file)
        inputStream.copyTo(outputStream)
        inputStream.close()
        outputStream.close()
        file
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}
