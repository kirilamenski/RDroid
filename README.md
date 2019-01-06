## About

RDroid is a free, open source program written in Java to connect to Android devices. Provides the ability to display the screen of an Android device on a PC and remotely control it from a PC via a USB cable.Uses [MonkeyRunner](https://developer.android.com/studio/test/monkeyrunner/) for remote control and [JavaCV](https://github.com/bytedeco/javacv) for streaming video.

## Usage

To run this program you need to install [JVM](https://www.oracle.com/technetwork/java/javase/downloads/index.html) and [ADB](https://developer.android.com/studio/releases/platform-tools).

Download latest JAR file from [release](https://github.com/kirilamenski/RDroid/releases). Go to the folder where you downloaded the jar and run the file:
```shell
java -jar ~./RDroid.jar
```

## Default Hotkeys

| Keyboard shortcut  | Description |
| ------------- | ------------- |
| alt + left arrow  | go back  |
| ctrl + alt + h  | open recent app list  |
| ctrl + alt + l  | go home  |
| ctrl + shift + F10  | mute volume  |
| ctrl + shift + F11  | volume down  |
| ctrl + shift + F12  | volume up  |
| left/top/right/bottom arrow  | navigation  |


## Issues

1) Landscape orientation not supported yet. (Will be fixed)
2) If the size of the displayed screen is not correct, check whether the screen sizes have been changed (some devices such as Samsung-s7, provide the ability to change the resolution of the screen in the settings). To fix it just return screen sizes to original.
3) You can input only english letters :disappointed: (Will be fixed)
4) Xiaomi devices by default blocked input simulation. To use MonkeyRunner, you must enable this option in the developer settings.
5) Multiple connection working but after start connection with first device you should wait until the Android screen appears. After that you can connect with another.
6) When multiple connection opened if you close one connection it close input connection with another. Need to restart. (Will be fixed)
