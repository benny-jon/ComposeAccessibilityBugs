# ComposeAccessibilityBugs
 Playground to create examples of Accessibility bugs 

Sample project for the Google Issue: https://issuetracker.google.com/issues/265804603 

## Problem

`HorizontalPager` used here is the one from: `androidx.compose.foundation.pager.HorizontalPager`

**Current Behavior:** Talkback Navigation doesn't scroll all the way down in the Page, it currently
jumps to the next Tab (not event focusing on the Tab title).

**Expected Behavior:** Talkback Swipe Navigation should scroll until the end of the page before 
focusing on the next Tab title
