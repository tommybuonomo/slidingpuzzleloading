# Sliding Puzzle Loading Animation

A pretty loading animation inspired by [this post](https://site.uplabs.com/posts/react-loader-component-with-clip-path-and-animated) on [UpLabs](https://site.uplabs.com/)

![ezgif com-crop 1](https://cloud.githubusercontent.com/assets/15737675/20040418/528c7cfc-a457-11e6-8f80-5a5d0ab902c8.gif) ![ezgif com-crop](https://cloud.githubusercontent.com/assets/15737675/20040372/5055e546-a456-11e6-8bc2-9f586e6ab9c3.gif)
![ezgif com-crop 2](https://cloud.githubusercontent.com/assets/15737675/20040459/4a3b239a-a458-11e6-990a-cdbec76ad2e8.gif)

## How to
#### Gradle
```Gradle
dependencies {
    compile 'com.tbuonomo:slidingpuzzleloading:1.0.1'
}
```
#### In your XML layout
```Xml
<com.tbuonomo.slidingpuzzleloading.SlidingPuzzleView
    android:id="@+id/sliding_puzzle"
    android:layout_width="150dp"
    android:layout_height="100dp"
    app:strokeWidth="1dp"
    app:animationDuration="500"
    app:cornerRadius="4dp"
    app:squaresColor="#2196F3"
    app:strokeColor="@android:color/white"
        />
```

#### In your Java code
```Java
SlidingPuzzleView slidingPuzzleView = (SlidingPuzzleView) findViewById(R.id.sliding_puzzle);
slidingPuzzleView.start();
```

#### Attributes
| Attribute | Description |
| --- | --- |
| `squaresColor` | Color of the squares |
| `cornerRadius` | Corner radius of the squares (by default 3dp) |
| `strokeColor`  | Color of the squares stroke line |
| `strokeWidth`  | Width of the squares stroke line (by default 2dp) |
| `animationDuration` | Step duration of the animation in ms (by default 500) |

## License
    Copyright 2016 Tommy Buonomo
    
    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at
    
        http://www.apache.org/licenses/LICENSE-2.0
    
    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
