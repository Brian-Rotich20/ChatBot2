// ChatViewModel.kt
package com.example.chatbot

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatbot.ui.theme.data.ChatRepo
import com.example.chatbot.ui.theme.data.ChatRoomRepo
import com.example.chatbot.ui.theme.data.entities.Chat
import com.example.chatbot.ui.theme.data.entities.ChatRoom
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ChatViewModel : ViewModel() {
    private val _uiState: MutableStateFlow<UiState> = MutableStateFlow(UiState.Initial)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()


    var prompt by mutableStateOf("")
        private set

    var roomId by mutableStateOf("")
        private set

    var allRooms by mutableStateOf(emptyList<ChatRoom>())
    var allChats by mutableStateOf(emptyList<Chat>())


    fun updateCurrentRoomId(id: String) {
        roomId = id
    }

    fun insertChat(chat: Chat, context: Context) {
        val repo = ChatRepo(context)
        viewModelScope.launch {
            repo.insertChat(chat)
        }
    }

    fun insertChatRoom(chatRoom: ChatRoom, context: Context) {
        val repo = ChatRoomRepo(context)
        viewModelScope.launch {
            repo.insertChatRoom(chatRoom)
        }
    }

    fun getChats(context: Context): StateFlow<List<Chat>> {
        val repo = ChatRepo(context)
        return repo.getAllChats(
            roomId
        ).stateIn(
            viewModelScope,
            SharingStarted.Lazily,
            emptyList()
        )
    }

    fun getChatsRoom(context: Context): StateFlow<List<ChatRoom>> {
        val repo = ChatRoomRepo(context)
        return repo.getAllChatRooms(
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
        apiKey = BuildConfig.apiKey
    )

    fun sendPrompt(context: Context) {
        if (prompt != "") {
            _uiState.value = UiState.Loading
            if (roomId == "") {
                //create a new room
                //insert the chat
                viewModelScope.launch {
                    roomId = getCurrentFormattedDate()
                    delay(20)
                    insertChatRoom(
                        chatRoom = ChatRoom(
                            id = roomId,
                            title = prompt
                        ),
                        context = context
                    )

                }
            }
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    val response = generativeModel.generateContent(
                        content { text(prompt) }
                    )
                    response.text?.let { outputText ->
                        _uiState.value = UiState.Success(outputText)
                        insertChat(
                            chat = Chat(
                                id = 0,
                                dateCreated = roomId,
                                response = outputText,
                                question = prompt
                            ),
                            context = context
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
        } else {
            Toast.makeText(context, "prompt is empty", Toast.LENGTH_SHORT).show()
        }
    }

    fun getCurrentFormattedDate(): String {
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
