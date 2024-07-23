package com.base.hybridmvvm.ui.base

import android.app.Activity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.base.hybridmvvm.data.model.dangerousPermissions
import com.base.hybridmvvm.utils.PermissionUtils
import com.base.hybridmvvm.utils.SystemUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

open class BaseViewModel : ViewModel(), CoroutineScope {
    private val job = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }

    /**
     * Permission Area
     */
    private val _permissionsGranted = MutableLiveData<Boolean>()
    val permissionsGranted: LiveData<Boolean> get() = _permissionsGranted

    private val _deniedPermissions = MutableLiveData<List<String>>()
    val deniedPermissions: LiveData<List<String>> get() = _deniedPermissions

    fun requestPermission(activity: Activity, permissionList: List<String> = listOf()) {
        val requestPermList = permissionList.ifEmpty {
            dangerousPermissions.flatMap { it.permissions }
        }

        PermissionUtils.checkAndRequestPermissions(activity, requestPermList) { granted, denied ->
            if (granted) {
                _permissionsGranted.postValue(true)
            } else {
                _deniedPermissions.postValue(denied)
            }
        }
    }

    fun shouldShowRationale(activity: Activity, permission: String): Boolean {
        return PermissionUtils.shouldShowRationale(activity, permission)
    }

    fun openAppSettings(activity: Activity) {
        SystemUtils.openAppSettings(activity)
    }
}
