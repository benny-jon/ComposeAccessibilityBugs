package com.bennyjon.nestedscrollcomposebug

import androidx.compose.runtime.Composable
import com.bennyjon.nestedscrollcomposebug.examples.*

enum class ExamplesDestinations(val text: String, val screen: @Composable () -> Unit) {
    EXAMPLE_1(
        text = "EXAMPLE 1: Original code. \n-Talkback Focus doesn't scroll" +
                " to all elements inside the page, it jumps to the next" +
                " Page when it reaches the last visible item",
        screen = { HorizontalPagerWithBrokenTalkBackFocusOrderExample() }
    ),
    EXAMPLE_2(
        text = "EXAMPLE 2: Applies part of the recommended Solution to the Original code. \n" +
                "-Talkback focus is trapped inside the Pages until the user reaches the " +
                "First or Last page. \n" +
                "-Keyboard navigation doesn't finish the Pager transition from one to another one. \n " +
                "-Keyboard + Talkback together is very unstable, when using the Tab key " +
                "focus separates at end of the first visible elements before scrolling",
        screen = { HorizontalPagerWithRecommendedSolutionExample() }
    ),
    EXAMPLE_3(
        text = "EXAMPLE 3: Complex Scenario: Expands the example from Google by making all Items " +
                "clickable and add a tall Banner on Top. \n" +
                "-Not usable in Landscape mode\n" +
                "-Talkback navigation doesn't scroll the Banner and Tabs\n" +
                "-Keyboard navigation has same issues as Example 2",
        screen = { HorizontalPagerWithClickableScrollableContentWithBanner() }
    ),

    EXAMPLE_4(
        text = "EXAMPLE FROM GOOGLE: HorizontalPagerWithScrollableContent\n" +
                "(Also contains all issues from Example 3)",
        screen = { HorizontalPagerWithScrollableContent() }
    )
}
