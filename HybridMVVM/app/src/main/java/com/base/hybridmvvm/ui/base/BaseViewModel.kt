package com.base.hybridmvvm.ui.base

import android.app.Activity
import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.base.hybridmvvm.BaseApplication
import com.base.hybridmvvm.data.model.dangerousPermissions
import com.base.hybridmvvm.utils.PermissionUtils
import com.base.hybridmvvm.utils.SystemUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

open class BaseViewModel(application: Application) : AndroidViewModel(application), CoroutineScope {
    private val job = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }

    /** Start Permission Area */
    private val _permissionsGranted = MutableLiveData<Boolean>()
    val permissionsGranted: LiveData<Boolean> get() = _permissionsGranted

    private val _deniedPermissions = MutableLiveData<List<String>>()
    val deniedPermissions: LiveData<List<String>> get() = _deniedPermissions

    private val _showRationaleDialog = MutableLiveData<RationaleData?>()
    val showRationaleDialog: LiveData<RationaleData?> = _showRationaleDialog

    fun requestPermission(context: Context, permissionList: List<String> = listOf()) {
        val requestPermList = permissionList.ifEmpty {
            dangerousPermissions.flatMap { it.permissions }
        }

        PermissionUtils.checkAndRequestPermissions(context, requestPermList) { granted, denied ->
            if (granted) {
                _permissionsGranted.postValue(true)
            } else {
                _deniedPermissions.postValue(denied)
            }
        }
    }

    fun openAppSettings(context: Context) {
        SystemUtils.openAppSettings(context as Activity)
    }

    fun shouldShowRationale(context: Context, permission: String): Boolean {
        return PermissionUtils.shouldShowRationale(context , permission)
    }

    fun onRationaleCalculated(deniedPermissions: List<String>, showRationale: Boolean) {
        _showRationaleDialog.value = RationaleData(deniedPermissions, showRationale)
    }

    data class RationaleData(val deniedPermissions: List<String>, val showRationale: Boolean)

    /** EndPermission Area */
}
