# EmptyRecyclerView

[![](https://jitpack.io/v/joeShuff/EmptyRecyclerView.svg)](https://jitpack.io/#joeShuff/EmptyRecyclerView)
[![API](https://img.shields.io/badge/API-16%2B-green.svg?style=flat-square)](https://android-arsenal.com/api?level=16)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

EmptyRecyclerView is an android library to give [RecyclerView](https://developer.android.com/reference/android/support/v7/widget/RecyclerView)'s an empty indicator to display on screen to show there is no content in the list.

|       DEMO 1       |        DEMO 2      |
| :----------------: | :----------------: |
| ![](art/DEMO1.gif) | ![](art/DEMO2.gif) |

## Installation

### Gradle

[![](https://jitpack.io/v/joeShuff/EmptyRecyclerView.svg)](https://jitpack.io/#joeShuff/EmptyRecyclerView)
[![API](https://img.shields.io/badge/API-16%2B-green.svg?style=flat-square)](https://android-arsenal.com/api?level=16)

Add to project level `build.gradle`

```gradle
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}
```
Then to app level `build.gradle`
```gradle
dependencies {
    implementation 'com.github.joeShuff:EmptyRecyclerView:VERSION'
}
```

More installation instructions at [Jitpack Page](https://jitpack.io/#joeShuff/EmptyRecyclerView/Tag)


## Usage

### Setup
```xml
<com.joeshuff.emptyrecyclerview.EmptyRecyclerView
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:id="@+id/mainRecyclerView"
        app:empty_layout="@layout/empty_indicator_layout"
/>
```

Then in your activity you can set the adapter and layout manager using

#### Java
```java
EmptyRecyclerView recyclerView = findViewById(R.id.mainRecyclerView);
recyclerView.setAdapter();
recyclerView.setLayoutManager();
```

#### Kotlin
```kotlin
mainRecyclerView.setAdapter()
mainRecyclerView.setLayoutManager()
```

If you need to change anything else in the RecyclerView, use
```kotlin
mainRecyclerView.getRecyclerView()
```

### Features
#### Indicator Listener
You can add an `EmptyViewCreatedListener` to the `EmptyRecyclerView` which will notify you when the empty indicator has been created and everytime it is shown on the screen. It will also pass you a reference to the Empty indicator view so you can do whatever you like with the view. Like in DEMO1 (See table above) where we put the users search term into the empty indicator.

##### Example (Kotlin)
```kotlin
val emptyViewCreatedListener = object: EmptyViewCreatedListener {
        override fun onCreated(view: View) {

        }

        override fun onShown(view: View?) {

        }
    }

emptyRecyclerView.setOnEmptyViewCreatedListener(emptyViewCreatedListener)
```

##### Example (Java)
```java
EmptyViewCreatedListener listener = new EmptyViewCreatedListener() {
        @Override
        public void onCreated(@NotNull View view) {

        }

        @Override
        public void onShown(@Nullable View view) {

        }
    };

emptyRecyclerView.setOnEmptyViewCreatedListener(listener);
```

#### Remove Empty Indicator
You can use the following 2 methods to both show and remove the empty indicator respectively at runtime.

```kotlin
emptyRecyclerView.showEmptyLayout()
emptyRecyclerView.removeEmptyLayout()
```

## Take Note
Please make sure you use `emptyRecyclerView.setAdapter()` and not `emptyRecyclerView.getRecyclerView.setAdapter()` as the EmptyRecyclerView attaches required data observers to correctly display the empty indicator.

## Contributing
Please feel free to contribute to the repository and open pull requests.


## License

```code
Copyright 2019 Joseph Shufflebotham

The MIT License (MIT)

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
```