# SnapKit-android

[SnapKit-android](https://github.com/Swift-Android/SnapKit-Android) (https://github.com/Swift-Android/SnapKit-Android) Android UI layout tools use pure kotlin code like SanpKit in Swift.



You can use this to make UI layouts more sample and fast without nasty xml.

It's easy to use,if you experienced in swfit or Kotlin, you can also see [Snapkit](https://github.com/SnapKit/SnapKit.git) in swift

It's very popular whith more than 10 thousands stars.if you use this tool,nearly we can 

code UI once,and then run in IOS an Android.

For the Siwft-Android organization's Pourpose,  write app with swift once ,then run in in IOS an Android.



### Use SnapKit-android


[Example](https://github.com/Swift-Android/SnapKit-Android/tree/master/app)


#### Screenshots

<p align="center">
  <img width="360" src="https://raw.githubusercontent.com/Swift-Android/SnapKit-Android/master/Screenshots/s1.gif">
</p>



```kotlin
name.snp.makeConstraints {
    it.leftTo(this).offset(10.dip)
    it.topTo(this).offset(50.dip)
    it.rightTo(this).offset(-10.dip)
    it.heightTo (55.dip)
}
nameX.snp.makeConstraints{
    it.topTo(name.snp.bottom).offset(10.dip)
    it.heightTo(name).centerXTo(name)
    it.widthTo(100.dip)
}
nameY.snp.makeConstraints {
    it.heightTo(30.dip).centerYTo(nameX).leftTo(nameX.snp.right).offset(10.dip)
    it.widthTo(80.dip)
}
avatar.snp.makeConstraints {
    it.centerTo(this)
    it.widthTo(50.dip).heightTo(50.dip)
}

scrollView.snp.makeConstraints {
    it.leftTo(this).widthTo(this).topTo(avatar.snp.bottom).bottomTo(this)
}
```

## Releases

Our [change log](http://swift-android.github.io/snapkit-android/changelog/)has release history.

The latest release is available on [Maven Central](https://search.maven.org/swift-android/com.lly.snp/snapkit-android/0.0.1/jar).

```
implementation("com.lly.snp:snapkit-android:0.0.1")
```



### Acknowledgement

Some ideas of the library are drawn from the following projects:

https://github.com/cashapp/contour.git

Special thanks to the above author. If you like the original work, you can use the original project. At the same time, you are welcome to download and experience this project. If you encounter any problems in the process of using it, you are welcome to give feedback.




### MIT License

```
Copyright (c) 2019 swift-android

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```



