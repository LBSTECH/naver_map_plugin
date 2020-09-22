part of naver_map_plugin;

/// [NaverMap]의 [PolylineOverlayId]에 대한 유일 식별자
///
/// 전역적으로 유일할 필요는 없으며 목록상에서 유일하면 된다.
@immutable
class PolylineOverlayId {
  PolylineOverlayId(this.value) : assert(value != null);

  final String value;

  @override
  bool operator ==(Object other) {
    if (identical(this, other)) return true;
    if (other.runtimeType != runtimeType) return false;
    final PolylineOverlayId typedOther = other;
    return value == typedOther.value;
  }

  @override
  int get hashCode => value.hashCode;

  @override
  String toString() {
    return 'PolylineOverlayId{value: $value}';
  }
}

/// 끝 지점의 모양.
enum LineCap {
  /// 원형. 끝 지점에 지름이 두께만 한 원이 그려집니다.
  round,

  /// 평면. 끝 지점이 좌표에 딱 맞게 잘립니다.
  butt,

  /// 사각형. 끝 지점에 두께의 반만큼의 직사각형이 추가됩니다.
  square,
}

/// 연결점의 모양.
enum LineJoin {
  /// 미터. 연결점이 뾰족하게 그려집니다.
  miter,

  /// 사면. 연결점에서 뾰족하게 튀어나온 부분이 잘려나갑니다.
  bevel,

  /// 원형. 연결점이 둥글게 그려집니다.
  round,
}

/// 지도에 선을 나타내는 오버레이.
class PolylineOverlay {
  /// 기본 전역 Z 인덱스.
  static const defaultGlobalZIndex = -200000;

  /// 객체 식별자.
  final PolylineOverlayId polylineOverlayId;

  /// 좌표열.
  ///
  /// 배열의 길이가 2보다 작을 수 없습니다.
  final List<LatLng> coords;

  /// 전역 Z 인덱스.
  ///
  /// 여러 오버레이가 화면에서 겹쳐지면 전역 Z 인덱스가 큰 오버레이가 작은 오버레이를 덮습니다.
  /// 또한 값이 0 이상이면 오버레이가 심벌 위에, 0 미만이면 심벌 아래에 그려집니다.
  ///
  /// 기본값은 [defaultGlobalZIndex]입니다.
  final int globalZIndex;

  /// 두께.
  ///
  /// 기본값은 5입니다.
  final int width;

  /// 색상.
  ///
  /// 기본값은 [Colors.black]입니다.
  final Color color;

  /// 점선 패턴.
  ///
  /// 패턴은 픽셀 단위의 배열로 표현되며, 각각 2n번째 요소는 실선의 길이, 2n + 1번째 요소는 공백의 길이를 의미합니다.
  /// 빈 배열일 경우 실선이 됩니다.
  ///
  /// 기본값은 빈 배열입니다.
  final List<int> pattern;

  /// 끝 지점의 모양.
  ///
  /// 기본값은 [LineCap.butt]입니다.
  final LineCap capType;

  /// 연결점의 모양.
  ///
  /// 기본값은 [LineJoin.miter]입니다.
  final LineJoin joinType;

  /// 좌표열을 지정하는 생성자.
  ///
  /// 만약 [coords]의 크기가 2 미만이면 ArgumentError가 발생합니다.
  PolylineOverlay(
    this.polylineOverlayId,
    this.coords, {
    this.globalZIndex = defaultGlobalZIndex,
    this.width = 5,
    this.color = Colors.black,
    this.pattern = const [],
    this.capType = LineCap.butt,
    this.joinType = LineJoin.miter,
  }) {
    if (coords.length < 2) {
      throw ArgumentError.value(
        this.coords,
        'coords',
        'The length of coords cannot be less than 2.',
      );
    }
  }

  /// 같은 값을 지닌 새로운 [PolylineOverlay] 객체를 생성합니다.
  factory PolylineOverlay.clone(PolylineOverlay other) => PolylineOverlay(
        other.polylineOverlayId,
        other.coords,
        color: other.color,
        width: other.width,
        capType: other.capType,
        globalZIndex: other.globalZIndex,
        joinType: other.joinType,
        pattern: other.pattern,
      );

  /// 오버레이가 차지하는 영역을 반환합니다.
  ///
  /// [coords]의 영역과 동일합니다.
  LatLngBounds get bounds => LatLngBounds.fromLatLngList(coords);

  Map<String, dynamic> get _map => {
        'polylineOverlayId': polylineOverlayId.value,
        'coords': coords.map((coord) => coord.json).toList(),
        'globalZIndex': globalZIndex,
        'width': width,
        'color': color.value,
        'pattern': pattern,
        'capType': capType.index,
        'joinType': joinType.index,
      };
}

Map<PolylineOverlayId, PolylineOverlay> _keyByPolylineOverlayId(
    Iterable<PolylineOverlay> polylineOverlays) {
  if (polylineOverlays == null) return {};

  return Map<PolylineOverlayId, PolylineOverlay>.fromEntries(
    polylineOverlays.map(
      (polylineOverlay) => MapEntry(
        polylineOverlay.polylineOverlayId,
        PolylineOverlay.clone(polylineOverlay),
      ),
    ),
  );
}

List<Map<String, dynamic>> _serializePolylineOverlaySet(
    Set<PolylineOverlay> polylineOverlays) {
  if (polylineOverlays == null) return null;

  return polylineOverlays
      .map<Map<String, dynamic>>((PolylineOverlay p) => p._map)
      .toList();
}
