package com.example.chatbot.layers.presentation.comps

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp

fun Modifier.drawVerticalScrollbar(listState: LazyListState): Modifier = composed {
    val totalItems = listState.layoutInfo.totalItemsCount
    if (totalItems == 0) return@composed this

    val firstVisibleIndex = listState.firstVisibleItemIndex
    val viewportSize = listState.layoutInfo.visibleItemsInfo.size

    // Reduce scrollbar height slightly (e.g., 85% of calculated height)
    val scrollbarHeightRatio = (viewportSize.toFloat() / totalItems) * 0.85f

    drawWithContent {
        drawContent()

        if (scrollbarHeightRatio < 1f) {
            val scrollbarTop = firstVisibleIndex.toFloat() / totalItems * size.height
            val scrollbarHeight = size.height * scrollbarHeightRatio

            drawLine(
                color = Color.Gray.copy(alpha = 0.7f),
                start = Offset(size.width - 8.dp.toPx(), scrollbarTop),
                end = Offset(size.width - 8.dp.toPx(), scrollbarTop + scrollbarHeight),
                strokeWidth = 6.dp.toPx(),
                cap = StrokeCap.Round // Round edges
            )
        }
    }
}
