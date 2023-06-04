package com.bennyjon.nestedscrollcomposebug.examples

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.semantics.ScrollAxisRange
import androidx.compose.ui.semantics.horizontalScrollAxisRange
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.bennyjon.nestedscrollcomposebug.utils.focusableBorder
import com.google.accompanist.pager.pagerTabIndicatorOffset
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val A11Y_DELAY_MS = 400L

@OptIn(ExperimentalMaterialApi::class, ExperimentalFoundationApi::class)
@Composable
fun ComplexPageWithBottomSheetWithFocusRequester(
    modifier: Modifier = Modifier,
    pages: List<String> = listOf("Tab 1", "Tab 2", "Tab 3", "Tab 4", "Tab 5"),
    sizes: List<Int> = listOf(10, 17, 10, 3, 12) // real life example with variable Page heights
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
    val pagerState = rememberPagerState()

    ModalBottomSheetLayout(
        modifier = modifier,
        sheetState = sheetState,
        sheetContent = {
            MyBottomSheetContentV3 {
                coroutineScope.launch {
                    sheetState.hide()
                    restoreFocusToButton()
                }
            }
        }) {
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
                            if (it == 3 && page == 0) {
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
                            } else {
                                Text(text = "Text inside HorizontalPager=${it + 1}")
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * This is duplicated in every example so that every example is fully contained in a single file.
 */
@Composable
fun MyBottomSheetContentV3(onClickListener: () -> Unit) {
    Column(modifier = Modifier.height(100.dp)) {
        Button(onClick = onClickListener) {
            Text(text = "Dismiss button sheet")
        }
    }
}
