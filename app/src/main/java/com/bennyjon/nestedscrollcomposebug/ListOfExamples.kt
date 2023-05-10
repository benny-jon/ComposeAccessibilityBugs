package com.bennyjon.nestedscrollcomposebug

import androidx.compose.runtime.Composable
import com.bennyjon.nestedscrollcomposebug.examples.*

enum class ExamplesDestinations(val text: String, val screen: @Composable () -> Unit) {
    EXAMPLE_1(
        text = "EXAMPLE 1: Scrollable Colum + Tabs + HorizontalPager." +
                "\n(original bug reported where Talkback is broken)",
        screen = { HorizontalPagerWithBrokenTalkBackFocusOrderExample() }
    ),
    EXAMPLE_2(
        text = "EXAMPLE 2: Tabs + HorizontalPager + Scrollable Colum for the Pages. " +
                "\nThis uses part of the recommended Solution by Google. Now talkback focus scrolls " +
                "all the way down to the end of the Page." +
                "\nISSUES:" +
                "\n-Talkback focus is trapped inside the Pages until the user reaches the " +
                "First or Last page." +
                "\n-Keyboard navigation doesn't finish the Pager transition from one to another one." +
                "\n-Keyboard + Talkback together is very unstable, when using the Tab key " +
                "focus separates at end of the first visible elements before scrolling",
        screen = { HorizontalPagerWithSimplifiedSolutionExample() }
    ),
    EXAMPLE_3(
        text = "EXAMPLE 3: Complex Scenario: Text-Banner + Tabs + HorizontalPagerWithScrollableContent with variable heights" +
                "\nISSUES:" +
                "\n-Not usable in Landscape mode" +
                "\n-Talkback navigation doesn't scroll the Banner and Tabs" +
                "\n-Plus all issues from Example 2",
        screen = { HorizontalPagerWithClickableScrollableContentWithBanner() }
    ),

    EXAMPLE_4(
        text = "EXAMPLE FROM GOOGLE: HorizontalPagerWithScrollableContent" +
                "\n(Even when its a simplified example, has most of the issues from Example 2 and 3)",
        screen = { HorizontalPagerWithScrollableContent() }
    ),

    EXAMPLE_5(
        text = "EXAMPLE 5: Screen that opens a Bottom Sheet" +
        "\nISSUE:" +
                "\n-Focus is not restored after closing the Bottom Sheet. It should go back to the button 'Open Bottom Sheet'",
        screen = { SimpleScreenWithBottomSheet() }
    ),

    EXAMPLE_6(
        text = "EXAMPLE 6: Screen that opens a Bottom Sheet, and manually uses RequestFocus with Delays to restore focus",
        screen = { SimpleScreenWithBottomSheetWithFocusRequester() }
    )
}
