part of naver_map_plugin;

/// 마커에 쓰일 비트맵 이미지를 정의한다.
class OverlayImage {
  final AssetImage image;
  final AssetBundleImageKey key;

  const OverlayImage._(this.image, this.key);

  /// [assetName] 이미지 중 [configuration]에 맞는 이미지를 찾아 [OverlayImage]객체를 만든다.
  ///
  /// [configuration]이 null일 경우 기기 해상도와 플랫폼, [context]를 통해 [ImageConfiguration]을 만들어 이미지를 찾는다.
  /// 이 때 [ImageConfiguration.bundle]은 [PlatformAssetBundle]이 된다.
  /// [context]가 null일 경우 [ImageConfiguration.locale]과 [ImageConfiguration.textDirection]은 null이 된다.
  static Future<OverlayImage> fromAssetImage(
    String assetName, {
    ImageConfiguration configuration,
    BuildContext context,
  }) async {
    final _configuration = configuration ??
        ImageConfiguration(
          devicePixelRatio: window.devicePixelRatio,
          locale: context == null ? null : Localizations.localeOf(context),
          textDirection: context == null ? null : Directionality.of(context),
          platform:
              Platform.isIOS ? TargetPlatform.iOS : TargetPlatform.android,
          bundle: PlatformAssetBundle(),
        );
    final AssetImage assetImage = AssetImage(assetName);
    final AssetBundleImageKey key = await assetImage.obtainKey(_configuration);

    return OverlayImage._(assetImage, key);
  }
}
