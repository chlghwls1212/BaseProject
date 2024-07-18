package com.example.choibase

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.choibase.ui.Routes
import com.example.choibase.ui.main.MainScreen
import com.example.choibase.ui.permission.PermissionScreen
import com.example.choibase.ui.splash.SplashScreen
import com.example.choibase.ui.theme.ChoiBaseTheme
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            ChoiBaseTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = Routes.Splash.route) {
                    composable(Routes.Splash.route) { SplashScreen(navController) }
                    composable(Routes.Permission.route) { PermissionScreen(navController) }
                    composable(Routes.Main.route) { MainScreen(navController) }
                }
            }
        }
    }
}
