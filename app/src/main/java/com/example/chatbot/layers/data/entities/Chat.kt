package com.example.chatbot.layers.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "chats")
data class Chat(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val dateCreated: String,// this will be the id of the chat session format dd:mm:yyyy:hh:mmm:sss
    val response: String,
    val question: String
)
