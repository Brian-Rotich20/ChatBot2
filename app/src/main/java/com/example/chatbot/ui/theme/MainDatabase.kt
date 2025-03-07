package com.example.chatbot.ui.theme

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.chatbot.ui.theme.data.dao.ChatDao
import com.example.chatbot.ui.theme.data.dao.ChatRoomDao
import com.example.chatbot.ui.theme.data.entities.Chat
import com.example.chatbot.ui.theme.data.entities.ChatRoom

@Database(
    entities = [
        ChatRoom::class,
        Chat::class
    ],
    version = 1,//
    exportSchema = false
)
abstract class MainDatabase : RoomDatabase() {

    // abstract fun userDao(): UserDao
    abstract fun chatDao(): ChatDao
    abstract fun chatRoomDao(): ChatRoomDao


    companion object {
        @Volatile
        private var INSTANCE: MainDatabase? = null

        fun getDatabase(context: Context): MainDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MainDatabase::class.java,
                    "main_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
