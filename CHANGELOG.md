## 0.8.0
- initial publish
Flutter Plug-in which shows naver map on flutter project support Android and iOS.


## 0.8.1

## 0.8.2

## 0.8.3

## 0.8.4
fix iOS camera onIdle delegate,

## 0.8.5
- new feature of NaverMapController
    - get meter per dp/meter per pixel
    - set Content Padding 
- add touch event callback to only android
    - onMapDoubleTap 
    - onMapTwoFingerTap
    - onMapLongTap

## 0.8.6
- ___hotfix___ fix Error 
    - some properties of Marker occur type conversion Error in Android 
    - captionTextSize
    - subCaptionTextSize
    - Alpha

## 0.8.7
- New Class : LocationOverlay
    - location overlay usually represent user's current position. 
    - setPosition : move location overlay to specific LatLng(given by parameter)
    - setBearing : set bearing(azimuth) of location overlay
        - ***0.0 means north***
- fix Error
    - type conversion Error in NaverMapController's getSize()
    
## 0.8.8
- fix Error!
    - onCameraChange not work on IOS [LINK](https://github.com/LBSTECH/naver_map_plugin/issues/8)