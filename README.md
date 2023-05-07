# ComposeAccessibilityBugs
 Playground to create examples of Accessibility bugs 

Sample project for the Google Issue: https://issuetracker.google.com/issues/265804603 

## Composable under Test

`HorizontalPager` used here is the one from: `androidx.compose.foundation.pager.HorizontalPager`

## Look at EXAMPLE 3

Complex Scenario: Expands the example from Google by making all Items clickable and add a tall Banner on Top.

### Issues
- Not usable in Landscape mode
- Talkback navigation doesn't scroll the Banner and Tabs. Will be a problem for Landscape.
- Talkback navigation focus is trapped inside the Pages until the user reaches the First or Last page.
- Keyboard navigation doesn't finish the Pager transition from one to another one.
- Keyboard + Talkback navigation is very unstable, when using the Tab key focus separates at end of the first visible elements before scrolling
- Normal Touch swipes: The Pager runs a weird Animation for Page 4 because it has less elements than Page 3 and 5
