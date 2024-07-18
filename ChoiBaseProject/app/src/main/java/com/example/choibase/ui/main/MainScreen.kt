package com.example.choibase.ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import com.example.choibase.ui.Routes
import kotlinx.coroutines.delay

@Composable
fun MainScreen(navController: NavHostController) {
    LaunchedEffect(Unit) {
        delay(2000L)
        navController.navigate(Routes.Permission.route)
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Green)
    )
}