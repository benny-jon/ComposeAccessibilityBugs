# ComposeAccessibilityBugs
 Playground to create examples of Accessibility bugs 

Sample project for the Google Issue: https://issuetracker.google.com/issues/265804603 

## Composable under Test

`HorizontalPager` used here is the one from: `androidx.compose.foundation.pager.HorizontalPager`

## CODE: For the full list of examples look at the file `ListOfExamples.kt`

## Look at EXAMPLE 3

Complex Scenario: Text-Banner + Tabs + [HorizontalPagerWithScrollableContent](https://cs.android.com/androidx/platform/frameworks/support/+/androidx-main:compose/foundation/foundation/samples/src/main/java/androidx/compose/foundation/samples/PagerSamples.kt;drc=8200a13fd2551907d0a7fa99f00b09988821704b;l=276) with variable heights.

### Issues
- Not usable in Landscape mode
- Talkback navigation doesn't scroll the Banner and Tabs. Will be a problem for Landscape.
- Talkback navigation focus is trapped inside the Pages until the user reaches the First or Last page.
- Keyboard navigation doesn't finish the Pager transition from one to another one.
- Keyboard + Talkback navigation is very unstable, when using the Tab key focus separates at end of the first visible elements before scrolling
- Normal Touch swipes: The Pager runs a weird Animation for Page 4 because it has less elements than Page 3 and 5
