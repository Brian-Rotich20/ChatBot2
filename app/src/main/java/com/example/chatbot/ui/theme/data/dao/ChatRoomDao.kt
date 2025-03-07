package com.example.chatbot.ui.theme.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.chatbot.ui.theme.data.entities.ChatRoom
import kotlinx.coroutines.flow.Flow

@Dao
interface ChatRoomDao {
    @Query("SELECT * FROM chat_room")
    fun getAllChatRooms(): Flow<List<ChatRoom>>

    @Upsert
    suspend fun insertChatRoom(chatRoom: ChatRoom)
}