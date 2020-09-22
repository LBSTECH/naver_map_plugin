part of naver_map_plugin;

/// [PolylineOverlay] update events to be applied to the [NaverMap].
///
/// Used in [NaverMapController] when the map is updated.
class _PolylineOverlayUpdates {
  Set<PolylineOverlay> polylineOverlaysToAddOrUpdate;
  Set<PolylineOverlayId> polylineOverlayIdsToRemove;

  _PolylineOverlayUpdates.from(
      Set<PolylineOverlay> previous, Set<PolylineOverlay> current) {
    if (previous == null) {
      previous = Set<PolylineOverlay>.identity();
    }

    if (current == null) {
      current = Set<PolylineOverlay>.identity();
    }

    final Map<PolylineOverlayId, PolylineOverlay> previousPathOverlays =
        _keyByPolylineOverlayId(previous);
    final Map<PolylineOverlayId, PolylineOverlay> currentPathOverlays =
        _keyByPolylineOverlayId(current);

    final Set<PolylineOverlayId> prevPathOverlayIds =
        previousPathOverlays.keys.toSet();
    final Set<PolylineOverlayId> currentPathOverlayIds =
        currentPathOverlays.keys.toSet();

    PolylineOverlay idToCurrentPolylineOverlay(PolylineOverlayId id) {
      return currentPathOverlays[id];
    }

    final Set<PolylineOverlayId> _pathOverlayIdsToRemove =
        prevPathOverlayIds.difference(currentPathOverlayIds);

    final Set<PolylineOverlay> _pathOverlaysToAddOrModify =
        currentPathOverlayIds.map(idToCurrentPolylineOverlay).toSet();

    polylineOverlaysToAddOrUpdate = _pathOverlaysToAddOrModify;
    polylineOverlayIdsToRemove = _pathOverlayIdsToRemove;
  }

  Map<String, dynamic> get _map => {
        'polylineToAddOrUpdate':
            _serializePolylineOverlaySet(polylineOverlaysToAddOrUpdate),
        'polylineIdsToRemove': polylineOverlayIdsToRemove
            .map<dynamic>((PolylineOverlayId m) => m.value)
            .toList(),
      };

  @override
  bool operator ==(Object other) {
    if (identical(this, other)) return true;
    if (other.runtimeType != runtimeType) return false;
    final _PolylineOverlayUpdates typedOther = other;
    return setEquals(polylineOverlaysToAddOrUpdate,
            typedOther.polylineOverlaysToAddOrUpdate) &&
        setEquals(
            polylineOverlayIdsToRemove, typedOther.polylineOverlayIdsToRemove);
  }

  @override
  int get hashCode =>
      hashValues(polylineOverlaysToAddOrUpdate, polylineOverlayIdsToRemove);

  @override
  String toString() {
    return '_PolylineUpdates{polylineToAddOrUpdate: $polylineOverlaysToAddOrUpdate, '
        'polylineIdsToRemove: $polylineOverlayIdsToRemove';
  }
}
