package com.bennyjon.nestedscrollcomposebug.utils

import androidx.compose.foundation.border
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/**
 * Useful to have a more visual focus indicator during Keyboard navigation.
 */
fun Modifier.focusableBorder(): Modifier = composed {
    var focused by remember { mutableStateOf(false) }
    this
        .onFocusEvent { focused = it.isFocused }
        .border(
            width = 2.dp,
            color = if (focused) Color(255, 0, 255) else Color.Transparent
        )
}
