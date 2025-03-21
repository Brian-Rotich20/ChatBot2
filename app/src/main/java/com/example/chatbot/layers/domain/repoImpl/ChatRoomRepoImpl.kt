package com.example.chatbot.layers.domain.repoImpl


import com.example.chatbot.layers.data.dao.ChatRoomDao
import com.example.chatbot.layers.data.entities.ChatRoom
import com.example.chatbot.layers.data.repos.ChatRoomRepo
import kotlinx.coroutines.flow.Flow

class ChatRoomRepoImpl(private val dao: ChatRoomDao) : ChatRoomRepo {

    override fun getAllChatRooms(): Flow<List<ChatRoom>> {
        return dao.getAllChatRooms()
    }

    override suspend fun insertChatRoom(chatRoom: ChatRoom) {
        dao.insertChatRoom(chatRoom)
    }
}