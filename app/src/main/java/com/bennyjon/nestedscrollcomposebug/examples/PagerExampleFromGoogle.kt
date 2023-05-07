package com.bennyjon.nestedscrollcomposebug.examples

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

/**
 * SOURCE: https://cs.android.com/androidx/platform/frameworks/support/+/androidx-main:compose/foundation/foundation/samples/src/main/java/androidx/compose/foundation/samples/PagerSamples.kt;drc=8200a13fd2551907d0a7fa99f00b09988821704b;l=276
 *
 * Small change: I had to replace "rememberPagerState { 10 }" with "pageCount = 10" to make it work
 * with my current Compose version.
 */

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HorizontalPagerWithScrollableContent() {
    // This is a sample using NestedScroll and Pager.
    // We use the toolbar offset changing example from
    // androidx.compose.ui.samples.NestedScrollConnectionSample

    val pagerState = rememberPagerState()

    val toolbarHeight = 48.dp
    val toolbarHeightPx = with(LocalDensity.current) { toolbarHeight.roundToPx().toFloat() }
    val toolbarOffsetHeightPx = remember { mutableStateOf(0f) }
    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                val delta = available.y
                val newOffset = toolbarOffsetHeightPx.value + delta
                toolbarOffsetHeightPx.value = newOffset.coerceIn(-toolbarHeightPx, 0f)
                return Offset.Zero
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(nestedScrollConnection)
    ) {
        TopAppBar(
            modifier = Modifier
                .height(toolbarHeight)
                .offset { IntOffset(x = 0, y = toolbarOffsetHeightPx.value.roundToInt()) },
            title = { Text("Toolbar offset is ${toolbarOffsetHeightPx.value}") }
        )

        val paddingOffset =
            toolbarHeight + with(LocalDensity.current) { toolbarOffsetHeightPx.value.toDp() }

        HorizontalPager(
            modifier = Modifier.fillMaxSize(),
            state = pagerState,
            pageCount = 10,
            contentPadding = PaddingValues(top = paddingOffset)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                repeat(20) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(64.dp)
                            .padding(4.dp)
                            .background(if (it % 2 == 0) Color.Black else Color.Yellow),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = it.toString(),
                            modifier = Modifier.clickable {  },
                            color = if (it % 2 != 0) Color.Black else Color.Yellow
                        )
                    }
                }
            }
        }
    }
}
