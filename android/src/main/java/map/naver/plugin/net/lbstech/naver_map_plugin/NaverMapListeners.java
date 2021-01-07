package map.naver.plugin.net.lbstech.naver_map_plugin;

import android.content.Context;
import android.graphics.PointF;

import androidx.annotation.NonNull;

import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraUpdate;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.Symbol;
import com.naver.maps.map.overlay.CircleOverlay;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.overlay.Overlay;
import com.naver.maps.map.overlay.PathOverlay;
import com.naver.maps.map.overlay.PolygonOverlay;

import java.util.HashMap;
import java.util.Map;

import io.flutter.plugin.common.MethodChannel;

public class NaverMapListeners implements
        NaverMap.OnMapClickListener,
        NaverMap.OnSymbolClickListener,
        NaverMap.OnCameraChangeListener,
        NaverMap.OnCameraIdleListener,
        NaverMap.OnMapLongClickListener,
NaverMap.OnMapDoubleTapListener,
NaverMap.OnMapTwoFingerTapListener,
        Overlay.OnClickListener {

    // member variable
    private final MethodChannel channel;
    private final Context context;
    private NaverMap naverMap;

    NaverMapListeners(MethodChannel channel, Context context, NaverMap naverMap) {
        this.channel = channel;
        this.context = context;
        this.naverMap = naverMap;
    }

    @Override
    public void onMapClick(@NonNull PointF pointF, @NonNull LatLng latLng) {
        final Map<String, Object> arguments = new HashMap<>(2);
        arguments.put("position", Convert.latLngToJson(latLng));
        channel.invokeMethod("map#onTap", arguments);
    }

    @Override
    public void onMapLongClick(@NonNull PointF pointF, @NonNull LatLng latLng) {
        final Map<String, Object> arguments = new HashMap<>(2);
        arguments.put("position", Convert.latLngToJson(latLng));
        channel.invokeMethod("map#onLongTap", arguments);
    }

    @Override
    public boolean onMapDoubleTap(@NonNull PointF pointF, @NonNull LatLng latLng) {
        final Map<String, Object> arguments = new HashMap<>(2);
        arguments.put("position", Convert.latLngToJson(latLng));
        channel.invokeMethod("map#onMapDoubleTap", arguments);
        return false;
    }

    @Override
    public boolean onMapTwoFingerTap(@NonNull PointF pointF, @NonNull LatLng latLng) {
        final Map<String, Object> arguments = new HashMap<>(2);
        arguments.put("position", Convert.latLngToJson(latLng));
        channel.invokeMethod("map#onMapTwoFingerTap", arguments);
        return false;
    }

    @Override
    public boolean onSymbolClick(@NonNull Symbol symbol) {
        final Map<String, Object> arguments = new HashMap<>(2);
        arguments.put("position", Convert.latLngToJson(symbol.getPosition()));
        arguments.put("caption", symbol.getCaption());
        channel.invokeMethod("map#onSymbolClick", arguments);
        return true;
    }

    @Override
    public void onCameraChange(int i, boolean b) {
        final Map<String, Object> arguments = new HashMap<>(2);
        LatLng latLng = naverMap.getCameraPosition().target;
        arguments.put("position", Convert.latLngToJson(latLng));

        int reason = 0;
        switch (i) {
            case CameraUpdate.REASON_GESTURE:
                reason = 1;
                break;
            case CameraUpdate.REASON_CONTROL:
                reason = 2;
                break;
            case CameraUpdate.REASON_LOCATION:
                reason = 3;
                break;
        }
        arguments.put("reason", reason);
        arguments.put("animated", b);
        channel.invokeMethod("camera#move", arguments);
    }

    @Override
    public void onCameraIdle() {
        channel.invokeMethod("camera#idle", null);
    }

    @Override
    public boolean onClick(@NonNull Overlay overlay) {
        if (overlay instanceof Marker) {
            NaverMarkerController.MarkerController controller =
                    (NaverMarkerController.MarkerController) overlay.getTag();
            if (controller == null) return true;
            controller.toggleInfoWindow();
            final Map<String, Object> arguments = new HashMap<>(2);
            arguments.put("markerId", controller.id);
            arguments.put("iconWidth", controller.marker.getIcon().getIntrinsicWidth(context));
            arguments.put("iconHeight", controller.marker.getIcon().getIntrinsicHeight(context));

            channel.invokeMethod("marker#onTap", arguments);
            return true;
        } else if (overlay instanceof PathOverlay) {
            NaverPathsController.PathController controller =
                    (NaverPathsController.PathController) overlay.getTag();
            if (controller == null) return true;

            final Map<String, Object> arguments = new HashMap<>(2);
            arguments.put("pathId", controller.id);
            channel.invokeMethod("path#onTap", arguments);
            return true;
        } else if (overlay instanceof CircleOverlay) {
            NaverCircleController.CircleController controller =
                    (NaverCircleController.CircleController) overlay.getTag();
            if (controller == null) return true;

            final Map<String, Object> arguments = new HashMap<>(2);
            arguments.put("overlayId", controller.id);
            channel.invokeMethod("circle#onTap", arguments);
            return true;
        } else if (overlay instanceof PolygonOverlay) {
            NaverPolygonController.PolygonController controller =
                    (NaverPolygonController.PolygonController) overlay.getTag();
            if (controller == null) return true;

            final Map<String, Object> argument = new HashMap<>(2);
            argument.put("polygonOverlayId", controller.id);
            channel.invokeMethod("polygon#onTap", argument);
        }
        return false;
    }
}
