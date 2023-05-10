package com.bennyjon.nestedscrollcomposebug.examples

import android.content.res.Configuration
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.TabRowDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.ScrollAxisRange
import androidx.compose.ui.semantics.horizontalScrollAxisRange
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.bennyjon.nestedscrollcomposebug.R
import com.bennyjon.nestedscrollcomposebug.utils.focusableBorder
import com.google.accompanist.pager.pagerTabIndicatorOffset
import kotlinx.coroutines.launch
import kotlin.math.roundToInt


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HorizontalPagerWithClickableScrollableContentWithBanner(
    pages: List<String> = listOf("Tab 1", "Tab 2", "Tab 3", "Tab 4", "Tab 5"),
    sizes: List<Int> = listOf(20, 10, 10, 4, 12) // real life example with variable Page heights
) {


    val pagerState = rememberPagerState()
    val coroutineScope = rememberCoroutineScope()

    val tabsHeight = 48.dp
    val topBannerHeight = getRealScreenWidth(LocalConfiguration.current) - tabsHeight
    val toolbarHeight = tabsHeight + topBannerHeight
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
        Column(modifier = Modifier
            .height(toolbarHeight)
            .zIndex(1f)
            .offset { IntOffset(x = 0, y = toolbarOffsetHeightPx.value.roundToInt()) }) {

            Column(
                modifier = Modifier
                    .height(topBannerHeight) // lets pretend its a fixed height
                    .fillMaxWidth()
                    .background(Color.DarkGray), verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(id = R.string.banner_text),
                    style = TextStyle(fontWeight = FontWeight.Bold),
                    fontSize = 20.sp,
                    modifier = Modifier.background(Color.DarkGray),
                    textAlign = TextAlign.Center,
                )
            }

            TabRow(
                // Our selected tab is our current page
                selectedTabIndex = pagerState.currentPage,
                modifier = Modifier.height(tabsHeight),
                // Override the indicator, using the provided pagerTabIndicatorOffset modifier
                indicator = { tabPositions ->
                    TabRowDefaults.Indicator(
                        Modifier.pagerTabIndicatorOffset(pagerState, tabPositions)
                    )
                }
            ) {
                // Add tabs for all of our pages
                pages.forEachIndexed { index, title ->
                    Tab(
                        text = { Text(title) },
                        selected = pagerState.currentPage == index,
                        modifier = Modifier.focusableBorder(),
                        onClick = {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(index)
                            }
                        },
                    )
                }
            }
        }

        val paddingOffset =
            toolbarHeight + with(LocalDensity.current) { toolbarOffsetHeightPx.value.toDp() }

        HorizontalPager(
            modifier = Modifier
                .fillMaxSize()
                .semantics {
                    horizontalScrollAxisRange =
                        ScrollAxisRange(value = { 0f }, maxValue = { 0f })
                },
            state = pagerState,
            pageCount = pages.size,
            contentPadding = PaddingValues(top = paddingOffset),
            verticalAlignment = Alignment.Top
        ) { page ->
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                repeat(sizes[page]) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(64.dp)
                            .padding(4.dp)
                            .background(if (it % 2 == 0) Color.Black else Color.Yellow)
                            .focusableBorder(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = it.toString(),
                            modifier = Modifier.clickable { },
                            color = if (it % 2 != 0) Color.Black else Color.Yellow
                        )
                    }
                }
            }
        }
    }
}

private fun getRealScreenWidth(configuration: Configuration): Dp =
    if (configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
        configuration.screenWidthDp.dp
    } else {
        configuration.screenHeightDp.dp
    }
