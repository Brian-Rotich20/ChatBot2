package com.example.chatbot.layers.data

import android.content.Context
import com.example.chatbot.layers.domain.MainDatabase
import com.example.chatbot.layers.data.dao.ChatDao
import com.example.chatbot.layers.data.entities.Chat
import kotlinx.coroutines.flow.Flow

class ChatRepo(context: Context) : ChatDao {
    private val db = MainDatabase.getDatabase(context)
    private val dao = db.chatDao()
    override fun getAllChats(date: String): Flow<List<Chat>> {
        return dao.getAllChats(date)
    }

    override suspend fun insertChat(chat: Chat) {
        dao.insertChat(chat)
    }
}