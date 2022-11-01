package com.brandsin.store.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Environment
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class FileManager (private val file: File) {
    fun extractAndCompress(path: String): File? {
        return try {
            val bitmap = BitmapFactory.decodeFile(path)
            val f = bitmap.compress(file, getRandomString() ?: "")
            f
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun Bitmap.compress(cacheDir: File, f_name: String): File? {
        val f = File(cacheDir, "user$f_name.jpg")
        f.createNewFile()
        ByteArrayOutputStream().use { stream ->
            compress(Bitmap.CompressFormat.JPEG, 90, stream)
            val bArray = stream.toByteArray()
            FileOutputStream(f).use { os -> os.write(bArray) }
        }//stream
        return f
    }

    @Throws(IOException::class)
    fun createFile(onResult: (File?, String?) -> Unit = { _, _ -> }) {
        // Create an image file name
        try {
            val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH).format(Date())
            val imageFileName = "JPEG_" + timeStamp + "_"
            val storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES
            )
            val image = File.createTempFile(
                imageFileName, // prefix
                ".jpg", // suffix
                storageDir // directory
            )
            // Save a file: path for use with ACTION_VIEW intents
            val mCurrentPhotoPath = "file:" + image.absolutePath
            onResult(image, mCurrentPhotoPath)

        } catch (e: Exception) {
            e.printStackTrace()
            onResult(null, null)
        }
    }
}