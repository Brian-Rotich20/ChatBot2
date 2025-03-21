package com.example.chatbot.layers.data.repos

import com.example.chatbot.layers.data.entities.ChatRoom
import kotlinx.coroutines.flow.Flow

interface ChatRoomRepo {
    fun getAllChatRooms(): Flow<List<ChatRoom>>
    suspend fun insertChatRoom(chatRoom: ChatRoom)

}