import 'dart:async';

import 'package:flutter/material.dart';
import 'package:naver_map_plugin/naver_map_plugin.dart';

class ControllerTest extends StatefulWidget {
  @override
  _ControllerTestState createState() => _ControllerTestState();
}

class _ControllerTestState extends State<ControllerTest> {
  Completer<NaverMapController> _controller = Completer();

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: NaverMap(
        onMapCreated: _onMapCreated,
        initLocationTrackingMode: LocationTrackingMode.Follow,
        locationButtonEnable: true,
      ),
    );
  }

  void _onMapCreated(NaverMapController controller) {
    if (_controller.isCompleted) _controller = Completer();
    _controller.complete(controller);
  }
}
