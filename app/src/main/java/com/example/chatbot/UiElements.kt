package com.example.chatbot

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.unit.dp
import com.example.chatbot.ui.theme.data.entities.Chat

@Composable
fun ChatComp(
    chat: Chat,
    spacer: Int = 10,
    onCopy:()->Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(0.95f),
        horizontalAlignment = Alignment.End
    ) {
        val formattedText = parseBoldText(chat.question)
        val formattedText2 = parseBoldText(chat.response)

        Box(
            modifier = Modifier
                .fillMaxWidth(0.5f) // Set width to 50% of the parent
                .heightIn(max = 70.dp) // Restrict maximum height to 70.dp
                .clip(RoundedCornerShape(5.dp))
                .background(MaterialTheme.colorScheme.inverseOnSurface)
                .verticalScroll(rememberScrollState()) // Make content scrollable beyond 70.dp
        ) {

            Text(
                text = chat.question,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(15.dp)
            )
        }
        Spacer(Modifier.height(15.dp))
        Box(
            modifier = Modifier

                .clip(RoundedCornerShape(5.dp))
        ) {
            Text(
                text = formattedText2,
                modifier = Modifier.padding(15.dp)
            )
        }
        HorizontalDivider()
        Spacer(Modifier.height(15.dp))
        Row (
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ){
            Icon(
                painter = painterResource(R.drawable.copy),
                contentDescription = "",
                modifier = Modifier.size(17.dp).clip(RoundedCornerShape(2.dp)).clickable {
                    onCopy()
                }
            )

        }
        Spacer(Modifier.height(spacer.dp))
    }
}


// Updated parseBoldText function
@Composable
private fun parseBoldText(text: String): AnnotatedString {
    return buildAnnotatedString {
        val regex = "\\*\\*(.*?)\\*\\*".toRegex()
        var lastIndex = 0

        regex.findAll(text).forEach { match ->
            append(
                AnnotatedString(
                    text.substring(lastIndex, match.range.first),
                    spanStyle = SpanStyle(color = MaterialTheme.colorScheme.primary) // Normal text
                )
            )

            pushStyle(SpanStyle(color = Color.White)) // White color for **bold** text
            append(match.groups[1]?.value ?: "") // Append bold text without **
            pop()

            lastIndex = match.range.last + 1
        }

        append(
            AnnotatedString(
                text.substring(lastIndex),
                spanStyle = SpanStyle(color = MaterialTheme.colorScheme.primary) // Normal text
            )
        )
    }

}
