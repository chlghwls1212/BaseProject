package com.example.choibaseproject.com.example.choibaseproject.ui.permission

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.choibaseproject.com.example.choibaseproject.ui.Routes

@Composable
fun PermissionScreen(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "권한 요청 하기", modifier = Modifier.weight(1f).align(alignment = Alignment.CenterHorizontally))

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            modifier = Modifier.weight(8f)
        ) {
            items(dangerousPermissions) { permissionItem ->
                PermissionItemView(permissionItem)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            modifier = Modifier.weight(1f),
            onClick = {
                //TODO 권한요청 Action
            }) {
            Text(text = "권한 요청 하기")
        }

        Button(
            modifier = Modifier.weight(1f),
            onClick = {
            navController.navigate(Routes.Main.route)
        }) {
            Text(text = "메인 으로")
        }
    }
}

@Composable
fun PermissionItemView(permissionItem: PermissionItem) {
    Row(){
        Column(
            modifier = Modifier
                .weight(5f)
                .padding(8.dp)
        ) {
            Text(text = permissionItem.name, style = MaterialTheme.typography.bodyLarge)
            Text(text = permissionItem.description, style = MaterialTheme.typography.bodyMedium)
        }

        Button(
            modifier = Modifier.weight(1f),
            onClick = { /*TODO*/ }) {
            Text(text = "요청")
        }
    }

}
