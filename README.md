# BlobAnimation
[![](https://jitpack.io/v/adityaikhbalm/blobanimation.svg)](https://jitpack.io/#adityaikhbalm/blobanimation) ![GitHub release (latest by date)](https://img.shields.io/github/v/release/adityaikhbalm/blobanimation) [![API](https://img.shields.io/badge/API-21%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=21) [![GitHub license](https://img.shields.io/github/license/adityaikhbalm/blobanimation)](https://github.com/adityaikhbalm/blobanimation/blob/master/LICENSE)

Automate Blob Animation with your own custom to simplify designing Blob Animation for your Android Apps.

<img src="https://github.com/adityaikhbalm/BlobAnimation/blob/master/demo/blob_animation.gif" width="300px" height="550px">

#### Latest version
See the latest released Blob Animation version [here](https://github.com/adityaikhbalm/blobanimation/releases).

## 🔧 Installation
#### Gradle setup
```gradle
// project level gradle
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```
```gradle
// module level gradle
dependencies {
        implementation 'com.github.adityaikhbalm:blobanimation:1.0'
}
```

#### Maven setup
```xml
<!-- <repositories> section of pom.xml -->
<repositories>
  <repository>
      <id>jitpack.io</id>
      <url>https://jitpack.io</url>
  </repository>
</repositories>
```
```xml
<!-- <dependencies> section of pom.xml -->
<dependency>
    <groupId>com.github.adityaikhbalm</groupId>
    <artifactId>blobanimation</artifactId>
    <version>1.0</version>
</dependency>
```

## 💻 Compatibility
 * **Minimum Android SDK**: Requires a minimum API level of 21.

## 🚀️ Getting Started
#### XML Layout
Blob Animation usage is pretty simple, you just need put code in your xml layout.

Default Blob Animation, it will play Blob Animation with red background.
```xml
<com.adityaikhbalm.blobanimation.layout.BlobLayout
        android:layout_width="300dp" // 300dp or whatever you want
        android:layout_height="300dp" // 300dp or whatever you want />
```

Default Blob Animation, it will play Blob Animation with your own style.
```xml
<com.adityaikhbalm.blobanimation.layout.BlobLayout
        android:layout_width="300dp" // 300dp or whatever you want
        android:layout_height="300dp" // 300dp or whatever you want 
        app:blobLayout="@drawable/your_image" // image or color
        app:blobPointCount="16"
        app:blobRadius="200"
        app:blobPlay="true"
        app:blobDuration="2000"
        app:blobOffset="100"
        app:blobInterpolator="@android:anim/decelerate_interpolator" />
```

#### Properties
Blob Animation has default properties and you can modify it with your own style, here are the properties:

| Property | Value Type | Default Value | Information |
| -------- | ---------- | ------------- | ----------- |
| app:blobLayout | reference/color | RED Color | Image or Color of blob layout |
| app:blobPointCount | integer | 7 | Number of curve points in the blob object |
| app:blobRadius | float | 400 | Radius of blob object |
| app:blobOffset | float | 25 | Offset of blob object |
| app:blobDuration | integer | 2000 | Duration of animation |
| app:blobInterpolator | reference | Linear Interpolator | Change corner radius |
| app:blobPlay | boolean | true  | Should animation to play or not |

## ✍️ To-Do-List
- [ ] Support code programmatically
- [ ] Display preview in xml layout

## 📝 License
This project is under MIT License. See the [LICENSE](https://github.com/adityaikhbalm/blobanimation/blob/master/LICENSE) file for details.

