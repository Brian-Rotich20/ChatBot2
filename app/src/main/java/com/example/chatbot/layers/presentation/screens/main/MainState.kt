package com.example.chatbot.layers.presentation.screens.main

import com.example.chatbot.layers.data.entities.Chat
import com.example.chatbot.layers.data.entities.ChatRoom

data class MainState(
    val roomId: String = "...",
    val currentIndex: Int = -1,
    val allChats: List<Chat> = listOf(),
    val allRooms: List<ChatRoom> = listOf()

)
