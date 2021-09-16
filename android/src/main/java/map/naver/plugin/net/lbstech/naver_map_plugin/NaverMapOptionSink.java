package map.naver.plugin.net.lbstech.naver_map_plugin;

import java.util.List;

public interface NaverMapOptionSink {
    void setNightModeEnable(boolean nightModeEnable);

    void setLiteModeEnable(boolean liteModeEnable);

    void setIndoorEnable(boolean indoorEnable);

    void setMapType(int typeIndex);

    void setBuildingHeight(double buildingHeight);

    void setSymbolScale(double symbolScale);

    void setSymbolPerspectiveRatio(double symbolPerspectiveRatio);

    void setActiveLayers(List activeLayers);

    void setLocationButtonEnable(boolean locationButtonEnable);

    void setContentPadding(List<Double> paddingData);

    void setMaxZoom(double maxZoom);

    void setMinZoom(double minZoom);

    /**
     * flutter 에서 setState()로 값을 변경해도 반영되지 않는 method. 최초 생성시에만 값변경.
     */
    void setRotationGestureEnable(boolean rotationGestureEnable);

    void setScrollGestureEnable(boolean scrollGestureEnable);

    void setTiltGestureEnable(boolean tiltGestureEnable);

    void setZoomGestureEnable(boolean zoomGestureEnable);

    void setLocationTrackingMode(int locationTrackingMode);

}
