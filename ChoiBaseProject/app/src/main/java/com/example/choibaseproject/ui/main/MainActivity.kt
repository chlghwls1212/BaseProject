package com.example.choibaseproject.ui.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.choibaseproject.com.example.choibaseproject.ui.Routes
import com.example.choibaseproject.com.example.choibaseproject.ui.main.MainScreen
import com.example.choibaseproject.com.example.choibaseproject.ui.permission.PermissionScreen
import com.example.choibaseproject.com.example.choibaseproject.ui.splash.SplashScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val navController = rememberNavController()
            NavHost(navController = navController, startDestination = Routes.Splash.route) {
                composable(Routes.Splash.route) { SplashScreen(navController) }
                composable(Routes.Permission.route) { PermissionScreen(navController) }
                composable(Routes.Main.route) { MainScreen(navController) }
            }
        }
    }
}
