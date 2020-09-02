part of naver_map_plugin;

/// 아이콘과 캡션을 이용해 지도 위의 한 지점을 표시하는 오버레이.
class Marker{

  /// 마커의 고유 식별자로 사용되는 값입니다.
  ///
  /// 또한 마커들의 클릭이벤트에서 여러 마커들을 구분하는데에 사용됩니다.
  final String markerId;

  /// 마커를 클릭했을 때 인포윈도우를 보이게 할 수 있습니다.
  /// null 일 경우 마커를 클릭해도 윈도우가 보이지 않습니다.
  final String infoWindow;

  /// 마커의 불투명도를 조절하는 값입니다.
  /// 유효한 값의 범위는 0.0 ~ 1.0 까지이며 1.0일떄 완전 붏투명입니다.
  final double alpha;

  /// 마커를 평평하게 설정할지 여부를 지정합니다.
  ///
  /// 마커가 평평할 경우 지도가 회전하거나 기울어지면 마커 이미지도 함께 회전하거나
  /// 기울어집니다. 단, 마커가 평평하더라도 이미지의 크기는 항상 동일하게 유지됩니다.
  final bool flat;

  /// 오직 클릭 이벤트 리스너가 지정된 오버레이만이 클릭 이벤트를 받을 수 있습니다.
  ///
  /// 예를 들어 마커와 지상 오버레이가 겹쳐져 있고 지상 오버레이에만 클릭 이벤트
  /// 리스너가 지정된 경우, 사용자가 마커를 클릭하더라도 지상 오버레이가 클릭 이벤트를
  /// 받습니다.
  ///
  ///
  /// iconSize 는 'width' 와 'height'의 key값을 가지고 있다.
  OnMarkerTab onMarkerTab;

  /// 좌표를 지정합니다.
  /// -
  /// 만약 position이 유효하지 않은(LatLng.isValid()가 false인) 좌표라면
  /// InvalidCoordinateException이 발생합니다.
  LatLng position;

  /// 캡션의 텍스트를 지정합니다. 빈 문자열일 경우 캡션이 그려지지 않습니다.
  /// -
  /// 기본값은 빈 문자열입니다.
  final String captionText;

  /// 캡션의 크기를 지정합니다.
  final double captionTextSize;

  /// 캡션이 보이는 최대 줌 레벨을 지정합니다.
  ///
  ///
  /// 지도의 줌 레벨이 캡션의 최대 줌 레벨보다 클 경우 아이콘만 나타나고
  /// 캡션은 화면에 나타나지 않으며 이벤트도 받지 못합니다.
  final double captionMaxZoom;

  /// 캡션이 보이는 최소 줌 레벨을 지정합니다.
  ///
  ///
  /// 지도의 줌 레벨이 캡션의 최소 줌 레벨보다 작을 경우 아이콘만 나타나고
  /// 캡션은 화면에 나타나지 않으며 이벤트도 받지 못합니다.
  final double captionMinZoom;

  /// 캡션의 색상을 지정합니다. RGB CODE를 필요로 합니다.
  ///
  ///
  /// 기본값은 [Colors.black]입니다.
  final Color captionColor;

  /// 캡션의 테두리색을 지정합니다. RGB Code를 필요로합니다.
  ///
  ///
  /// 기본값은 [Colors.white] 입니다.
  final Color captionHaloColor;

  /// 캡션의 희망 너비를 지정합니다.
  ///
  ///
  /// 지정할 경우 한 줄의 너비가 희망 너비를 초과하는 캡션 텍스트가 자동으로
  /// 개행됩니다. 자동 개행은 어절 단위로 이루어지므로,
  ///
  /// 하나의 어절이 길 경우 캡션의 너비가 희망 너비를 초과할 수 있습니다.
  /// 0일 경우 너비를 제한하지 않습니다.
  final int captionRequestedWidth;

  /// 아이콘과 캡션 간의 여백을 지정합니다.
  /// </br>
  /// 단위 >> Android: DP, iOS: PT, 기본값은 0입니다.
  final int captionOffset;

  /// 보조 캡션의 텍스트를 지정합니다. 보조 캡션은 주 캡션의 하단에 나타납니다.
  /// 빈 문자열일 경우 보조 캡션이 그려지지 않습니다.
  ///
  ///
  /// 기본값은 빈 문자열입니다.
  final String subCaptionText;

  /// 보조 캡션의 텍스트 크기를 지정합니다.
  final double subCaptionTextSize;

  /// 보조 캡션의 너비를 지정합니다. 지정할 경우 한 줄의 너비가 희망 너비를 초과하는
  /// 캡션 텍스트는 자동으로 개행됩니다. 자동 개행은 어절 단위로 이루어지므로,
  /// 하나의 어절이 길 경우 캡션의 너비가 희망 너비를 초과할 수 있습니다.
  ///
  ///
  /// 0일 경우 너비를 제한하지 않습니다.
  final int subCaptionRequestedWidth;

  /// 보조 캡션의 색상을 지정합니다.
  ///
  ///
  /// 기본값은 [Colors.black]입니다.
  final Color subCaptionColor;

  /// 보조 캡션의 테두리 색상을 지정합니다.
  ///
  ///
  /// 기본값은 [Colors.white]입니다.
  final Color subCaptionHaloColor;

  /// 아이콘의 너비, 높이를 지정합니다.
  /// <br/>
  /// 단위 >> android : dp, iOS: pt
  /// <br/>
  /// 값이 없는 경우 이미지의 너비를 따릅니다.
  final int width, height;

  /// 오버레이가 보이는 최대, 최소 줌 레벨을 지정합니다. 지도의 줌 레벨이
  /// 오버레이의 최대 줌 레벨보다 크거나 지도의 줌 레벨이 오보레이의
  /// 줌 레벨보다 작은 경우 오버레이는 화면에 나타나지 않으며
  /// 이벤트도 받지 못합니다.
  final double maxZoom, minZoom;

  /// 아이콘의 각도를 지정합니다. 각도를 지정하면 아이콘이 해당 각도만큼
  /// 시계 방향으로 회전합니다
  final double angle;

  /// 아이콘을 지정합니다.
  final OverlayImage icon;

  /// 캡션에 원근 효과를 적용할지 여부를 반환합니다.
  /// 원근 효과를 적용할 경우 가까운 캡션은 크게, 먼 캡션은 작게 표시됩니다.
  ///
  ///
  /// 기본값은 false입니다.
  final bool captionPerspectiveEnabled;

  /// 아이콘에 덧입힐 색상을 지정합니다. 덧입힐 색상을 지정하면 덧입힐 색상이
  /// 아이콘 이미지의 색상과 가산 혼합됩니다. 단, 덧입힐 색상의 알파는 무시됩니다.
  ///
  ///
  /// 기본값은 Color.TRANSPARENT입니다.
  final Color iconTintColor;

  /// 보조 Z 인덱스를 지정합니다. 전역 Z 인덱스가 동일한 여러 오버레이가 화면에서
  /// 겹쳐지면 보조 Z 인덱스가 큰 오버레이가 작은 오버레이를 덮습니다.
  final int zIndex;

  /// 전역 Z 인덱스를 지정합니다. 여러 오버레이가 화면에서 겹쳐지면 전역 Z
  /// 인덱스가 큰 오버레이가 작은 오버레이를 덮습니다.
  ///
  /// 또한 값이 0 이상이면 오버레이가 심벌 위에, 0 미만이면 심벌 아래에 그려집니다.
  ///
  ///
  /// 기본값은 DEFAULT_GLOBAL_Z_INDEX입니다
  final int globalZIndex;

  Marker({
    @required this.markerId,
    @required this.position,
    this.infoWindow,
    this.alpha,
    this.flat,
    this.onMarkerTab,
    this.icon,
    this.captionText,
    this.captionTextSize,
    this.captionColor,
    this.captionHaloColor,
    this.width, this.height,
    this.maxZoom, this.minZoom,
    this.angle,
    this.captionRequestedWidth,
    this.captionMaxZoom,
    this.captionMinZoom,
    this.captionOffset,
    this.captionPerspectiveEnabled,
    this.zIndex,
    this.globalZIndex,
    this.iconTintColor,
    this.subCaptionText,
    this.subCaptionTextSize,
    this.subCaptionColor,
    this.subCaptionHaloColor,
    this.subCaptionRequestedWidth
  });

  Map<String, dynamic> _toJson() {
    assert(markerId != null);
    assert(position != null);

    final Map<String, dynamic> json = <String, dynamic>{};

    void addIfPresent(String fieldName, dynamic value) {
      if (value != null) {
        json[fieldName] = value;
      }
    }

    addIfPresent('markerId', markerId);
    addIfPresent('alpha', alpha);
    addIfPresent('flat', flat);
    addIfPresent('position', position?._toJson());
    addIfPresent('captionText', captionText);
    addIfPresent('captionTextSize', captionTextSize);
    addIfPresent('captionColor', captionColor?.value);
    addIfPresent('captionHaloColor', captionHaloColor?.value);
    addIfPresent('width', width);
    addIfPresent('height', height);
    addIfPresent('maxZoom', maxZoom);
    addIfPresent('minZoom', minZoom);
    addIfPresent('angle', angle);
    addIfPresent('captionRequestedWidth', captionRequestedWidth);
    addIfPresent('captionMaxZoom', captionMaxZoom);
    addIfPresent('captionMinZoom', captionMinZoom);
    addIfPresent('captionOffset', captionOffset);
    addIfPresent('captionPerspectiveEnabled', captionPerspectiveEnabled);
    addIfPresent('zIndex', zIndex);
    addIfPresent('globalZIndex', globalZIndex);
    addIfPresent('iconTintColor', iconTintColor?.value);
    addIfPresent('subCaptionText', subCaptionText);
    addIfPresent('subCaptionTextSize', subCaptionTextSize);
    addIfPresent('subCaptionColor', subCaptionColor?.value);
    addIfPresent('subCaptionHaloColor', subCaptionHaloColor?.value);
    addIfPresent('subCaptionRequestedWidth', subCaptionRequestedWidth);
    addIfPresent('icon', icon?._toJson());
    addIfPresent('infoWindow', infoWindow);

    return json;
  }

  @override
  bool operator ==(other) {
    if (identical(this, other)) return true;
    if (other.runtimeType != runtimeType) return false;
    final Marker typedOther = other;
    return markerId == typedOther.markerId &&
        alpha == typedOther.alpha &&
        flat == typedOther.flat &&
        position == typedOther.position &&
        zIndex == typedOther.zIndex &&
        globalZIndex == typedOther.globalZIndex &&
        captionText == typedOther.captionText &&
        captionTextSize == typedOther.captionTextSize &&
        captionColor == typedOther.captionColor &&
        captionHaloColor == typedOther.captionHaloColor &&
        width == typedOther.width &&
        height == typedOther.height &&
        angle == typedOther.angle &&
        maxZoom == typedOther.maxZoom &&
        minZoom == typedOther.minZoom &&
        icon == typedOther.icon &&
        iconTintColor == typedOther.iconTintColor &&
        subCaptionText == typedOther.subCaptionText &&
        subCaptionTextSize == typedOther.subCaptionTextSize &&
        infoWindow == typedOther.infoWindow &&
        onMarkerTab == typedOther.onMarkerTab;
  }

  @override
  int get hashCode => markerId.hashCode;

  /// 같은 값을 가진 새로운 Maker 객체를 반환한다.
  Marker clone(){
    return Marker(
      position: position,
      markerId: markerId,
      width: width,
      height: height,
      alpha: alpha,
      angle: angle,
      captionColor: captionColor,
      captionHaloColor: captionHaloColor,
      captionMaxZoom: captionMaxZoom,
      captionMinZoom: captionMinZoom,
      captionOffset: captionOffset,
      captionPerspectiveEnabled: captionPerspectiveEnabled,
      captionRequestedWidth: captionRequestedWidth,
      captionText: captionText,
      captionTextSize: captionTextSize,
      flat: flat,
      globalZIndex: globalZIndex,
      iconTintColor: iconTintColor,
      maxZoom: maxZoom,
      minZoom: minZoom,
      onMarkerTab: onMarkerTab,
      subCaptionColor: subCaptionColor,
      subCaptionHaloColor: subCaptionHaloColor,
      subCaptionRequestedWidth: subCaptionRequestedWidth,
      subCaptionText: subCaptionText,
      subCaptionTextSize: subCaptionTextSize,
      zIndex: zIndex,
      icon: icon,
      infoWindow: infoWindow,
    );
  }

  @override
  String toString() {
    return 'Marker{markerId: $markerId, alpha: $alpha, '
        'flat: $flat, position: $position, zIndex: $zIndex, '
        'onMarkerTab: $onMarkerTab, infowindow : $infoWindow}';
  }

}

List<Map<String, dynamic>> _serializeMarkerSet(Iterable<Marker> markers) {
  if (markers == null) {
    return null;
  }
  return markers
      .map<Map<String, dynamic>>((Marker m) => m._toJson()).toList();
}

Map<String, Marker> _keyByMarkerId(Iterable<Marker> markers) {
  if (markers == null) {
    return <String, Marker>{};
  }
  return Map<String, Marker>.fromEntries(markers.map((Marker marker) =>
      MapEntry<String, Marker>(marker.markerId, marker.clone())));
}
