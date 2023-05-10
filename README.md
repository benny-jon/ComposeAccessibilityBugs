# ComposeAccessibilityBugs
 Playground to create examples of Accessibility bugs 

Sample project for the Google Issue: https://issuetracker.google.com/issues/265804603 

## Composables under Test

- `HorizontalPager` used here is the one from: `androidx.compose.foundation.pager.HorizontalPager`
- `ModalBottomSheetLayout` 

## Code

For the full list of examples look at the file `ListOfExamples.kt`

# Highlighted Examples

## EXAMPLE 3 - HorizontalPager

Complex Scenario: Built on top of Google Example [HorizontalPagerWithScrollableContent](https://cs.android.com/androidx/platform/frameworks/support/+/androidx-main:compose/foundation/foundation/samples/src/main/java/androidx/compose/foundation/samples/PagerSamples.kt;drc=8200a13fd2551907d0a7fa99f00b09988821704b;l=276) with variable Page heights.
```
Box {
    Colum { 
      Text() // Banner
      TabRow() 
    }
    HorizontalPager {
        Colum (scrollable) { ... }
    }
}
```

### Issues
- Not usable in Landscape mode
- Talkback navigation doesn't scroll the Banner and Tabs out of the screen through the nestedScrollConnnection. Will be a problem for Landscape.
- Talkback navigation focus is trapped inside the Pages until the user reaches the First or Last page.
- Keyboard navigation doesn't finish the Pager transition from one to another one.
- Keyboard + Talkback navigation is very unstable, when using the Tab key focus separates at end of the first visible elements before scrolling


| Touch | Talkback |
|-|-|
| <video src="https://github.com/benny-jon/ComposeAccessibilityBugs/assets/13564205/e8541fe9-5599-4967-b25f-31c876b35f0b"/> | <video src="https://user-images.githubusercontent.com/13564205/236697763-d3c56586-ba06-4632-b285-5fece0ea08d3.mp4 "/> |

| Keyboard | Talkback + Keyboard |
|-|-|
| <video src="https://user-images.githubusercontent.com/13564205/236698023-1a1e0be8-83d4-4e1b-a6a6-248a257934bd.mp4"/> | <video src="https://user-images.githubusercontent.com/13564205/236698254-d5464b5c-373b-406a-8f1e-e2e90931c716.mp4"/> |

### Talkback in Landscape

https://user-images.githubusercontent.com/13564205/236698279-191447be-cfab-4485-9687-d2f55cab3a76.mp4


## EXAMPLE 5 - ModalBottomSheetLayout

### Issue:
Focus is not restored after closing the Bottom Sheet. It should go back to the button 'Open Bottom Sheet'

https://github.com/benny-jon/ComposeAccessibilityBugs/assets/13564205/632ff82e-134e-40db-bef8-b849c746b757




