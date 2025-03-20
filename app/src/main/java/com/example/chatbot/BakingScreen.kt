package com.example.chatbot


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ChatScreen(model: ChatViewModel = viewModel()) {
    val uiState by model.uiState.collectAsState()
    val context = LocalContext.current
    var showTypingIndicator by remember { mutableStateOf(false) }
    var moveInputToBottom by remember { mutableStateOf(false) }
    val userInput = model.prompt
    val allRooms by remember { model.getChatsRoom(context) }.collectAsState()

    val allChats by model.getChats(context).collectAsState()


    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    LaunchedEffect(uiState) {
        when (uiState) {
            is UiState.Loading -> {
                showTypingIndicator = true
                delay(500)
                moveInputToBottom = true
            }

            is UiState.Success, is UiState.Error -> showTypingIndicator = false
            else -> {}
        }
    }


    ModalNavigationDrawer(
        drawerState = drawerState, // Add this line
        drawerContent = {
            ModalDrawerSheet {
                Text(
                    "Chat Rooms",
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.headlineSmall
                )
                HorizontalDivider()
                Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                    allRooms.forEach { room ->
                        NavigationDrawerItem(
                            label = { Text(room.title) },
                            selected = false,
                            onClick = { /* Handle room selection */ }
                        )
                    }
                }
            }
        },
        gesturesEnabled = true,
        scrimColor = Color.Black.copy(alpha = 0.3f)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Icon(
                        painter = painterResource(R.drawable.list),
                        contentDescription = "Toggle Drawer",
                        modifier = Modifier
                            .size(20.dp)
                            .clickable {
                                scope.launch {
                                    if (drawerState.isClosed) {
                                        drawerState.open()
                                    } else {
                                        drawerState.close()
                                    }
                                }
                            }
                    )
                    Text(text = "AI Chatbot", style = MaterialTheme.typography.headlineMedium)
                    if (showTypingIndicator) TypingIndicator() else Spacer(Modifier.width(5.dp))

                }
                if (allChats.isNotEmpty()) {
                    LazyColumn(modifier = Modifier.fillMaxHeight(0.75f)) {
                        items(allChats) {
                            ChatComp(it) {
                                model.copyToClipboard(
                                    context = context,
                                    text = it.response
                                )
                            }
                        }
                    }
                } else {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.chatbot),
                            contentDescription = "",
                            modifier = Modifier.size(60.dp),
                            tint = Color.White
                        )
                        Spacer(Modifier.height(20.dp))
                        TypingTextAnimation("Hello?,what i can i help with?")
                    }
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                    // .padding(bottom = if (moveInputToBottom) 40.dp else 300.dp)
                ) {
                    OutlinedTextField(
                        value = userInput,
                        onValueChange = { model.editPrompt(it) },
                        label = { Text("Enter your message") },
                        modifier = Modifier.fillMaxWidth(),
                        trailingIcon = {
                            Icon(
                                painter = painterResource(R.drawable.send),
                                contentDescription = "Send",
                                modifier = Modifier
                                    .size(24.dp)
                                    .clickable {
                                        model.sendPrompt(context)
                                    },
                                tint = if (model.prompt.isNotEmpty()) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.inverseSurface
                            )
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun TypingTextAnimation(fullText: String, typingSpeed: Long = 50L, textSize: Int = 24) {
    var displayedText by remember { mutableStateOf("") }

    LaunchedEffect(fullText) {
        displayedText = ""
        fullText.forEach { char ->
            displayedText += char
            delay(typingSpeed) // Adjust speed (lower = faster typing)
        }
    }

    Box(modifier = Modifier.padding(16.dp)) {
        Text(
            text = displayedText,
            fontSize = textSize.sp,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.ExtraBold
        )
    }
}


// Typing Indicator with Animated Dots
@Composable
fun TypingIndicator() {
    var dotCount by remember { mutableIntStateOf(0) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(500)
            dotCount = (dotCount + 1) % 4
        }
    }

    Text("Typing${".".repeat(dotCount)}", style = MaterialTheme.typography.bodyMedium)
}
