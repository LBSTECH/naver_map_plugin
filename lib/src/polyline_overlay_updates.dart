part of naver_map_plugin;

/// [PolylineOverlay] update events to be applied to the [NaverMap].
///
/// Used in [NaverMapController] when the map is updated.
class _PolylineOverlayUpdates {
  Set<PolylineOverlay> polylineOverlaysToAddOrUpdate;
  Set<PolylineOverlayId> polylineOverlayIdsToRemove;

  _PolylineOverlayUpdates._(
    this.polylineOverlaysToAddOrUpdate,
    this.polylineOverlayIdsToRemove,
  );

  factory _PolylineOverlayUpdates.from(
    Set<PolylineOverlay>? previous,
    Set<PolylineOverlay>? current,
  ) {
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
      return currentPathOverlays[id]!;
    }

    final Set<PolylineOverlayId> _pathOverlayIdsToRemove =
        prevPathOverlayIds.difference(currentPathOverlayIds);

    final Set<PolylineOverlay> _pathOverlaysToAddOrModify =
        currentPathOverlayIds.map(idToCurrentPolylineOverlay).toSet();

    return _PolylineOverlayUpdates._(
      _pathOverlaysToAddOrModify,
      _pathOverlayIdsToRemove,
    );
  }

  Map<String, dynamic> get _map => {
        'polylineToAddOrUpdate':
            _serializePolylineOverlaySet(polylineOverlaysToAddOrUpdate),
        'polylineIdsToRemove': polylineOverlayIdsToRemove
            .map<dynamic>((PolylineOverlayId m) => m.value)
            .toList(),
      };

  @override
  bool operator ==(Object other) =>
      identical(this, other) ||
      other is _PolylineOverlayUpdates &&
          runtimeType == other.runtimeType &&
          polylineOverlaysToAddOrUpdate ==
              other.polylineOverlaysToAddOrUpdate &&
          polylineOverlayIdsToRemove == other.polylineOverlayIdsToRemove;

  @override
  int get hashCode =>
      polylineOverlaysToAddOrUpdate.hashCode ^
      polylineOverlayIdsToRemove.hashCode;

  @override
  String toString() {
    return '_PolylineUpdates{polylineToAddOrUpdate: $polylineOverlaysToAddOrUpdate, '
        'polylineIdsToRemove: $polylineOverlayIdsToRemove';
  }
}
