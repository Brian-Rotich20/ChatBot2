package com.example.chatbot.layers.presentation.screens.main


import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.chatbot.R
import com.example.chatbot.layers.presentation.comps.ChatComp
import com.example.chatbot.layers.presentation.comps.drawVerticalScrollbar
import com.example.chatbot.layers.presentation.theme.mediumShape
import com.example.chatbot.layers.presentation.theme.smallShape
import com.example.chatbot.layers.presentation.theme.smallText
import com.example.chatbot.layers.utils.getTimeDifference
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ChatScreen(
    model: ChatViewModel = hiltViewModel()
) {
    val uiState by model.uiState.collectAsState()
    val mainState = model.mainState.collectAsState().value
    val context = LocalContext.current
    var showTypingIndicator by remember { mutableStateOf(false) }
    var moveInputToBottom by remember { mutableStateOf(false) }
    var shouldRefresh by remember { mutableStateOf(false) }
    val listState = rememberLazyListState() // LazyColumn's state
    // val scope = rememberCoroutineScope()
    val userInput = model.prompt
    val allRooms by remember { model.getChatsRoom() }.collectAsState()
    val keyboardController = LocalSoftwareKeyboardController.current

    val allChats by remember(key1 = shouldRefresh) { model.getChats() }.collectAsState()
    model.updateCurrentChats(allChats)

    LaunchedEffect(key1 = mainState.allChats.size) {
        if (mainState.allChats.size > 2) {
            listState.animateScrollToItem(mainState.allChats.size - 1)//we want it to scroll to the second lasts chat end

        } else {
            listState.animateScrollToItem(mainState.allChats.size)
        }
    }

    LaunchedEffect(key1 = shouldRefresh) {
        delay(800)
        if (mainState.allChats.size > 2) {
            listState.animateScrollToItem(mainState.allChats.size - 1)//we want it to scroll to the second lasts chat end

        } else {
            listState.animateScrollToItem(mainState.allChats.size)
        }
    }

    if (shouldRefresh) {
      //  Toast.makeText(context, "called ${mainState.roomId}", Toast.LENGTH_SHORT).show()
        val allChats by model.getChats().collectAsState()
        model.updateCurrentChats(allChats)
        shouldRefresh = false
    }

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
    LaunchedEffect(key1 = mainState.roomId) {
        //clear the current chats
        model.updateCurrentChats(emptyList())

        //refill
        shouldRefresh = true
    }


    ModalNavigationDrawer(
        drawerState = drawerState, // Add this line
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier
                    .fillMaxWidth(0.65f)
                    .padding(start = 10.dp, bottom = 20.dp)
                    .clip(shape = mediumShape)
                    .shadow(elevation = 3.dp)
            ) {
                Text(
                    "History",
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.headlineSmall
                )
                Spacer(modifier = Modifier.height(20.dp))
                HorizontalDivider()
                Spacer(modifier = Modifier.height(20.dp))
                Column(
                    modifier = Modifier
                        .verticalScroll(rememberScrollState())
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    allRooms.reversed().forEachIndexed { index, room ->
                        val day = getTimeDifference(room.id)
                        NavigationDrawerItem(
                            label = { Text(room.title, fontSize = smallText) },
                            selected = index == mainState.currentIndex,
                            onClick = {
                                model.updateCurrentRoomId(
                                    room.id
                                )
                                model.updateCurrentIndex(index = index)
                                scope.launch {
                                    if (drawerState.isClosed) {
                                        drawerState.open()
                                    } else {
                                        drawerState.close()
                                    }
                                }
                            },
                            badge = {
                                Text(
                                    fontSize = smallText,
                                    text = day,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth(0.96f)
                                .clip(smallShape)
                                .background(
                                    MaterialTheme.colorScheme.inverseOnSurface.copy(
                                        alpha = if (index == mainState.currentIndex) 0.09f else 0.3f
                                    )
                                )
                        )
                        Spacer(Modifier.height(10.dp))
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
                    Icon(
                        painter = painterResource(R.drawable.add),
                        contentDescription = "",
                        modifier = Modifier.clickable {
                            model.updateCurrentRoomId("...")
                            model.updateCurrentChats(emptyList())

                        }
                    )

                }
                if (mainState.allChats.isNotEmpty()) {
                    LazyColumn(
                        state = listState,
                        modifier = Modifier.fillMaxHeight(0.75f)
                            .fillMaxWidth()
                            .drawVerticalScrollbar(listState) // Apply the custom scrollbar
                    ) {
                        items(mainState.allChats) {
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
                            if (showTypingIndicator) {
                                TypingIndicator()
                            } else {
                                Icon(
                                    painter = painterResource(R.drawable.send),
                                    contentDescription = "Send",
                                    modifier = Modifier
                                        .size(24.dp)
                                        .clickable {
                                            model.sendPrompt(
                                                context = context,
                                                keyboardController = keyboardController
                                            )
                                        },
                                    tint = if (model.prompt.isNotEmpty()) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.inverseSurface
                                )
                            }
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

@Composable
fun VerticalScrollbar(modifier: Modifier, scrollState: LazyListState) {
    val firstVisibleItemIndex = scrollState.firstVisibleItemIndex
    val itemsCount = scrollState.layoutInfo.totalItemsCount

    if (itemsCount > 0) {
        Box(
            modifier = modifier
                .width(4.dp)
                .background(Color.Gray, RoundedCornerShape(50))
                .fillMaxHeight(
                    (firstVisibleItemIndex + 1) / itemsCount.toFloat()
                )
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
