package com.example.chatbot.layers.presentation.comps

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.FloatingActionButtonDefaults.smallShape
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.chatbot.R
import com.example.chatbot.layers.data.entities.Chat
import com.example.chatbot.layers.presentation.theme.iconStandard
import com.example.chatbot.layers.presentation.theme.mediumLargeText
import com.example.chatbot.layers.presentation.theme.smallText
import com.example.chatbot.layers.utils.NumberFormattingTransformation

@Composable
fun ChatComp(
    chat: Chat,
    spacer: Int = 10,
    onCopy: () -> Unit
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
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            Icon(
                painter = painterResource(R.drawable.copy),
                contentDescription = "",
                modifier = Modifier
                    .size(17.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .clickable {
                        onCopy()
                    }
            )

        }
        Spacer(Modifier.height(spacer.dp))
    }
}



@Composable/*basic text field for inserting data */
fun BasicEditTextComp(
    modifier: Modifier = Modifier,
    onDone: () -> Unit,
    onValueChange: (String) -> Unit,
    isError: Boolean = false,
    isHelperTextEnabled: Boolean = false,
    helperText: String = "",
    spacer: Int = 5,
    isPassword: Boolean = false,
    isNumber: Boolean = false,
    hint: String,
    value: String,
    singleLine: Boolean = true,
    isMaxCharEnabled: Boolean = false,
    maxChar: Int = 50,
    leadingIcon: Int? = null,
    isPasswordVisible: Boolean = false,
    onPassWordClick: () -> Unit,
    isNumberToFormat: Boolean = false
) {
    Column(
        modifier = modifier.fillMaxWidth(0.8f), horizontalAlignment = Alignment.CenterHorizontally
    ) {
        androidx.compose.material3.OutlinedTextField(
            value = value,
            onValueChange = { value ->
                if (isMaxCharEnabled) {
                    val updatedValue = value.take(maxChar) // Ensure maximum length is not exceeded
                    onValueChange(updatedValue)
                    if (isNumber) {
                        val filteredValue = updatedValue.filter { it.isDigit() }
                        if (filteredValue != value) {
                            // Non-numeric characters were entered, prevent onValueChange
                            return@OutlinedTextField
                        }
                        onValueChange(value)
                    }
                } else if (isNumber) {
                    val filteredValue = value.filter { it.isDigit() }
                    if (filteredValue != value) {
                        // Non-numeric characters were entered, prevent onValueChange
                        return@OutlinedTextField
                    }
                    onValueChange(value)
                } else {
                    onValueChange(value)
                }
            },
            singleLine = singleLine,
            placeholder = {
                Text(
                    text = hint,
                    color = if (!isError) MaterialTheme.colorScheme.onSurfaceVariant else Color.Red,
                    textAlign = TextAlign.Start,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = FontWeight.Light,
                    fontSize = mediumLargeText,
                    maxLines = 1
                )
            },
            modifier = Modifier.fillMaxWidth(),
            leadingIcon = {
                if (leadingIcon != null) {
                    Icon(
                        painter = painterResource(id = leadingIcon),
                        contentDescription ="",
                        tint = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.size(iconStandard)
                    )
                }
            },
            trailingIcon = {
                // this will choose the right icon according to the current state of the text-field
                if (isPassword && isPasswordVisible) {
                    Icon(painter = painterResource(id = R.drawable.vissibillity_off),
                        contentDescription = "",
                        tint = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier
                            .size(iconStandard)
                            .clip(smallShape)
                            .clickable {
                                onPassWordClick()
                            })
                } else if (!isPasswordVisible && isPassword) {
                    Icon(painter = painterResource(id = R.drawable.vissible),
                        contentDescription = "",
                        tint = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier
                            .size(iconStandard)
                            .clip(smallShape)
                            .clickable {
                                onPassWordClick()
                            })
                } else if (isError) {
                    //this error icon should be shown only for 500ms then should disappear
                    Icon(
                        painter = painterResource(id = R.drawable.error_24),
                        contentDescription ="",
                        tint = MaterialTheme.colorScheme.error,
                        modifier = Modifier.size(iconStandard)
                    )
                }
            },
            isError = isError,/*   colors = TextFieldDefaults.colors(
                   focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                   unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                   errorTextColor = MaterialTheme.colorScheme.error,
                   focusedTextColor = MaterialTheme.colorScheme.onSurface,
                   unfocusedTextColor = MaterialTheme.colorScheme.onBackground,
                   unfocusedIndicatorColor = Color.Transparent
               ),*/
            shape = smallShape,
            visualTransformation = if (!isPasswordVisible && isPassword) PasswordVisualTransformation() else if (isNumberToFormat && value != "") NumberFormattingTransformation() else VisualTransformation.None,
            keyboardOptions = KeyboardOptions(
                keyboardType = if (isNumber) KeyboardType.Number else KeyboardType.Text,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(onDone = { onDone() })
        )
        AnimatedVisibility(isHelperTextEnabled) {
            Spacer(modifier = Modifier.height(10.dp))
            // this will be used to show some text not to be forgotten or some short error
            Row(
                modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = helperText,
                    color = if (isError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.outline,
                    fontSize = smallText,
                    fontWeight = FontWeight.Light,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                if (isMaxCharEnabled) {
                    Text(
                        text = "${value.length}/${maxChar}",
                        color = if (isError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.outline,
                        fontSize = smallText,
                        fontWeight = FontWeight.Light,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(spacer.dp))
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
