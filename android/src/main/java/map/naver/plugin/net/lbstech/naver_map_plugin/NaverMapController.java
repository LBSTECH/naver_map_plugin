package map.naver.plugin.net.lbstech.naver_map_plugin;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;

import com.naver.maps.geometry.LatLng;
import com.naver.maps.geometry.LatLngBounds;
import com.naver.maps.map.CameraAnimation;
import com.naver.maps.map.CameraPosition;
import com.naver.maps.map.CameraUpdate;
import com.naver.maps.map.LocationTrackingMode;
import com.naver.maps.map.MapView;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.NaverMapOptions;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.overlay.LocationOverlay;
import com.naver.maps.map.util.FusedLocationSource;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import io.flutter.Log;
import io.flutter.plugin.common.BinaryMessenger;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.platform.PlatformView;

import static map.naver.plugin.net.lbstech.naver_map_plugin.NaverMapPlugin.CREATED;
import static map.naver.plugin.net.lbstech.naver_map_plugin.NaverMapPlugin.DESTROYED;
import static map.naver.plugin.net.lbstech.naver_map_plugin.NaverMapPlugin.PAUSED;
import static map.naver.plugin.net.lbstech.naver_map_plugin.NaverMapPlugin.RESUMED;
import static map.naver.plugin.net.lbstech.naver_map_plugin.NaverMapPlugin.STARTED;
import static map.naver.plugin.net.lbstech.naver_map_plugin.NaverMapPlugin.STOPPED;

@SuppressWarnings("rawtypes")
public class NaverMapController implements
        PlatformView,
        OnMapReadyCallback,
        MethodChannel.MethodCallHandler,
        Application.ActivityLifecycleCallbacks,
        NaverMapOptionSink{

    private MapView mapView;
    private final AtomicInteger activityState;
    private final MethodChannel methodChannel;
    private final int registrarActivityHashCode;
    private final Activity activity;
    private List initialMarkers;
    private List initialPaths;
    private List initialCircles;
    private List initialPolygons;

    private NaverMap naverMap;
    private boolean disposed = false;
    private MethodChannel.Result mapReadyResult;
    private int locationTrackingMode;

    private final Float density;

    private NaverPathsController pathsController;
    private NaverMarkerController markerController;
    private NaverCircleController circleController;
    private NaverPolygonController polygonController;

    NaverMapController(
            int id,
            Context context,
            AtomicInteger activityState,
            BinaryMessenger binaryMessenger,
            Activity activity,
            NaverMapOptions options,
            List initialMarkers,
            List initialPaths,
            List initialCircles,
            List initialPolygons
    ) {
        this.mapView = new MapView(context, options);

        this.activityState = activityState;
        this.activity = activity;
        this.density = context.getResources().getDisplayMetrics().density;
        this.initialMarkers = initialMarkers;
        this.initialPaths = initialPaths;
        this.initialCircles = initialCircles;
        this.initialPolygons = initialPolygons;

        methodChannel = new MethodChannel(binaryMessenger, "naver_map_plugin_"+ id);
        registrarActivityHashCode = activity.hashCode();

        methodChannel.setMethodCallHandler(this);
    }

    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {
        this.naverMap = naverMap;

        // 제대로 동작하지 않는 컨트롤러 UI로 원인이 밝혀지기 전까진 강제 비활성화.
        this.naverMap.getUiSettings().setZoomControlEnabled(false);
        this.naverMap.getUiSettings().setIndoorLevelPickerEnabled(false);

        // 네이버 로고 선택시 Crash 나는 현상 방지
        this.naverMap.getUiSettings().setLogoClickEnabled(false);

        if (mapReadyResult != null) {
            mapReadyResult.success(null);
            mapReadyResult = null;
        }
        NaverMapListeners listeners = new NaverMapListeners(methodChannel, mapView.getContext(), naverMap);

        naverMap.setOnMapClickListener(listeners);
        naverMap.setOnMapDoubleTapListener(listeners);
        naverMap.setOnMapLongClickListener(listeners);
        naverMap.setOnMapTwoFingerTapListener(listeners);
        naverMap.setOnSymbolClickListener(listeners);
        naverMap.addOnCameraChangeListener(listeners);
        naverMap.addOnCameraIdleListener(listeners);
        naverMap.setLocationSource(new FusedLocationSource(activity, 0xAAFF));
        setLocationTrackingMode(locationTrackingMode);


        // 맵 완전히 만들어진 이후에 오버레이 추가.
        // - 패스
        pathsController = new NaverPathsController(naverMap, listeners, density);
        pathsController.set(initialPaths);

        // - 마커
        markerController = new NaverMarkerController(naverMap, listeners, density, mapView.getContext());
        markerController.add(initialMarkers);

        // - 원형 오버레이
        circleController = new NaverCircleController(naverMap, listeners, density);
        circleController.add(initialCircles);

        // - 폴리곤 오버레이
        polygonController = new NaverPolygonController(naverMap, listeners, density);
        polygonController.add(initialPolygons);

    }

    @Override
    public View getView() {
        return mapView;
    }

    void init() {
        switch (activityState.get()) {
            case STOPPED:
                mapView.onCreate(null);
                mapView.onStart();
                mapView.onResume();
                mapView.onPause();
                mapView.onStop();
                break;
            case PAUSED:
                mapView.onCreate(null);
                mapView.onStart();
                mapView.onResume();
                mapView.onPause();
                break;
            case RESUMED:
                mapView.onCreate(null);
                mapView.onStart();
                mapView.onResume();
                break;
            case STARTED:
                mapView.onCreate(null);
                mapView.onStart();
                break;
            case CREATED:
                mapView.onCreate(null);
                break;
            case DESTROYED:
                // Nothing to do, the activity has been completely destroyed.
                break;
            default:
                throw new IllegalArgumentException(
                        "Cannot interpret " + activityState.get() + " as an activity state");
        }
        activity.getApplication().registerActivityLifecycleCallbacks(this);
        mapView.getMapAsync(this);
    }

    @Override
    public void dispose() {
        if (disposed) return;
        disposed = true;
        naverMap.setLocationTrackingMode(LocationTrackingMode.None);
        methodChannel.setMethodCallHandler(null);
        mapView.onDestroy();
        activity.getApplication().unregisterActivityLifecycleCallbacks(this);
    }

    @SuppressWarnings({"ConstantConditions"})
    @Override
    public void onMethodCall(@NonNull MethodCall methodCall,@NonNull MethodChannel.Result result) {
        switch (methodCall.method){
            case "map#waitForMap":
                {
                    if(naverMap != null){
                        result.success(null);
                        return;
                    }
                    mapReadyResult = result;
                }
                break;
            case "map#update" :
                {
                    Convert.carveMapOptions(this, methodCall.argument("options"));
                    result.success(true);
                }
                break;
            case "map#getVisibleRegion":
                {
                    if (naverMap != null) {
                        LatLngBounds latLngBounds = naverMap.getContentBounds();
                        result.success(Convert.latlngBoundsToJson(latLngBounds));
                    } else result.error("네이버맵 초기화 안됨.",
                            "네이버 지도가 생성되기 전에 이 메서드를 사용할 수 없습니다.",
                            null);
                }
                break;
            case "map#getPosition":
                {
                    if (naverMap != null) {
                        CameraPosition position = naverMap.getCameraPosition();
                        result.success(Convert.cameraPositionToJson(position));
                    } else result.error("네이버맵 초기화 안됨.",
                            "네이버 지도가 생성되기 전에 이 메서드를 사용할 수 없습니다.",
                            null);
                }
                break;
            case "map#getSize" :
                {
                    if(naverMap != null){
                        Map<String, Integer> data = new HashMap<>();
                        data.put("width" , naverMap.getWidth());
                        data.put("height", naverMap.getHeight());
                        result.success(data);
                    }else result.error("네이버맵 초기화 안됨.",
                            "네이버 지도가 생성되기 전에 이 메서드를 사용할 수 없습니다.",
                            null);
                }
                break;
            case "camera#move" :
                {
                    if (naverMap != null) {
                        CameraUpdate update = Convert.toCameraUpdate(methodCall.argument("cameraUpdate"), density);
                        update.animate(CameraAnimation.Easing);
                        naverMap.moveCamera(update);
                        result.success(null);
                    } else result.error("네이버맵 초기화 안됨.",
                            "네이버 지도가 생성되기 전에 이 메서드를 사용할 수 없습니다.",
                            null);
                }
                break;
            case "meter#dp" :
                {
                    if (naverMap == null) {
                        Log.e("getMeterPerDp", "네이버맵이 초기화되지 않았습니다.");
                        result.success(null);
                        break;
                    }
                    result.success(naverMap.getProjection().getMetersPerDp());
                }
                break;
            case "meter#px" :
                {
                    if (naverMap == null) {
                        Log.e("getMeterPerDp", "네이버맵이 초기화되지 않았습니다.");
                        result.success(null);
                        break;
                    }
                    result.success(naverMap.getProjection().getMetersPerPixel());
                }
                break;
            case "markers#update":
                {
                    List markersToAdd = methodCall.argument("markersToAdd");
                    markerController.add(markersToAdd);
                    List markersToChange = methodCall.argument("markersToChange");
                    markerController.modify(markersToChange);
                    List markerIdsToRemove = methodCall.argument("markerIdsToRemove");
                    markerController.remove(markerIdsToRemove);
                    result.success(null);
                }
                break;
            case "pathOverlay#update":
                {
                    List pathToAddOrUpdate = methodCall.argument("pathToAddOrUpdate");
                    List pathToRemove = methodCall.argument("pathIdsToRemove");
                    pathsController.set(pathToAddOrUpdate);
                    pathsController.remove(pathToRemove);
                    result.success(null);
                }
                break;
            case "tracking#mode":
                {
                    if(naverMap != null){
                        int mode = methodCall.argument("locationTrackingMode");
                        switch (mode){
                            case 0:
                                naverMap.setLocationTrackingMode(LocationTrackingMode.None);
                                break;
                            case 1:
                                naverMap.setLocationTrackingMode(LocationTrackingMode.NoFollow);
                                break;
                            case 2:
                                naverMap.setLocationTrackingMode(LocationTrackingMode.Follow);
                                break;
                            default:
                                naverMap.setLocationTrackingMode(LocationTrackingMode.Face);
                                break;
                        }
                        result.success(null);
                    } else result.error("네이버맵 초기화 안됨.",
                            "네이버 지도가 생성되기 전에 이 메서드를 사용할 수 없습니다.",
                            null);
                }
                break;
            case "map#padding" :
                {
                    if (naverMap == null) result.success(null);
                    float left = Convert.toFloat(methodCall.argument("left"));
                    float right = Convert.toFloat(methodCall.argument("right"));
                    float top = Convert.toFloat(methodCall.argument("top"));
                    float bottom = Convert.toFloat(methodCall.argument("bottom"));
                    naverMap.setContentPadding(
                            Math.round(left*density),
                            Math.round(top*density),
                            Math.round(right*density),
                            Math.round(bottom*density));
                    result.success(null);
                }
                break;
            case "map#capture" :
                {
                    if (naverMap == null) result.success(null);
                    naverMap.takeSnapshot(this::treatCapture);
                    result.success(null);
                }
                break;
            case "circleOverlay#update":
                {
                    List circlesToAdd = methodCall.argument("circlesToAdd");
                    List circleIdsToRemove = methodCall.argument("circleIdsToRemove");
                    List circlesToChange = methodCall.argument("circlesToChange");
                    circleController.add(circlesToAdd);
                    circleController.remove(circleIdsToRemove);
                    circleController.modify(circlesToChange);
                    result.success(null);
                }
                break;
            case "polygonOverlay#update":
                {
                    List polygonToAdd = methodCall.argument("polygonToAdd");
                    List polygonToRemove = methodCall.argument("polygonToRemove");
                    List polygonToModify = methodCall.argument("polygonToChange");
                    polygonController.add(polygonToAdd);
                    polygonController.modify(polygonToModify);
                    polygonController.remove(polygonToRemove);
                    result.success(null);
                }
                break;
            case "LO#set#position":
                {
                    if(naverMap != null) {
                        LatLng position = Convert.toLatLng(methodCall.argument("position"));
                        naverMap.getLocationOverlay().setPosition(position);
                        result.success(null);
                    }else result.error("네이버맵 초기화 안됨.",
                            "네이버 지도가 생성되기 전에 이 메서드를 사용할 수 없습니다.",
                            null);
                }
                break;
            case "LO#set#bearing" :
                {
                    if (naverMap != null) {
                        naverMap.getLocationOverlay().setBearing(Convert.toFloat(methodCall.argument("bearing")));
                        result.success(null);
                    }else result.error("네이버맵 초기화 안됨.",
                            "네이버 지도가 생성되기 전에 이 메서드를 사용할 수 없습니다.",
                            null);
                }
                break;
        }
    }

    private void treatCapture(Bitmap snapshot){
        String result = null;
        try {
            File file = File.createTempFile("road", ".jpg", activity.getApplicationContext().getCacheDir());
            FileOutputStream fos = new FileOutputStream(file);
            snapshot.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
            result = file.getPath();
        } catch (IOException e) {
            Log.e("takeCapture", e.getMessage());
        }
        HashMap<String, String> arg = new HashMap<>(2);
        arg.put("path", result);
        methodChannel.invokeMethod("snapshot#done", arg);
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle bundle) {
        if (disposed || activity.hashCode() != registrarActivityHashCode) {
            return;
        }
        mapView.onCreate(bundle);
    }

    @Override
    public void onActivityStarted(Activity activity) {
        if (disposed || activity.hashCode() != registrarActivityHashCode) {
            return;
        }
        mapView.onStart();
    }

    @Override
    public void onActivityResumed(Activity activity) {
        if (disposed || activity.hashCode() != registrarActivityHashCode) {
            return;
        }
        mapView.onResume();
    }

    @Override
    public void onActivityPaused(Activity activity) {
        if (disposed || activity.hashCode() != registrarActivityHashCode) {
            return;
        }
        mapView.onPause();
    }

    @Override
    public void onActivityStopped(Activity activity) {
        if (disposed || activity.hashCode() != registrarActivityHashCode) {
            return;
        }
        mapView.onStop();
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
        if (disposed || activity.hashCode() != registrarActivityHashCode) {
            return;
        }
        mapView.onSaveInstanceState(bundle);
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        if (disposed || activity.hashCode() != registrarActivityHashCode) {
            return;
        }
        mapView.onDestroy();
    }

    @Override
    public void setNightModeEnable(boolean nightModeEnable) {
        naverMap.setNightModeEnabled(nightModeEnable);
    }

    @Override
    public void setLiteModeEnable(boolean liteModeEnable) {
        naverMap.setLiteModeEnabled(liteModeEnable);
    }

    @Override
    public void setIndoorEnable(boolean indoorEnable) {
        naverMap.setIndoorEnabled(indoorEnable);
        naverMap.getUiSettings().setIndoorLevelPickerEnabled(indoorEnable);
    }

    @Override
    public void setMapType(int typeIndex) {
        NaverMap.MapType type;
        switch (typeIndex) {
            case 1:
                type = NaverMap.MapType.Navi;
                break;
            case 2:
                type = NaverMap.MapType.Satellite;
                break;
            case 3:
                type = NaverMap.MapType.Hybrid;
                break;
            case 4:
                type = NaverMap.MapType.Terrain;
                break;
            default:
                type = NaverMap.MapType.Basic;
                break;
        }
        naverMap.setMapType(type);
    }

    @Override
    public void setBuildingHeight(double buildingHeight) {
        naverMap.setBuildingHeight((float) buildingHeight);
    }

    @Override
    public void setSymbolScale(double symbolScale) {
        naverMap.setSymbolScale((float) symbolScale);
    }

    @Override
    public void setSymbolPerspectiveRatio(double symbolPerspectiveRatio) {
        naverMap.setSymbolPerspectiveRatio((float) symbolPerspectiveRatio);
    }

    @Override
    public void setActiveLayers(List activeLayers) {
        naverMap.setLayerGroupEnabled(NaverMap.LAYER_GROUP_BUILDING, false);
        naverMap.setLayerGroupEnabled(NaverMap.LAYER_GROUP_TRAFFIC, false);
        naverMap.setLayerGroupEnabled(NaverMap.LAYER_GROUP_TRANSIT, false);
        naverMap.setLayerGroupEnabled(NaverMap.LAYER_GROUP_BICYCLE, false);
        naverMap.setLayerGroupEnabled(NaverMap.LAYER_GROUP_MOUNTAIN, false);
        naverMap.setLayerGroupEnabled(NaverMap.LAYER_GROUP_CADASTRAL, false);
        for (int i = 0; i < activeLayers.size(); i++) {
            int layerIndex = (int) activeLayers.get(i);
            switch (layerIndex) {
                case 0:
                    naverMap.setLayerGroupEnabled(NaverMap.LAYER_GROUP_BUILDING, true);
                    break;
                case 1:
                    naverMap.setLayerGroupEnabled(NaverMap.LAYER_GROUP_TRAFFIC, true);
                    break;
                case 2:
                    naverMap.setLayerGroupEnabled(NaverMap.LAYER_GROUP_TRANSIT, true);
                    break;
                case 3:
                    naverMap.setLayerGroupEnabled(NaverMap.LAYER_GROUP_BICYCLE, true);
                    break;
                case 4:
                    naverMap.setLayerGroupEnabled(NaverMap.LAYER_GROUP_MOUNTAIN, true);
                    break;
                case 5:
                    naverMap.setLayerGroupEnabled(NaverMap.LAYER_GROUP_CADASTRAL, true);
                    break;
            }
        }
    }

    @Override
    public void setRotationGestureEnable(boolean rotationGestureEnable) {
        naverMap.getUiSettings().setRotateGesturesEnabled(rotationGestureEnable);
    }
    @Override
    public void setScrollGestureEnable(boolean scrollGestureEnable) {
        naverMap.getUiSettings().setScrollGesturesEnabled(scrollGestureEnable);
    }
    @Override
    public void setTiltGestureEnable(boolean tiltGestureEnable) {
        naverMap.getUiSettings().setTiltGesturesEnabled(tiltGestureEnable);
    }
    @Override
    public void setZoomGestureEnable(boolean zoomGestureEnable) {
        naverMap.getUiSettings().setZoomControlEnabled(zoomGestureEnable);
    }
    @Override
    public void setLocationButtonEnable(boolean locationButtonEnable) {
        naverMap.getUiSettings().setLocationButtonEnabled(locationButtonEnable);
    }

    @Override
    public void setLocationTrackingMode(int locationTrackingMode) {
        if(naverMap == null) {
            this.locationTrackingMode = locationTrackingMode;
            return;
        }
        switch (locationTrackingMode){
            case 0:
                naverMap.setLocationTrackingMode(LocationTrackingMode.None);
                break;
            case 1:
                naverMap.setLocationTrackingMode(LocationTrackingMode.NoFollow);
                break;
            case 2:
                naverMap.setLocationTrackingMode(LocationTrackingMode.Follow);
                break;
            case 3:
                naverMap.setLocationTrackingMode(LocationTrackingMode.Face);
                break;
        }
    }
}
