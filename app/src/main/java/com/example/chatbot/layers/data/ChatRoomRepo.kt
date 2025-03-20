package com.example.chatbot.layers.data

import android.content.Context
import com.example.chatbot.layers.domain.MainDatabase
import com.example.chatbot.layers.data.dao.ChatRoomDao
import com.example.chatbot.layers.data.entities.ChatRoom
import kotlinx.coroutines.flow.Flow

class ChatRoomRepo(context: Context) : ChatRoomDao {
    private val db = MainDatabase.getDatabase(context)
    private val dao = db.chatRoomDao()
    override fun getAllChatRooms(): Flow<List<ChatRoom>> {
        return dao.getAllChatRooms()
    }

    override suspend fun insertChatRoom(chatRoom: ChatRoom) {
        dao.insertChatRoom(chatRoom)
    }
}