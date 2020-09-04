# naver_map_plugin

네이버지도를 플러터에서 띄울 수 있는 플러그인입니다. 

Plug-in which shows naver map on flutter project support Android and iOS.

## Install

해당 플러그인은 [Naver Cloud PlatForm - map][L1] 에서 제공하는 map서비스를 Android와 iOS 환경에서 보여주는 플러그인입니다. 

[L1]: https://docs.ncloud.com/ko/naveropenapi_v3/maps/overview.html

- Naver cloud platform 에서 콘솔의 AI·Application Service > AI·NAVER API > Application에서 애플리케이션을 등록합니다.
- 등록한 애플리케이션을 선택해 Client ID값을 확인하고 변경 화면에서 Maps가 선택되어 있는지 확인한다.

pubspec.yaml에 plug in dependencies에 작성
``` yaml
dependencies:
  naver_map_plugin: ^0.8.3
```

## ANDROID GETTING START

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

> 대용량 파일을 받기 위해 [git-lfs][L2] 설치가 필요합니다.

[L2]: https://git-lfs.github.com/

```
$ brew install git-lfs
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
	<string>USAGE PERPOSE</string>
	<key>NSLocationAlwaysUsageDescription</key>
	<string>USAGE PERPOSE</string>
	<key>NSLocationWhenInUseUsageDescription</key>
	<string>USAGE PERPOSE</string>
</dict>
```


> 이후 AppDelefate에서 위치 사용권한을 획득하는 예제.
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
