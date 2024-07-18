package com.example.choibase.utils

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.core.app.ActivityCompat
import com.example.choibase.com.example.choibase.ui.custom.PermissionDialog
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState

object PermissionUtils {

    @OptIn(ExperimentalPermissionsApi::class)
    @Composable
    //권한 리스트 타입 캐스팅
    fun rememberPermissionsState(permissions: List<String>) =
        rememberMultiplePermissionsState(permissions)

    @Composable
    //권한 체크 공통함수 ( 단수 ,복수 )
    fun PermissionCheck(
        permissions: List<String>,
        onResult: () -> Unit = {}
    ) {
        val context = LocalContext.current

        var deniedPermission: Array<String> by remember { mutableStateOf(emptyArray()) }
        var recheckPermissions: Boolean by remember { mutableStateOf(true) }

        val multiplePermissionLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestMultiplePermissions(),
            onResult = { params ->
                if (params.values.contains(false)) {
                    deniedPermission = params.filterValues { !it }.keys.toTypedArray()
                } else {
                    onResult()
                }
            }
        )

        val singlePermissionLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission(),
            onResult = { isGranted ->
                if (!isGranted)
                    deniedPermission = arrayOf(permissions[0])
                else
                    onResult()
            }
        )

        fun updatePermissionsList(): Array<String> {
            val checkedList = ArrayList<String>()
            permissions.forEach { permission ->
                if (context.checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                    checkedList.add(permission)
                }
            }
            return checkedList.toTypedArray()
        }

        //recheckPermissions 해당 변수가 변경 될 때 실행.
        LaunchedEffect(recheckPermissions) {
            if (recheckPermissions) {
                recheckPermissions = false
                val newList = updatePermissionsList()
                if (newList.isEmpty()) {
                    onResult()
                } else {
                    if (newList.size == 1)
                        singlePermissionLauncher.launch(newList[0])
                    else if (newList.size > 1) {
                        multiplePermissionLauncher.launch(newList)
                    }
                }
            }
        }
        fun setDeniedPermissionText(): String {
            val deniedPermissionNames = mutableListOf<String>()
            deniedPermission.forEach { item ->
                when (item) {
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION -> {
                        deniedPermissionNames.add("LOCATION")
                    }

                    Manifest.permission.CAMERA -> {
                        deniedPermissionNames.add("CAMERA")
                    }

                    Manifest.permission.READ_PHONE_NUMBERS -> {
                        deniedPermissionNames.add("PHONE")
                    }

                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE -> {
                        deniedPermissionNames.add("STORAGE")
                    }
                }
            }
            return deniedPermissionNames.joinToString(", ")
        }

        if (deniedPermission.isNotEmpty()) {
            val buttonCase = !ActivityCompat.shouldShowRequestPermissionRationale(
                (context as Activity),
                deniedPermission[0]
            )

            PermissionDialog(
                onDismissRequest = {
                    deniedPermission = emptyArray()
                    recheckPermissions = true
                },
                title = "권한요청",
                text = "필수 ${setDeniedPermissionText()} 권한이 수락되지 않았습니다.",
                confirmButton = if (buttonCase) "환경 설정" else "확인",
                onConfirm = {
                    if (buttonCase) {
                        //todo 환경 설정으로 보내기
                    } else{
                        deniedPermission = emptyArray()
                        recheckPermissions = true
                    }
                }

            )
        }
    }
}