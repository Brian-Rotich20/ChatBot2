package com.example.chatbot.layers.domain.repoImpl

import com.example.chatbot.layers.data.dao.ChatDao
import com.example.chatbot.layers.data.entities.Chat
import com.example.chatbot.layers.data.repos.ChatRepos
import kotlinx.coroutines.flow.Flow

class ChatRepoImpl(private val dao: ChatDao) : ChatRepos {

    override fun getAllChats(date: String): Flow<List<Chat>> {
        return dao.getAllChats(date)
    }

    override suspend fun insertChat(chat: Chat) {
        dao.insertChat(chat)
    }
}