package com.bennyjon.nestedscrollcomposebug.examples

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.TabRowDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.ScrollAxisRange
import androidx.compose.ui.semantics.horizontalScrollAxisRange
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.pagerTabIndicatorOffset
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HorizontalPagerWithBrokenTalkBackFocusOrderExample(
    pages: List<String> = listOf("Tab 1", "Tab 2", "Tab 3"),
    sizes: List<Int> = listOf(10, 20, 7) // real life example with variable Page heights
) {
    // In this example the focus jumps from Page N to Page N+1 without scrolling all the way
    // down the elements of the Page N.

    val pagerState = rememberPagerState()
    val coroutineScope = rememberCoroutineScope()

    Column(Modifier.verticalScroll(rememberScrollState())) {
        TabRow(
            // Our selected tab is our current page
            selectedTabIndex = pagerState.currentPage,
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
                    onClick = {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(index)
                        }
                    },
                )
            }
        }

        HorizontalPager(
            pageCount = pages.size,
            state = pagerState,
            modifier = Modifier.semantics {
                horizontalScrollAxisRange = ScrollAxisRange(value = { 0f }, maxValue = { 0f })
            } // This semantics modifier was the suggested fix, but it's also not working
        ) { page ->
            Column {
                repeat(sizes[page]) {
                    Box(modifier = Modifier.height(150.dp)) {
                        Text(text = "Text inside HorizontalPager=$it")
                    }
                }
            }
        }
    }
}
