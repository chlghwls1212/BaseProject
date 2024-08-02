package com.base.hybridmvvm.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import androidx.core.content.FileProvider
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

object CameraUtils {

    private var photoUri: Uri? = null

    @Throws(IOException::class)
    fun createImageFile(context: Context): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
        val storageDir: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_",
            ".jpg",
            storageDir
        ).apply {
            photoUri = Uri.fromFile(this)
        }
    }

    fun getPhotoUri(): Uri? = photoUri

    fun takePhotoIntent(context: Context): Intent? {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val photoFile: File? = try {
            createImageFile(context)
        } catch (ex: IOException) {
            null
        }
        photoFile?.also {
            photoUri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                it
            )
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
            return takePictureIntent
        }
        return null
    }

    fun pickSingleImageIntent(): Intent {
        return Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "image/*"
        }
    }

    fun pickMultipleImagesIntent(): Intent {
        return Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "image/*"
            putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        }
    }
}
