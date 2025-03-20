package com.example.chatbot.layers.presentation.navigation.nav_hosts

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.chatbot.layers.presentation.navigation.routes.MainRoutes
import com.example.chatbot.layers.presentation.screens.main.ChatScreen

@Composable
fun SetUpMainNavHost(
    navHostController: NavHostController
) {

    NavHost(
        navController = navHostController,
        startDestination = MainRoutes.MainScreen.route
    ) {
        composable(route = MainRoutes.MainScreen.route) {
            ChatScreen()
        }
    }

}