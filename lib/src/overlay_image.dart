part of naver_map_plugin;

/// 마커에 쓰일 비트맵 이미지를 정의한다.
class OverlayImage{
  const OverlayImage._(this._json);

  Uint8List get blob => Uint8List.fromList([]);

  final dynamic _json;

  dynamic _toJson() => _json;

  /// asset 이미지로 부터 [OverlayImage] 객체를 만든다.
  ///
  ///
  /// 이 함수는 해상도를 고려해서 dpi에 따라 이미지를 올바른 해상도로 스케일링한다.
  static Future<OverlayImage> fromAssetImage(
      ImageConfiguration configuration, String assetName) async {
    if (configuration.devicePixelRatio != null) {
      return OverlayImage._(<dynamic>[
        assetName,
        configuration.devicePixelRatio,
      ]);
    }
    final AssetImage assetImage = AssetImage(assetName);
    final AssetBundleImageKey assetBundleImageKey = await assetImage.obtainKey(configuration);
    return OverlayImage._(<dynamic>[
      assetBundleImageKey.name,
      assetBundleImageKey.scale,
    ]);
  }

}