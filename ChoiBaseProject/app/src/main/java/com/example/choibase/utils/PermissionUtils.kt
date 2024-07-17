package com.example.choibaseproject.utils

import androidx.compose.runtime.Composable
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberMultiplePermissionsState

object PermissionUtils {

    @OptIn(ExperimentalPermissionsApi::class)
    @Composable
    fun rememberPermissionsState(permissions: List<String>) = rememberMultiplePermissionsState(permissions)
}