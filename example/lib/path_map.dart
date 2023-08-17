import 'dart:async';

import 'package:flutter/material.dart';
import 'package:naver_map_plugin/naver_map_plugin.dart';

class PathMapPage extends StatefulWidget {
  @override
  _PathMapPageState createState() => _PathMapPageState();
}

class _PathMapPageState extends State<PathMapPage> {
  static const MODE_ADD = 0xF1;
  static const MODE_REMOVE = 0xF2;
  static const MODE_NONE = 0xF3;
  int _currentMode = MODE_NONE;

  Completer<NaverMapController> _controller = Completer();
  List<Marker> _markers = [];
  List<LatLng> _coordinates = [
    LatLng(37.56362311822393, 126.9625202364619),
    LatLng(37.56363478283817, 126.96253077811741),
    LatLng(37.56367253174221, 126.96258632920774),
    LatLng(37.56373145188756, 126.96267920737716),
    LatLng(37.56376812996228, 126.96273714413314),
    LatLng(37.563707641893686, 126.96279487284198),
    LatLng(37.56437155880891, 126.96378291348714),
    LatLng(37.56441247892628, 126.96388192370267),
    LatLng(37.564422261525166, 126.96395205678004),
    LatLng(37.56450894460999, 126.96400578656181),
    LatLng(37.5645498411082, 126.96402021359154),
    LatLng(37.56472258564191, 126.96416946045525),
    LatLng(37.56494172653656, 126.96433354882186),
    LatLng(37.565293360320055, 126.96449657646372),
    LatLng(37.565477387978866, 126.9645501768785),
    LatLng(37.56555757875549, 126.96456364373552),
    LatLng(37.56619045819795, 126.96461312615764),
    LatLng(37.566356493660855, 126.9645750235648),
    LatLng(37.56661085944149, 126.96447695375235),
    LatLng(37.56682593673424, 126.96481840153996),
    LatLng(37.56646280105629, 126.9651403195889),
    LatLng(37.56670161835629, 126.96556946887898),
    LatLng(37.56627313001534, 126.96593057531862),
    LatLng(37.56622071333572, 126.9659796400316),
    LatLng(37.56629393820862, 126.9661070714626),
    LatLng(37.56640418855303, 126.96628995005108),
    LatLng(37.56720429708787, 126.96552918924186),
    LatLng(37.56733955391124, 126.96578028472976),
    LatLng(37.56739759350261, 126.96573887898307),
    LatLng(37.5674400651122, 126.96578273748457),
    LatLng(37.56751735132218, 126.96571209265687),
    LatLng(37.56754828408495, 126.96577551016344),
    LatLng(37.56816872593097, 126.96528778500522),
    LatLng(37.5688577077469, 126.96466967767662),
    LatLng(37.568983274748035, 126.96469165302267),
    LatLng(37.56905482779843, 126.96458922676402),
    LatLng(37.569447166648516, 126.96504276965425),
    LatLng(37.569902276010154, 126.96580353974241),
    LatLng(37.569967950740526, 126.96579616307793),
    LatLng(37.570045258943466, 126.96573038498423),
    LatLng(37.5710425543163, 126.96478551240313),
    LatLng(37.571434886264434, 126.96440266611307),
    LatLng(37.5714986071096, 126.9643222611744),
    LatLng(37.57154697455878, 126.96419542373161),
    LatLng(37.57156817212444, 126.96400038279626),
    LatLng(37.571653226035984, 126.9639930919621),
  ];

  double _sliderValue = 1.0;
  int _width = 10;

  @override
  void initState() {
    // _coordinates.forEach((point) {
    //   _markers.add(Marker(
    //     markerId: point.json.toString(),
    //     position: point,
    //     onMarkerTab: _onMarkerTap,
    //   ));
    // });
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return SafeArea(
      child: Scaffold(
        appBar: AppBar(),
        body: Column(
          children: <Widget>[
            _controlPanel(),
            _naverMap(),
          ],
        ),
      ),
    );
  }

  _controlPanel() {
    return Container(
      padding: EdgeInsets.all(16),
      child: Row(
        crossAxisAlignment: CrossAxisAlignment.center,
        children: <Widget>[
          // 추가
          Expanded(
            child: GestureDetector(
              onTap: () => setState(() => _currentMode = MODE_ADD),
              child: Container(
                decoration: BoxDecoration(
                    color:
                        _currentMode == MODE_ADD ? Colors.black : Colors.white,
                    borderRadius: BorderRadius.circular(6),
                    border: Border.all(color: Colors.black)),
                padding: EdgeInsets.all(8),
                margin: EdgeInsets.only(right: 8),
                child: Text(
                  '추가',
                  textAlign: TextAlign.center,
                  style: TextStyle(
                    color:
                        _currentMode == MODE_ADD ? Colors.white : Colors.black,
                    fontWeight: FontWeight.w600,
                    fontSize: 14,
                  ),
                ),
              ),
            ),
          ),

          // 삭제
          Expanded(
            child: GestureDetector(
              onTap: () => setState(() => _currentMode = MODE_REMOVE),
              child: Container(
                decoration: BoxDecoration(
                    color: _currentMode == MODE_REMOVE
                        ? Colors.black
                        : Colors.white,
                    borderRadius: BorderRadius.circular(6),
                    border: Border.all(color: Colors.black)),
                padding: EdgeInsets.all(8),
                margin: EdgeInsets.only(right: 8),
                child: Text(
                  '삭제',
                  textAlign: TextAlign.center,
                  style: TextStyle(
                    color: _currentMode == MODE_REMOVE
                        ? Colors.white
                        : Colors.black,
                    fontWeight: FontWeight.w600,
                    fontSize: 14,
                  ),
                ),
              ),
            ),
          ),

          // none
          GestureDetector(
            onTap: () => setState(() => _currentMode = MODE_NONE),
            child: Container(
              decoration: BoxDecoration(
                  color:
                      _currentMode == MODE_NONE ? Colors.black : Colors.white,
                  borderRadius: BorderRadius.circular(6),
                  border: Border.all(color: Colors.black)),
              padding: EdgeInsets.all(4),
              child: Icon(
                Icons.clear,
                color: _currentMode == MODE_NONE ? Colors.white : Colors.black,
              ),
            ),
          ),
        ],
      ),
    );
  }

  _naverMap() {
    return Expanded(
      child: Stack(
        children: <Widget>[
          NaverMap(
            markers: [
              Marker(
                  markerId: 'start',
                  position: LatLng(37.56362311822393, 126.9625202364619)),
              Marker(
                  markerId: 'end',
                  position: LatLng(37.571653226035984, 126.9639930919621)),
            ],
            onMapCreated: _onMapCreated,
            onMapTap: _onMapTap,
            // markers: _markers,
            pathOverlays: {
              PathOverlay(
                PathOverlayId('path'),
                _coordinates,
                width: _width,
                color: Colors.red,
                outlineColor: Colors.white,
                onPathOverlayTab: (v){
                  print('pathId : '+v.value);
                }
              )
            },
          ),
          Align(
            alignment: Alignment.bottomRight,
            child: Container(
              margin: EdgeInsets.symmetric(vertical: 40, horizontal: 36),
              padding: EdgeInsets.all(8),
              height: MediaQuery.of(context).size.height * 0.1,
              decoration: BoxDecoration(
                  color: Colors.white,
                  borderRadius: BorderRadius.circular(6),
                  boxShadow: [BoxShadow(color: Colors.black12, blurRadius: 3)]),
              child: Slider.adaptive(
                value: _sliderValue,
                onChanged: _onChangeSlider,
                onChangeEnd: _onSliderChangeEnd,
                min: 0.1,
                max: 2.0,
              ),
            ),
          ),
        ],
      ),
    );
  }

  // ================== method ==========================

  void _onMapCreated(NaverMapController controller) async {
    _controller.complete(controller);

    Future.delayed(Duration(milliseconds: 100), () {
      print('start');
      controller.moveCamera(CameraUpdate.fitBounds(
        LatLngBounds.fromLatLngList(_coordinates),
        padding: 48,
      ));
      print('end');
    });
  }

  void _onMapTap(LatLng latLng) {
    if (_currentMode == MODE_ADD) {
      _markers.add(Marker(
        markerId: latLng.json.toString(),
        position: latLng,
        onMarkerTab: _onMarkerTap,
      ));
      _coordinates.add(latLng);
      setState(() {});
    }
  }

  void _onMarkerTap(Marker? marker, Map<String, int?> iconSize) {
    if (_currentMode == MODE_REMOVE && _coordinates.length > 2) {
      setState(() {
        _coordinates.remove(marker?.position);
        _markers.removeWhere((m) => m.markerId == marker?.markerId);
      });
    }
  }

  void _onChangeSlider(double value) {
    setState(() {
      _sliderValue = value;
    });
  }

  void _onSliderChangeEnd(double value) {
    setState(() {
      _width = (value * 10).floor();
    });
  }
}
