package com.onopriienko.datingapp.util

import android.content.Context
import android.net.Uri
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.util.Base64

fun byteArrayToBase64(byteArray: ByteArray): String = Base64.getEncoder().encodeToString(byteArray)

fun imageToByteArray(context: Context, uri: Uri): ByteArray? {
    val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
    return inputStream?.use { stream ->
        val byteArrayOutputStream = ByteArrayOutputStream()
        val buffer = ByteArray(1024)
        var bytesRead: Int

        while (stream.read(buffer).also { bytesRead = it } != -1) {
            byteArrayOutputStream.write(buffer, 0, bytesRead)
        }

        byteArrayOutputStream.toByteArray()
    }
}
