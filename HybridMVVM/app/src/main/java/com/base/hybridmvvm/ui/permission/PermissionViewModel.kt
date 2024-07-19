package com.base.hybridmvvm.ui.permission

import android.app.Activity
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.base.hybridmvvm.data.model.dangerousPermissions
import com.base.hybridmvvm.utils.PermissionUtils

class PermissionViewModel(application: Application) : AndroidViewModel(application) {

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
                _permissionsGranted.value = true
            } else {
                _deniedPermissions.value = denied
            }
        }
    }

    fun shouldShowRationale(activity: Activity, permission: String): Boolean {
        return PermissionUtils.shouldShowRationale(activity, permission)
    }

    fun openAppSettings(activity: Activity) {
        PermissionUtils.openAppSettings(activity)
    }
}
