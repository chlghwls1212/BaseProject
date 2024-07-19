package com.base.hybridmvvm.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.provider.Settings
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

private const val REQUEST_CODE_MULTIPLE_PERMISSIONS = 1001
private const val REQUEST_CODE_SINGLE_PERMISSION = 1002

object PermissionUtils {
    fun checkAndRequestPermissions(
        activity: Activity,
        permissions: List<String>,
        onResult: (Boolean, List<String>) -> Unit
    ) {
        val deniedPermissions = permissions.filter {
            ContextCompat.checkSelfPermission(activity, it) != PackageManager.PERMISSION_GRANTED
        }

        if (deniedPermissions.isEmpty()) {
            onResult(true, emptyList())
        } else {
            if (deniedPermissions.size == 1) {
                ActivityCompat.requestPermissions(
                    activity,
                    arrayOf(deniedPermissions[0]),
                    REQUEST_CODE_SINGLE_PERMISSION
                )
            } else {
                ActivityCompat.requestPermissions(
                    activity,
                    deniedPermissions.toTypedArray(),
                    REQUEST_CODE_MULTIPLE_PERMISSIONS
                )
            }
        }
    }

    fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
        onResult: (Boolean, List<String>) -> Unit
    ) {
        when (requestCode) {
            REQUEST_CODE_SINGLE_PERMISSION,
            REQUEST_CODE_MULTIPLE_PERMISSIONS -> {
                val deniedPermissions = permissions.filterIndexed { index, _ ->
                    grantResults[index] != PackageManager.PERMISSION_GRANTED
                }
                if (deniedPermissions.isEmpty()) {
                    onResult(true, emptyList())
                } else {
                    onResult(false, deniedPermissions)
                }
            }
        }
    }

    fun getRationalePermissions(deniedPermissions: List<String>) =
        deniedPermissions.joinToString(", ") { permission ->
            when (permission) {
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION -> "LOCATION"
                Manifest.permission.CAMERA -> "CAMERA"
                Manifest.permission.READ_PHONE_NUMBERS -> "PHONE"
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE -> "STORAGE"

                else -> "UNKNOWN"
            }
        }

    fun shouldShowRationale(activity: Activity, permission: String): Boolean {
        return ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)
    }

    /**
     * openAppSettings
     * 앱 설정 으로 이동
     * @param context
     */
    fun openAppSettings(context: Context) {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = android.net.Uri.fromParts("package", context.packageName, null)
        }
        context.startActivity(intent)
    }
}