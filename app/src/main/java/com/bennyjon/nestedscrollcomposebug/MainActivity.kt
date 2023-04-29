package com.bennyjon.nestedscrollcomposebug

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.ScrollAxisRange
import androidx.compose.ui.semantics.horizontalScrollAxisRange
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.bennyjon.nestedscrollcomposebug.ui.theme.NestedScrollComposeBugTheme
import com.google.accompanist.pager.pagerTabIndicatorOffset
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NestedScrollComposeBugTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    HorizontalPagerExample()
                }
            }
        }
    }

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    fun HorizontalPagerExample(
        pages: List<String> = listOf("Tab 1", "Tab 2", "Tab 3"),
        sizes: List<Int> = listOf(10, 20, 7) // real life example with variable Page heights
    ) {
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
}
