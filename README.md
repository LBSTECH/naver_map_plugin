# naver_map_plugin

네이버지도를 플러터에서 띄울 수 있는 플러그인입니다. 

Plug-in which shows naver map on flutter project support Android and iOS.

# **중요 공지** | Important Notice

* **LBSTECH**에서 사용하기 위해 제작하기 시작하였으며 **Naver** 와는 무관함을 알립니다.   
    **LBSTECH**의 서비스를 중단하기 전까지 사후관리됩니다 (개발자가 부족해서 원할히는 못해요 :cry:)
    
    
* 원작자 부재로 [pub.dev](https://pub.dev/packages/naver_map_plugin) 업데이트를 못하고 있습니다.   
    따라서 최신 업데이트를 사용하고자 하시면 번거롭더라도 ```github```을 등록하여 사용하시기를 권장합니다.   
    
* Can`t update to [pub.dev](https://pub.dev/packages/naver_map_plugin). Plz use github url.    

## Install

해당 플러그인은 [Naver Cloud PlatForm - map][L1] 에서 제공하는 map서비스를 Android와 iOS 환경에서 보여주는 플러그인입니다. 

[L1]: https://docs.ncloud.com/ko/naveropenapi_v3/maps/overview.html

- Naver cloud platform 에서 콘솔의 AI·Application Service > AI·NAVER API > Application에서 애플리케이션을 등록합니다.
- 등록한 애플리케이션을 선택해 Client ID값을 확인하고 변경 화면에서 Maps가 선택되어 있는지 확인한다.

pubspec.yaml에 plug in dependencies에 작성

``` yaml
dependencies:
  naver_map_plugin:
    git: https://github.com/LBSTECH/naver_map_plugin.git
```


### Warning
 - 지도에서 제공하는 기본 컨트롤러가 잘 작동하지 않는 문제 (이유를 찾지 못하고 있음)
 - android는 현 위치 버튼만 정상 작동
  
### NOTICE (Android) 
- 한국어
    - 네이버에서 제공하는 SDK의 경우 안드로이드에서 지도를 표시하기 위해 기본값으로 GLSurfaceView를 사용한다.
    hot reload시에 naver map SDK의 binary에서 정확하지 않은 이유로 app crash가 발생한다. 
    reload하지 않는 release version 에서는 성능이 더 좋은 GLSurfaceView를 사용하고, 아닌 경우 hot reload가 가능한 
    TextureView를 사용한다. 
- English
    - NaverMap SDK use GLSurfaceView as default to flush map view in Android. 
    At hot reload, App crash occurs in binary file of naver map SDK caused by unclear reason.
    if you build released version, this plug-in use GLSurfaceView for better performance, 
    otherwise TextureView is used to make hot-reload available.

## ANDROID GETTING START

#### [Naver Cloud Platform - Android 시작 가이드](https://docs.ncloud.com/ko/naveropenapi_v3/maps/android-sdk/v3/start.html)

> AndroidManifest.xml에 지정
``` xml
<manifest>
    <application>
        <meta-data
            android:name="com.naver.maps.map.CLIENT_ID"
            android:value="YOUR_CLIENT_ID_HERE" />
    </application>
</manifest>
```

> naver map에서 현위치탐색 기능을 사용하기 위해서는 AndroidManifest.xml에서 권한을 명시한다.
``` xml
<manifest>
    <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION"/>
</manifest>
```

> 또한 android API level 23(M) 이상의 경우 동적 권한을 필요로 한다. 다음은 동적권한을 요청하는 예제 코드이다.
``` java
if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
    if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
    }
}
```

## iOS GETTING START

#### [Naver Cloud Platform - iOS 시작 가이드](https://docs.ncloud.com/ko/naveropenapi_v3/maps/ios-sdk/v3/start.html)

> 대용량 파일을 받기 위해 [git-lfs][L2] 설치가 필요합니다.

[L2]: https://git-lfs.github.com/

```
$ brew install git-lfs
```

> 그리고 git-lfs을 사용하기 위해 다음의 커맨드를 입력해주세요. lfs 사용 설정이 안될 경우 pod를 통한 dependency가 다운로드 되지않습니다.

```
$ git lfs install
```

> info.plist에 지정
``` 
<dict>
  <key>NMFClientId</key>
  <string>YOUR_CLIENT_ID_HERE</string>
  <key>io.flutter.embedded_views_preview</key>
  <true/>
  <key>NSAllowsArbitraryLoads</key>
  <true/>
</dict>
```

> naver map에서 현위치탐색 기능을 사용하기 위해서는 info.plist에서 권한을 명시한다.
``` 
<dict>
    <key>NSLocationAlwaysAndWhenInUseUsageDescription</key>
    <string>[USAGE PERPOSE]</string>
    <key>NSLocationAlwaysUsageDescription</key>
    <string>[USAGE PERPOSE]</string>
    <key>NSLocationWhenInUseUsageDescription</key>
    <string>[USAGE PERPOSE]</string>
</dict>
```


> 이후 AppDelegate에서 위치 사용권한을 획득하는 예제.
``` swift
if (CLLocationManager.locationServicesEnabled()) {
    switch CLLocationManager.authorizationStatus() {
    case .denied, .notDetermined, .restricted:
        self.manager.requestAlwaysAuthorization()
        break
    default:
        break
    }
}       
```
