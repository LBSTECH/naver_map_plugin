## 0.10.0
- migrate null safety

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
    
    
## 0.9.0
- new feature added!! : ___polygon overlay___
- use polygon overlay to display polygon

## 0.9.1
- ___Breaking Change!!___
    - Changed onCameraChange()'s argument
        - added ***CameraChangeReason*** and ***animated***
    - ***CameraChangeReason*** is Enum. which means the reason of camera view changing
        - CameraChangeReason.developer: moved by API(default)
        - CameraChangeReason.gesture: moved by user's gesture
        - CameraChangeReason.control: moved by button click event
        - CameraChangeReason.location: moved by location tracking mode
    - New Class ***AnchorPoint***
        - use for Marker's anchor property.
        - represent the point on which the icon is referenced.
        
## 0.9.2
- changed iOS target version to 9.0
- issue about how to display openGL (Android)
    - disappear ```isDevMode``` property of ```NaverMap``` 
    - see [README.md](https://github.com/LBSTECH/naver_map_plugin/blob/master/README.md)
    
## 0.9.3
- fix isDebugMode.

## 0.9.4
- change maven repository url. (android)
- new property of naver map: content padding
    - at the previous version, you have to use ```NaverMapController``` to set content Padding.
- allow attributes of overlays(marker, path, polygon, circle) be changed dynamically.
    -  thanks to [#22](https://github.com/LBSTECH/naver_map_plugin/issues/22#issue-813219541)
   
## 0.9.5
- add property of NaverMap - useSurface
    - it decide whether using GLSurfaceView or not in Android.
    - default value is false.
- if you're using lower flutter SDK version than 1.22.2, use this version. 
    - next version will use [hybrid composition](https://flutter.dev/docs/development/platform-integration/platform-views?tab=android-platform-views-java-tab#hybrid-composition)
    - and it require higher flutter version than 1.22.2.
    
## 0.9.6 (hotfix of 0.9.5)
- in iOS small error. so fix it. 
- new method of ```NaverMapController```: ```Future<void> setMapType(MapType type)```