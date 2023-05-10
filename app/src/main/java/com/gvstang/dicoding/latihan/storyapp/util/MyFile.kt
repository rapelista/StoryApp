package com.gvstang.dicoding.latihan.storyapp.util

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.os.Environment
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.Locale

class MyFile(private val context: Context) {

    private var timestamp = String()

    init {
        timestamp = SimpleDateFormat(
            FILENAME_FORMAT,
            Locale.US
        ).format(System.currentTimeMillis())
    }

    fun create(): File {
        val storageDir: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(timestamp, ".jpg", storageDir)
    }

    fun fromUri(image: Uri): File {
        val contentResolver: ContentResolver = context.contentResolver
        val myFile = create()

        val inputStream = contentResolver.openInputStream(image) as InputStream
        val outputStream: OutputStream = FileOutputStream(myFile)
        val buf = ByteArray(1024)
        var len: Int
        while (inputStream.read(buf).also { len = it } > 0) outputStream.write(buf, 0, len)
        outputStream.close()
        inputStream.close()

        return myFile
    }

    companion object {
        const val FILENAME_FORMAT = "ddMMMyyyyHHmmss"
    }

}