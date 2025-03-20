package com.example.chatbot.layers.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "chat_room")
data class ChatRoom(
    @PrimaryKey(autoGenerate = false) val id: String,// this id will be the date it was created
    val title: String
)
