part of naver_map_plugin;

/// 마커에 쓰일 비트맵 이미지를 정의한다.
class OverlayImage {
  final AssetImage? _image;
  final AssetBundleImageKey? _key;
  final String? _imagePath;

  get assetName => Platform.isIOS ? _image?.assetName : _key?.name;

  get imageFilePath => _imagePath;

  const OverlayImage._({image, key, imagePath}) : _image = image, _key = key, _imagePath = imagePath;

  /// ## [assetName] 이미지 중 [configuration]에 맞는 이미지를 찾아 [OverlayImage]객체를 만든다.
  ///
  /// 이 때 [ImageConfiguration.bundle]은 [PlatformAssetBundle]이 된다.
  /// [ImageConfiguration.textDirection]은 null이 된다.
  ///
  /// - .../image.png  ---> android mdpi(1.0x)와 ios @1x 에서의 기본값
  /// - .../1.5x/image.png  ---> android hdpi(1.5x) 에서 기본
  /// - .../2.0x/image.png  ---> android xhdpi(2.0x)와 ios @2x 에서 기본
  /// - .../3.0x/image.png  ---> android xxhdpi(3.0x)와 ios @3x 에서 기본
  /// - .../4.0x/image.png  ---> android xxxhdpi(4.0x)에서 기본
  static Future<OverlayImage> fromAssetImage({
    required String assetName,
    AssetBundle? bundle,
    double? devicePixelRatio,
    Locale? locale,
    TextDirection? textDirection,
    Size? size,
    TargetPlatform? platform,
  }) async {
    final _configuration = ImageConfiguration(
      bundle: bundle,
      devicePixelRatio: devicePixelRatio,
      locale: locale,
      textDirection: textDirection,
      size: size,
      platform: platform ??
          (Platform.isIOS ? TargetPlatform.iOS : TargetPlatform.android),
    );
    final AssetImage assetImage = AssetImage(assetName);
    final AssetBundleImageKey key = await assetImage.obtainKey(_configuration);

    return OverlayImage._(image: assetImage, key: key);
  }

  static OverlayImage fromImageFile(String imagePath) {
    return OverlayImage._(imagePath: imagePath);
  }
}
