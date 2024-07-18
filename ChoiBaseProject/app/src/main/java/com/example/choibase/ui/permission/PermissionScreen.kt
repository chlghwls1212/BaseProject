package com.example.choibase.ui.permission

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.choibase.ui.Routes
import com.example.choibase.ui.custom.ButtonType
import com.example.choibase.ui.custom.SolidButton
import com.example.choibase.utils.PermissionUtils
import com.example.choibase.utils.PermissionUtils.PermissionCheck
import com.example.choibase.utils.PermissionUtils.rememberPermissionsState
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PermissionScreen(navController: NavHostController) {
    val context = LocalContext.current

    val permissions = dangerousPermissions.flatMap { it.permissions }
    val allPermissionsState = rememberPermissionsState(permissions)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "권한 요청 하기",
            modifier = Modifier
                .weight(1f)
                .align(alignment = Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            modifier = Modifier.weight(8f)
        ) {
            items(dangerousPermissions) { permissionItem ->
                PermissionItemView(permissionItem,context)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))


        Row {
            SolidButton(
                text = "메인으로",
                type = ButtonType.Secondary,
                onClick = {
                    navController.navigate(Routes.Main.route)
                },
                modifier = Modifier.weight(1f)
            )

            SolidButton(
                text = "모든 권한 요청 하기",
                type = ButtonType.Primary,
                onClick = {
                    PermissionCheck(dangerousPermissions.flatMap { it.permissions },{})
                },
                modifier = Modifier.weight(1f)
            )

        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PermissionItemView(permissionItem: PermissionItem, context: Context) {
    val permissionState = rememberPermissionsState(permissions = permissionItem.permissions)

    Row(modifier = Modifier.heightIn(min = 100.dp)) {
        Column(
            modifier = Modifier
                .weight(5f)
                .padding(8.dp)
        ) {
            Text(text = permissionItem.name, style = MaterialTheme.typography.bodyLarge)
            Text(text = permissionItem.description, style = MaterialTheme.typography.bodyMedium)
        }

        SolidButton(
            text = "${permissionItem.name}\n 권한 요청",
            type = ButtonType.Primary,
            onClick = {
//                permissionState.launchMultiplePermissionRequest()
            },
            modifier = Modifier.weight(2f)
        )
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun HandlePermission(
    permissionState: MultiplePermissionsState,
    onPermissionResult: (Boolean) -> Unit
) {
    when {
        permissionState.allPermissionsGranted -> {
            onPermissionResult(true)
        }
    }
}


@Composable
fun createAllPermissionMap() = dangerousPermissions.flatMap { it.permissions }