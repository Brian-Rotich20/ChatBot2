package com.example.chatbot.layers.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.chatbot.layers.presentation.navigation.nav_hosts.SetUpMainNavHost
import com.example.chatbot.layers.presentation.screens.main.ChatScreen
import com.example.chatbot.layers.presentation.theme.ChatBotTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ChatBotTheme {
                val navHostController = rememberNavController()
                Surface(

                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {

                    SetUpMainNavHost(
                        navHostController = navHostController
                    )
                }
            }
        }

    }
}
