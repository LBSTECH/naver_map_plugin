package map.naver.plugin.net.lbstech.naver_map_plugin;

import android.annotation.SuppressLint;
import android.graphics.Color;

import com.naver.maps.geometry.LatLng;
import com.naver.maps.geometry.LatLngBounds;
import com.naver.maps.map.CameraPosition;
import com.naver.maps.map.CameraUpdate;
import com.naver.maps.map.overlay.OverlayImage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.flutter.view.FlutterMain;

class Convert {

    @SuppressWarnings("ConstantConditions")
    static void carveMapOptions(NaverMapOptionSink sink, Map<String, Object> options) {
        if (options.containsKey("mapType"))
            sink.setMapType((Integer) options.get("mapType"));
        if (options.containsKey("liteModeEnable"))
            sink.setLiteModeEnable((Boolean) options.get("liteModeEnable"));
        if (options.containsKey("nightModeEnable"))
            sink.setNightModeEnable((Boolean) options.get("nightModeEnable"));
        if (options.containsKey("indoorEnable"))
            sink.setIndoorEnable((Boolean) options.get("indoorEnable"));
        if (options.containsKey("buildingHeight"))
            sink.setBuildingHeight((Double) options.get("buildingHeight"));
        if (options.containsKey("symbolScale"))
            sink.setSymbolScale((Double) options.get("symbolScale"));
        if (options.containsKey("symbolPerspectiveRatio"))
            sink.setSymbolPerspectiveRatio((Double) options.get("symbolPerspectiveRatio"));
        if (options.containsKey("activeLayers"))
            sink.setActiveLayers((List) options.get("activeLayers"));
        if (options.containsKey("scrollGestureEnable"))
            sink.setScrollGestureEnable((Boolean) options.get("scrollGestureEnable"));
        if (options.containsKey("zoomGestureEnable"))
            sink.setZoomGestureEnable((Boolean) options.get("zoomGestureEnable"));
        if (options.containsKey("rotationGestureEnable"))
            sink.setRotationGestureEnable((Boolean) options.get("rotationGestureEnable"));
        if (options.containsKey("tiltGestureEnable"))
            sink.setTiltGestureEnable((Boolean) options.get("tiltGestureEnable"));
        if (options.containsKey("locationButtonEnable"))
            sink.setLocationButtonEnable((Boolean) options.get("locationButtonEnable"));
        if(options.containsKey("locationTrackingMode"))
            sink.setLocationTrackingMode((Integer) options.get("locationTrackingMode"));
    }

    @SuppressWarnings("unchecked")
    static LatLng toLatLng(Object o) {
        final List<Double> data = (List<Double>) o;
        return new LatLng(data.get(0), data.get(1));
    }

    private static LatLngBounds toLatLngBounds(Object o) {
        if (o == null) {
            return null;
        }
        final List data = (List) o;
        return new LatLngBounds(toLatLng(data.get(0)), toLatLng(data.get(1)));
    }

    @SuppressLint("Assert")
    static CameraPosition toCameraPosition(Map<String, Object> cameraPositionMap) {
        double bearing, tilt, zoom;
        bearing = (double) cameraPositionMap.get("bearing");
        tilt = (double) cameraPositionMap.get("tilt");
        zoom = (double) cameraPositionMap.get("zoom");

        List target = (List) cameraPositionMap.get("target");
        assert target != null && target.size() == 2;
        double lat = (double) target.get(0);
        double lng = (double) target.get(1);
        return new CameraPosition(new LatLng(lat, lng), zoom, tilt, bearing);
    }

    @SuppressWarnings("unchecked")
    static CameraUpdate toCameraUpdate(Object o, float density) {
        final List data = (List) o;
        switch ((String) data.get(0)) {
            case "newCameraPosition":
                Map<String, Object> positionMap = (Map<String, Object>) data.get(1);
                return CameraUpdate.toCameraPosition(toCameraPosition(positionMap));
            case "scrollTo":
                return CameraUpdate.scrollTo(toLatLng(data.get(1)));
            case "zoomIn":
                return CameraUpdate.zoomIn();
            case "zoomOut":
                return CameraUpdate.zoomOut();
            case "zoomBy":
                double amount = (double) data.get(1);
                return CameraUpdate.zoomBy(amount);
            case "zoomTo":
                double level = (double) data.get(1);
                return CameraUpdate.zoomTo(level);
            case "fitBounds":
                int dp = (int) data.get(2);
                int px = Math.round(dp * density);
                return CameraUpdate.fitBounds(toLatLngBounds(data.get(1)), px);
            default:
                throw new IllegalArgumentException("Cannot interpret " + o + " as CameraUpdate");
        }
    }

    static Object cameraPositionToJson(CameraPosition position) {
        if (position == null) {
            return null;
        }
        final Map<String, Object> data = new HashMap<>();
        data.put("bearing", position.bearing);
        data.put("target", latLngToJson(position.target));
        data.put("tilt", position.tilt);
        data.put("zoom", position.zoom);
        return data;
    }

    static Object latlngBoundsToJson(LatLngBounds latLngBounds) {
        final Map<String, Object> arguments = new HashMap<>(2);
        arguments.put("southwest", latLngToJson(latLngBounds.getSouthWest()));
        arguments.put("northeast", latLngToJson(latLngBounds.getNorthEast()));
        return arguments;
    }

    static Object latLngToJson(LatLng latLng) {
        return Arrays.asList(latLng.latitude, latLng.longitude);
    }

    static float toFloat(Object o) {
        return ((Number) o).floatValue();
    }

    static OverlayImage toOverlayImage(Object o) {
        List data = (List) o;
        String assetName = (String) data.get(0);
        String key = FlutterMain.getLookupKeyForAsset(assetName);
        return OverlayImage.fromAsset(key);
    }

    static List<LatLng> toCoords(Object o) {
        final List<?> data = (List) o;
        final List<LatLng> points = new ArrayList<>(data.size());

        for (Object ob : data) {
            final List<?> point = (List) ob;
            points.add(new LatLng(toFloat(point.get(0)), toFloat(point.get(1))));
        }
        return points;
    }

    @SuppressWarnings("MalformedFormatString")
    static int toColorInt(Object value){
        if (value instanceof Long || value instanceof Integer) {
            return Color.parseColor(String.format("#%08x", value));
        }else {
            return 0;
        }
    }

}
