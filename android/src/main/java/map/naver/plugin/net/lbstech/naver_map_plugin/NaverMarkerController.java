package map.naver.plugin.net.lbstech.naver_map_plugin;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import androidx.annotation.NonNull;

import com.naver.maps.map.NaverMap;
import com.naver.maps.map.overlay.InfoWindow;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.overlay.Overlay;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@SuppressWarnings({"unchecked", "ConstantConditions", "rawtypes"})
class NaverMarkerController {
    private final NaverMap naverMap;
    private final Overlay.OnClickListener onClickListener;
    private final float density;
    private final Context context;

    private HashMap<String, MarkerController> idToController = new HashMap<>();
    private Handler handler = new Handler(Looper.getMainLooper());
    private InfoWindow infoWindow = new InfoWindow();
    private String markerIdOfInfoWindow;

    NaverMarkerController(NaverMap naverMap, Overlay.OnClickListener listener,
                          float density, Context context) {
        this.naverMap = naverMap;
        onClickListener = listener;
        this.density = density;
        this.context = context;
    }

    void add(List jsonArray) {
        if (jsonArray == null || jsonArray.isEmpty()) return;
        ExecutorService service = Executors.newCachedThreadPool();
        service.execute(()->{
            for (Object json : jsonArray) {
                HashMap<String, Object> data = (HashMap<String, Object>) json;
                MarkerController marker = new MarkerController(data);
                marker.setOnClickListener(onClickListener);
                idToController.put(marker.id, marker);
            }
            handler.post(()->{
               List<MarkerController> markers = new ArrayList(idToController.values());
                for (MarkerController marker : markers) {
                    marker.setMap(naverMap);
                }
            });
        });
        service.shutdown();
    }

    void remove(List jsonArray) {
        if (jsonArray == null || jsonArray.isEmpty()) return;
        for (Object json : jsonArray) {
            String id = (String) json;
            MarkerController marker = idToController.get(id);
            marker.setOnClickListener(null);
            marker.setMap(null);
            idToController.remove(id);
        }
    }

    void modify(List jsonArray) {
        if (jsonArray == null || jsonArray.isEmpty()) return;
        for (Object json : jsonArray) {
            HashMap<String, Object> data = (HashMap<String, Object>) json;
            String id = (String) data.get("markerId");
            if (idToController.containsKey(id) && idToController.get(id) != null)
                idToController.get(id).interpret(data);
        }
    }

    class MarkerController{
        @NonNull final String id;
        @NonNull final Marker marker;
        private String infoWindowText;

        MarkerController(HashMap<String, Object> jsonArray){
            marker = new Marker();
            id = (String) jsonArray.get("markerId");

            // onClickListener 에서 controller 객체 참조하기 위함.
            marker.setTag(this);

            interpret(jsonArray);
        }

        void interpret(HashMap<String, Object> json) {
            DisplayMetrics metrics = new DisplayMetrics();
            WindowManager mgr = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
            mgr.getDefaultDisplay().getMetrics(metrics);

            final float density = metrics.density;

            final Object position = json.get("position");
            if (position != null) marker.setPosition(Convert.toLatLng(position));

            final Object alpha = json.get("alpha");
            if (alpha != null) marker.setAlpha(Convert.toFloat(alpha));

            final Object flat = json.get("flat");
            if (flat != null) marker.setFlat((boolean) flat);

            final Object anchor = json.get("anchor");
            if (anchor != null) marker.setAnchor(Convert.toPoint(anchor));

            final Object captionText = json.get("captionText");
            if (captionText != null) marker.setCaptionText((String) captionText);

            final Object captionTextSize = json.get("captionTextSize");
            if (captionTextSize != null) marker.setCaptionTextSize(Convert.toFloat(captionTextSize));

            final Object captionColor = json.get("captionColor");
            if (captionColor != null) marker.setCaptionColor(Convert.toColorInt(captionColor));

            final Object captionHaloColor = json.get("captionHaloColor");
            if (captionHaloColor != null) marker.setCaptionHaloColor(Convert.toColorInt(captionHaloColor));

            final Object width = json.get("width");
            if (width != null) marker.setWidth(Math.round((int)width * NaverMarkerController.this.density));

            final Object height = json.get("height");
            if (height != null) marker.setHeight(Math.round((int)height * NaverMarkerController.this.density));

            final Object maxZoom = json.get("maxZoom");
            if (maxZoom != null) marker.setMaxZoom((double)maxZoom);

            final Object minZoom = json.get("minZoom");
            if (minZoom != null) marker.setMinZoom((double)minZoom);

            final Object angle = json.get("angle");
            if (angle != null) marker.setAngle(Convert.toFloat(angle));

            final Object captionRequestedWidth = json.get("captionRequestedWidth");
            if (captionRequestedWidth != null) marker.setCaptionRequestedWidth((int) captionRequestedWidth);

            final Object captionMaxZoom = json.get("captionMaxZoom");
            if (captionMaxZoom != null) marker.setCaptionMaxZoom((double) captionMaxZoom);

            final Object captionMinZoom = json.get("captionMinZoom");
            if (captionMinZoom != null) marker.setCaptionMinZoom((double) captionMinZoom);

            final Object captionOffset = json.get("captionOffset");
            if (captionOffset != null) marker.setCaptionOffset(Math.round((int) captionOffset * density));

            final Object captionPerspectiveEnabled = json.get("captionPerspectiveEnabled");
            if (captionPerspectiveEnabled != null)
                marker.setCaptionPerspectiveEnabled((boolean) captionPerspectiveEnabled);

            final Object zIndex = json.get("zIndex");
            if (zIndex != null) marker.setZIndex((int)zIndex);

            final Object globalZIndex = json.get("globalZIndex");
            if (globalZIndex != null) marker.setGlobalZIndex((int)globalZIndex);

            final Object iconTintColor = json.get("iconTintColor");
            if (iconTintColor != null) marker.setIconTintColor(Convert.toColorInt(iconTintColor));

            final Object subCaptionText = json.get("subCaptionText");
            if(subCaptionText != null) marker.setSubCaptionText((String) subCaptionText);

            final Object subCaptionTextSize = json.get("subCaptionTextSize");
            if (subCaptionTextSize != null) marker.setSubCaptionTextSize(Convert.toFloat(subCaptionTextSize));

            final Object subCaptionColor = json.get("subCaptionColor");
            if (subCaptionColor != null) marker.setSubCaptionColor(Convert.toColorInt(subCaptionColor));

            final Object subCaptionHaloColor = json.get("subCaptionHaloColor");
            if (subCaptionHaloColor != null)
                marker.setSubCaptionHaloColor(Convert.toColorInt(subCaptionHaloColor));

            final Object subCaptionRequestedWidth = json.get("subCaptionRequestedWidth");
            if (subCaptionRequestedWidth != null)
                marker.setSubCaptionRequestedWidth(Math.round((int)subCaptionRequestedWidth * NaverMarkerController.this.density));

            final Object icon = json.get("icon");
            if (icon != null) marker.setIcon(Convert.toOverlayImage(icon));

            final Object infoWindow = json.get("infoWindow");
            if (infoWindow != null) this.infoWindowText = (String)infoWindow;
            else this.infoWindowText = null;
        }

        void setMap(NaverMap naverMap) {
            marker.setMap(naverMap);
        }

        void setOnClickListener(Overlay.OnClickListener onClickListener) {
            marker.setOnClickListener(onClickListener);
        }

        void toggleInfoWindow(){
            if (infoWindowText == null) return;
            if (markerIdOfInfoWindow != null && markerIdOfInfoWindow.equals(id)) {
                infoWindow.close();
                markerIdOfInfoWindow = null;
            } else {
                infoWindow.setAdapter(new InfoWindow.DefaultTextAdapter(context){
                    @NonNull
                    @Override
                    public CharSequence getText(@NonNull InfoWindow infoWindow) {
                        return infoWindowText;
                    }
                });
                markerIdOfInfoWindow = id;
                infoWindow.open(marker);
            }
        }
    }
}
