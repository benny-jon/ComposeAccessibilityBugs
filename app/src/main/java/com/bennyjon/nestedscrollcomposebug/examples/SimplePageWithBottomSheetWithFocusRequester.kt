package com.bennyjon.nestedscrollcomposebug.examples

import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val A11Y_DELAY_MS = 400L

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SimpleScreenWithBottomSheetWithFocusRequester(
    modifier: Modifier = Modifier
) {

    val coroutineScope = rememberCoroutineScope()

    val focusRequester = remember { FocusRequester() }
    val localFocus = LocalFocusManager.current
    val restoreFocusToButton = {
        coroutineScope.launch {
            localFocus.clearFocus(force = true)
            delay(A11Y_DELAY_MS)
            focusRequester.requestFocus()
        }
    }

    val sheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        confirmValueChange = {
            if (it == ModalBottomSheetValue.Hidden) {
                restoreFocusToButton()
            }
            true
        }
    )

    ModalBottomSheetLayout(
        modifier = modifier,
        sheetState = sheetState,
        sheetContent = {
            MyBottomSheetContentV2 {
                coroutineScope.launch {
                    sheetState.hide()
                    restoreFocusToButton()
                }
            }
        }) {

        Column(modifier = Modifier.fillMaxSize()) {
            Button(onClick = {  }) {
                Text(text = "Random button at the beginning of the screen")
            }
            Text(
                text = "Sample Text before important Button",
                fontSize = 20.sp,
            )
            Button(
                modifier = Modifier
                    .focusRequester(focusRequester)
                    .focusable(),
                onClick = {
                coroutineScope.launch {
                    sheetState.show()
                }
            }) {
                Text(text = "Open Bottom Sheet")
            }
            Text(
                text = "Sample Text after important Button",
                fontSize = 20.sp,
            )
        }
    }
}

@Composable
fun MyBottomSheetContentV2(onClickListener: () -> Unit) {
    Column(modifier = Modifier.height(100.dp)) {
        Button(onClick = onClickListener) {
            Text(text = "Dismiss button sheet")
        }
    }
}
