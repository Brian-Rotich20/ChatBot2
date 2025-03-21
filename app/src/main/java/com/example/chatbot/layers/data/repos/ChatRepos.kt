package com.example.chatbot.layers.data.repos

import com.example.chatbot.layers.data.entities.Chat
import kotlinx.coroutines.flow.Flow

interface ChatRepos {
    fun getAllChats(date: String): Flow<List<Chat>>
    suspend fun insertChat(chat: Chat)

}