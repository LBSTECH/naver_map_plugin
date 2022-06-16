import 'dart:async';

import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';
import 'package:naver_map_plugin/naver_map_plugin.dart';

class CameraPage extends StatefulWidget {
  @override
  _CameraPageState createState() => _CameraPageState();
}

class _CameraPageState extends State<CameraPage> {
  Completer<NaverMapController> _controller = Completer();
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
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(),
      body: Stack(
        children: [
          _mapView(),
          _trackingModeSelector(),
        ],
      ),
    );
  }

  _mapView() => NaverMap(
        useSurface: kReleaseMode,
        initLocationTrackingMode: LocationTrackingMode.Follow,
        onMapCreated: onMapCreated,
    markers: [
      Marker(markerId: 'start', position:  LatLng(37.56362311822393, 126.9625202364619)),
      Marker(markerId: 'end', position: LatLng(37.571653226035984, 126.9639930919621)),
    ],
      );

  void onMapCreated(NaverMapController controller) {
    if (_controller.isCompleted) _controller = Completer();
    _controller.complete(controller);
  }

  _trackingModeSelector() {
    return Align(
      alignment: Alignment.bottomRight,
      child: GestureDetector(
        onTap: () {
          _controller.future
              .then((value) => value.moveCamera(CameraUpdate.fitBounds(
                    LatLngBounds.fromLatLngList(_coordinates),
                    padding: 48,
                  )));
          print('click');
        },
        child: Container(
          margin: EdgeInsets.only(right: 16, bottom: 48),
          decoration: BoxDecoration(
              borderRadius: BorderRadius.circular(6),
              color: Colors.white,
              boxShadow: [
                BoxShadow(
                  color: Colors.black12,
                  blurRadius: 2,
                )
              ]),
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
}
