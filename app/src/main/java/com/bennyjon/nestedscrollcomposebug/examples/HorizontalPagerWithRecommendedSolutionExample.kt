package com.bennyjon.nestedscrollcomposebug.examples

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.ScrollAxisRange
import androidx.compose.ui.semantics.horizontalScrollAxisRange
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.bennyjon.nestedscrollcomposebug.utils.focusableBorder
import com.google.accompanist.pager.pagerTabIndicatorOffset
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HorizontalPagerWithRecommendedSolutionExample(
    pages: List<String> = listOf("Tab 1", "Tab 2", "Tab 3", "Tab 4", "Tab 5"),
    sizes: List<Int> = listOf(10, 17, 10, 3, 12) // real life example with variable Page heights
) {
    // Using here a simplified version of the Recommended Solution by only moving the
    // scroll behavior from the Root Column to the individual Page Columns. I didn't add
    // the nestedScrollConnection to keep it simple.

    // In this example, the focus is able to scroll all the way down to the last element
    // of a Page before jumping to the first element of the next Page.

    // Issue 1: User is not able to easily switch tabs, it has to scroll through all the
    // intermediate Pages to switch Tabs. Example: If user reaches the last element of the Page 3,
    // to be able to go to Page 1, the user has to scroll backwards all the elements of Page 3,
    // Page 2 and Page 1.

    // Issue 2: When navigating using Keyboard + Talkback, the focus separate when it
    // reaches the last visible elements and the list starts scrolling and  when jumping from
    // page to page it ends in an intermediate transition state where you can see 2 Pages at
    // the same time

    // Issue 3: The Pager runs a weird Animation for Page 4 because it has less elements than
    // Page 3 and 5

    val pagerState = rememberPagerState()
    val coroutineScope = rememberCoroutineScope()

    Column {
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
                    modifier = Modifier.focusableBorder(),
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
            Column(Modifier.verticalScroll(rememberScrollState())) {
                repeat(sizes[page]) {
                    Box(
                        modifier = Modifier
                            .height(150.dp)
                            .focusableBorder()
                            .clickable { },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "Text inside HorizontalPager=${it + 1}")
                    }
                }
            }
        }
    }
}
