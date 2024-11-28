package com.example.nok

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.nok.feature.auth.signin.SignInScreen
import com.example.nok.feature.auth.signup.SignUpScreen
import com.example.nok.ui.UserPage

@Composable
fun MainApp() {
    Surface( modifier = Modifier.fillMaxSize()) {
        val navController = rememberNavController()

        NavHost(navController = navController, startDestination = "login") {
            composable(route = "login") {
                SignInScreen(navController = navController)

            }
            composable(route = "signup") {
                SignUpScreen(navController = navController)
            }
            composable(route = "home") {
                val context = LocalContext.current // Context 가져오기
                UserPage(navController = navController, context = context)            }
        }
    }
}