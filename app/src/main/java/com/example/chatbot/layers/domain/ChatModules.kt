package com.example.chatbot.layers.domain

import android.app.Application
import com.example.chatbot.layers.data.repos.ChatRepos
import com.example.chatbot.layers.data.repos.ChatRoomRepo
import com.example.chatbot.layers.domain.repoImpl.ChatRepoImpl
import com.example.chatbot.layers.domain.repoImpl.ChatRoomRepoImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ChatModules {

    @Singleton
    @Provides
    fun provideMainDataBase(application: Application): MainDatabase {
        return MainDatabase.getDatabase(application)
    }

    @Singleton
    @Provides
    fun providesChatRoomRepos(database: MainDatabase): ChatRoomRepo {
        return ChatRoomRepoImpl(dao = database.chatRoomDao())
    }


    @Singleton
    @Provides
    fun providesChatRepos(database: MainDatabase): ChatRepos {
        return ChatRepoImpl(dao = database.chatDao())
    }


}