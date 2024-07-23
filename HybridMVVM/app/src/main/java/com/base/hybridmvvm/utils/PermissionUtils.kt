package com.base.hybridmvvm.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.provider.Settings
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

private const val REQUEST_CODE_PERMISSIONS = 1001

private var onResultCallback: ((Boolean, List<String>) -> Unit)? = null

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
            onResultCallback = onResult
            ActivityCompat.requestPermissions(
                activity,
                deniedPermissions.toTypedArray(),
                REQUEST_CODE_PERMISSIONS
            )

        }
    }

    fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            val deniedPermissions = mutableListOf<String>()
            for ((index, result) in grantResults.withIndex()) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    deniedPermissions.add(permissions[index])
                }
            }
            val allGranted = deniedPermissions.isEmpty()
            onResultCallback?.invoke(allGranted, deniedPermissions)
            onResultCallback = null
        }
    }

    fun getRationalePermissions(deniedPermissions: List<String>) =
        deniedPermissions.joinToString(", ") { permission ->
            getManifestPermissionName(permission)
        }

    fun shouldShowRationale(activity: Activity, permission: String): Boolean {
        return ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)
    }

    /**
     * getManifestPermissionName
     * 권한의 풀네임 중 마지막 단어만 가져옴
     * @param perm
     * @return
     */
    private fun getManifestPermissionName(perm: String): String {
        val lastDotIndex = perm.lastIndexOf('.')

        return if (lastDotIndex == -1) {
            perm
        } else {
            perm.substring(lastDotIndex + 1)
        }
    }
}