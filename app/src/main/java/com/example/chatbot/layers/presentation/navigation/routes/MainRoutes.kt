package com.example.chatbot.layers.presentation.navigation.routes

sealed class MainRoutes(val route: String) {

    data object MainScreen : MainRoutes(route = "MAIN_SCREEN")
    data object LoginScreen : MainRoutes(route = "LOGIN_SCREEN")
    data object RegistrationScreen : MainRoutes(route = "REG_SCREEN")

}
