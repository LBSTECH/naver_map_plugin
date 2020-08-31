import 'dart:async';
import 'dart:io';

import 'package:flutter/material.dart';
import 'package:naver_map_plugin/naver_map_plugin.dart';

class BaseMapPage extends StatefulWidget {
  @override
  _BaseMapPageState createState() => _BaseMapPageState();
}

class _BaseMapPageState extends State<BaseMapPage> {
  Completer<NaverMapController> _controller = Completer();

  MapType _mapType = MapType.Basic;
  LocationTrackingMode _trackingMode = LocationTrackingMode.NoFollow;

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Stack(
        children: <Widget>[
          NaverMap(
            initialCameraPosition: CameraPosition(
              target: LatLng(37.566570, 126.978442),
              zoom: 17
            ),
            onMapCreated: onMapCreated,
            mapType: _mapType,
            initLocationTrackingMode: _trackingMode,
            indoorEnable: true,
            onCameraChange: _onCameraChange,
            onCameraIdle: _onCameraIdle,
          ),

          _mapTypeSelector(),
          _trackingModeSelector(),
          _toMyLocationButton(),
        ],
      ),
    );
  }

  _mapTypeSelector() {
    return Align(
      alignment: Alignment.topRight,
      child: Container(
        padding: EdgeInsets.all(16),
        margin: EdgeInsets.only(top: 36),
        child: Column(
          mainAxisSize: MainAxisSize.min,
          crossAxisAlignment: CrossAxisAlignment.end,
          children: MapType.values.map((e) {
            String title;
            switch (e) {
              case MapType.Basic:
                title = '기본';
                break;
              case MapType.Navi:
                title = '내비';
                break;
              case MapType.Satellite:
                title = '위성';
                break;
              case MapType.Hybrid:
                title = '위성혼합';
                break;
              case MapType.Terrain:
                title = '지형도';
                break;
            }
            return GestureDetector(
              onTap: () => _onTapTypeSelector(e),
              child: Container(
                decoration: BoxDecoration(
                  color: Colors.white,
                  borderRadius: BorderRadius.circular(6),
                  boxShadow: [
                    BoxShadow(
                      color: Colors.black26,
                      blurRadius: 3
                    )
                  ]
                ),
                margin: EdgeInsets.only(bottom: 16),
                padding: EdgeInsets.symmetric(vertical: 12, horizontal: 12),
                child: Text(
                  title,
                  style: TextStyle(
                    color: Colors.black87,
                    fontWeight: FontWeight.w600,
                    fontSize: 13
                  ),
                ),
              ),
            );
          }).toList(),
        ),
      ),
    );
  }

  _trackingModeSelector() {
    return Align(
      alignment: Alignment.bottomRight,
      child: GestureDetector(
        onTap: _onTapTakeSnapShot,
        child: Container(
          margin: EdgeInsets.only(right: 16, bottom: 48),
          decoration: BoxDecoration(
            borderRadius: BorderRadius.circular(6),
            color: Colors.white,
            boxShadow: [BoxShadow(
              color: Colors.black12,
              blurRadius: 2,
            )]
          ),
          padding: EdgeInsets.all(12),
          child: Column(
            mainAxisSize: MainAxisSize.min,
            crossAxisAlignment: CrossAxisAlignment.center,
            children: <Widget>[
              Icon(
                Icons.photo_camera,
                color: Colors.black54,
              ),
            ],
          ),
        ),
      ),
    );
  }

  _toMyLocationButton() {
    return Align(
      alignment: Alignment.bottomLeft,
      child: GestureDetector(
        onTap: _onTapLocation,
        child: Container(
          decoration: BoxDecoration(
            color: Colors.white,
            borderRadius: BorderRadius.circular(4),
            boxShadow: [
              BoxShadow(
                color: Colors.black12,
                blurRadius: 3
              )
            ]
          ),
          margin: EdgeInsets.only(left: 16, bottom: 80),
          padding: EdgeInsets.all(8),
          child: Icon(
            Icons.my_location,
            color: Colors.black87,
          ),
        ),
      ),
    );
  }


  /// 지도 생성 완료시
  void onMapCreated(NaverMapController controller) {
    if (_controller.isCompleted) _controller = Completer();
    _controller.complete(controller);
  }

  /// 지도 유형 선택시
  void _onTapTypeSelector(MapType type) async{
    if (_mapType != type) {
      setState(() {
        _mapType = type;
      });
    }
  }

  /// my location button
  void _onTapLocation() async{
    final controller = await _controller.future;
    controller.setLocationTrackingMode(LocationTrackingMode.Follow);
  }

  void _onCameraChange(LatLng latLng) {
    print('카메라 움직임. (위치 : ${latLng.latitude}, ${latLng.longitude})');
  }

  void _onCameraIdle() {
    print('카메라 움직임 멈춤');
  }

  /// 지도 스냅샷
  void _onTapTakeSnapShot() async{
    final controller = await _controller.future;
    controller.takeSnapshot((path){
      showDialog(
          context: context,
          builder: (context){
            return AlertDialog(
              contentPadding: EdgeInsets.zero,
              content: path != null ? Image.file(
                File(path),
              ) : Text('path is null!'),
              titlePadding: EdgeInsets.zero,
            );
          }
      );
    });
  }
}
