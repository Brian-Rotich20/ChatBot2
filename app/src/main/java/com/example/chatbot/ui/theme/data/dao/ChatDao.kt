package com.example.chatbot.ui.theme.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.chatbot.ui.theme.data.entities.Chat
import kotlinx.coroutines.flow.Flow

@Dao
interface ChatDao {
    @Query("SELECT * FROM chats WHERE dateCreated = :date ORDER BY id ASC")
    fun getAllChats(date: String): Flow<List<Chat>>

    @Upsert
    suspend fun insertChat(chat: Chat)
}