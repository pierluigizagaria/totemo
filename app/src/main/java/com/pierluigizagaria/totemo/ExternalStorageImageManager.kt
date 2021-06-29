package com.pierluigizagaria.totemo

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat
import java.io.File
import java.io.FileOutputStream

class ExternalStorageImageManager(context: Context, path: String?) {
    companion object {
        private const val IMAGE_EXTENSION = "jpg"
        private val IMAGE_FORMAT = CompressFormat.JPEG
    }

    private val path: File = File(context.getExternalFilesDir(null), path ?: "")
    private var fileName = "image"

    init {
        this.path.mkdirs()
    }

    val file: File?
        get() = if (path.exists())
            File(path, String.format("%s.%s", fileName, IMAGE_EXTENSION))
        else null

    fun setFileName(name: String): ExternalStorageImageManager {
        fileName = name
        return this
    }

    fun save(bitmapImage: Bitmap): File? {
        try {
            val file = file
            val fileOutputStream = FileOutputStream(file, false)
            bitmapImage.compress(IMAGE_FORMAT, 100, fileOutputStream)
            fileOutputStream.close()
            return file
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }
}