package com.base.hybridmvvm.ui.base

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.core.content.FileProvider
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.base.hybridmvvm.BaseApplication
import com.base.hybridmvvm.utils.CameraUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import java.io.File
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(application: Application) : BaseViewModel(application) {

    private val _photoUri = MutableLiveData<Uri?>()
    val photoUri: LiveData<Uri?> = _photoUri

    fun createImageUri(context: Context): Uri {
        val imageFile = File(context.getExternalFilesDir(null), "temp_image.jpg")
        return FileProvider.getUriForFile(context, context.packageName + ".fileprovider", imageFile)
    }

    fun createPickMultipleImagesIntent(): Intent {
        return Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "image/*"
            putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        }
    }
}
