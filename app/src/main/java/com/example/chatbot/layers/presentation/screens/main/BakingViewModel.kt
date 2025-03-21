// ChatViewModel.kt
package com.example.chatbot.layers.presentation.screens.main

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatbot.BuildConfig
import com.example.chatbot.layers.data.entities.Chat
import com.example.chatbot.layers.data.entities.ChatRoom
import com.example.chatbot.layers.data.repos.ChatRepos
import com.example.chatbot.layers.data.repos.ChatRoomRepo
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val chatRepos: ChatRepos,
    private val chatRoomRepo: ChatRoomRepo
) : ViewModel() {
    private val _uiState: MutableStateFlow<UiState> = MutableStateFlow(UiState.Initial)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()
    private val _mainState = MutableStateFlow(MainState())
    val mainState = _mainState.asStateFlow()


    var prompt by mutableStateOf("")
        private set


    fun updateCurrentChats(list: List<Chat>) {
        _mainState.update {
            it.copy(
                allChats = list
            )
        }
    }

    fun updateCurrentRooms(list: List<ChatRoom>) {
        _mainState.update {
            it.copy(
                allRooms = list
            )
        }
    }


    fun updateCurrentRoomId(id: String) {
        _mainState.update {
            it.copy(
                roomId = id
            )
        }
    }

    fun updateCurrentIndex(index: Int) {
        _mainState.update {
            it.copy(
                currentIndex = index
            )
        }
    }


    fun insertChat(chat: Chat) {
        viewModelScope.launch {
            chatRepos.insertChat(chat)
        }
    }

    fun insertChatRoom(chatRoom: ChatRoom) {
        viewModelScope.launch {
            chatRoomRepo.insertChatRoom(chatRoom)
        }
    }

    fun getChats(): StateFlow<List<Chat>> {
        return chatRepos.getAllChats(
            _mainState.value.roomId
        ).stateIn(
            viewModelScope,
            SharingStarted.Lazily,
            emptyList()
        )
    }

    fun getChatsRoom(): StateFlow<List<ChatRoom>> {
        return chatRoomRepo.getAllChatRooms(
        ).stateIn(
            viewModelScope,
            SharingStarted.Lazily,
            emptyList()
        )
    }


    fun editPrompt(value: String) {
        prompt = value
    }


    private val generativeModel = GenerativeModel(
        modelName = "gemini-1.5-flash",
        apiKey = BuildConfig.API_KEY
    )

    fun sendPrompt(context: Context) {
        val newId = getCurrentFormattedDate()

        if (prompt != "") {
            _uiState.value = UiState.Loading
            val roomId = _mainState.value.roomId
            if (roomId == "...") {
                //create a new room
                //insert the chat
                viewModelScope.launch {
                    delay(20)
                    insertChatRoom(
                        chatRoom = ChatRoom(
                            id = newId,
                            title = prompt
                        )
                    )
                    delay(20)

                    updateCurrentIndex(0)


                }
            }
            viewModelScope.launch(Dispatchers.IO) {
              //  delay(200)
                try {
                    val response = generativeModel.generateContent(
                        content { text(prompt) }
                    )
                    response.text?.let { outputText ->
                        _uiState.value = UiState.Success(outputText)
                        insertChat(
                            chat = Chat(
                                id = 0,
                                dateCreated = if (roomId != "...") roomId else newId,
                                response = outputText,
                                question = prompt
                            )
                        )

                        delay(100)
                        editPrompt("")
                    } ?: run {
                        _uiState.value = UiState.Error("No response from AI")
                    }
                } catch (e: Exception) {
                    _uiState.value = UiState.Error(e.localizedMessage ?: "Unknown error")
                }
            }
            if (_mainState.value.roomId == "...") {
                updateCurrentRoomId(id = newId)
            }
        } else {
            Toast.makeText(context, "prompt is empty", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getCurrentFormattedDate(): String {
        val dateFormat = SimpleDateFormat("dd:MM:yyyy:HH:mmm:sss", Locale.getDefault())
        return dateFormat.format(Date())
    }

    fun copyToClipboard(context: Context, text: String) {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("Copied Text", text)
        clipboard.setPrimaryClip(clip)

        Toast.makeText(context, "Copied to clipboard", Toast.LENGTH_SHORT).show()
    }

}
