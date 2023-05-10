package com.bennyjon.nestedscrollcomposebug.examples

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SimpleScreenWithBottomSheet(
    modifier: Modifier = Modifier
) {

    val sheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
    val coroutineScope = rememberCoroutineScope()

    ModalBottomSheetLayout(
        modifier = modifier,
        sheetState = sheetState,
        sheetContent = {
            MyBottomSheetContent {
                coroutineScope.launch {
                    sheetState.hide()
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
            Button(onClick = {
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
fun MyBottomSheetContent(onClickListener: () -> Unit) {
    Column(modifier = Modifier.height(100.dp)) {
        Button(onClick = onClickListener) {
            Text(text = "Dismiss button sheet")
        }
    }
}
