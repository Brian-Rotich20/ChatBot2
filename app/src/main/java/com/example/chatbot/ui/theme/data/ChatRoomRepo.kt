package com.example.chatbot.ui.theme.data

import android.content.Context
import com.example.chatbot.ui.theme.MainDatabase
import com.example.chatbot.ui.theme.data.dao.ChatRoomDao
import com.example.chatbot.ui.theme.data.entities.ChatRoom
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