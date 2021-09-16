package map.naver.plugin.net.lbstech.naver_map_plugin;

import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;

import com.naver.maps.map.NaverMap;
import com.naver.maps.map.overlay.Overlay;
import com.naver.maps.map.overlay.PolygonOverlay;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SuppressWarnings({"ConstantConditions", "rawtypes", "unchecked"})
public class NaverPolygonController {
    private final NaverMap naverMap;
    private final Overlay.OnClickListener onClickListener;
    private final float density;
    private Handler handler = new Handler(Looper.getMainLooper());

    private HashMap<String, PolygonController> idToController = new HashMap<>();

    public NaverPolygonController(NaverMap naverMap, Overlay.OnClickListener onClickListener, float density) {
        this.naverMap = naverMap;
        this.onClickListener = onClickListener;
        this.density = density;
    }

    void add(List jsonArray) {
        if (jsonArray == null || jsonArray.isEmpty()) return;
        ExecutorService service = Executors.newCachedThreadPool();
        service.execute(()->{
            for (Object json : jsonArray) {
                HashMap<String, Object> data = (HashMap<String, Object>) json;
                PolygonController polygonController = new PolygonController(data);
                polygonController.setOnClickListener(onClickListener);
                idToController.put(polygonController.id, polygonController);
            }
            handler.post(()->{
                List<PolygonController> polygons = new ArrayList(idToController.values());
                for (PolygonController polygon : polygons) {
                    polygon.setMap(naverMap);
                }
            });
        });
        service.shutdown();
    }

    void modify(List jsonArray) {
        if (jsonArray == null || jsonArray.isEmpty()) return;
        for (Object json : jsonArray) {
            HashMap<String, Object> data = (HashMap<String, Object>) json;
            String id = (String) data.get("polygonOverlayId");
            idToController.get(id).interpret(data);
        }
    }

    void remove(List polygonIds) {
        if (polygonIds == null || polygonIds.isEmpty()) return;
        for(Object o : polygonIds) {
            String id = (String) o;
            PolygonController polygonToRemove = idToController.get(id);
            polygonToRemove.setMap(null);
            polygonToRemove.setOnClickListener(null);
            idToController.remove(id);
        }
    }

    class PolygonController{
        @NonNull final String id;
        @NonNull final PolygonOverlay polygon;

        PolygonController(HashMap<String, Object> option){
            polygon = new PolygonOverlay();
            this.id = (String) option.get("polygonOverlayId");
            interpret(option);
            polygon.setTag(this);
        }

        void interpret(HashMap<String, Object> option) {
            final List latLngData = (List) option.get("coords");
            if (latLngData != null && latLngData.size() >= 3)
                polygon.setCoords(Convert.toCoords(latLngData));

            final Object color = option.get("color");
            if (color != null) polygon.setColor(Convert.toColorInt(color));

            final Object outlineColor = option.get("outlineColor");
            if (outlineColor != null)
                polygon.setOutlineColor(Convert.toColorInt(outlineColor));

            final Object width = option.get("outlineWidth");
            if (width != null) polygon.setOutlineWidth(Math.round((int)width * density));

            final Object globalZIndex = option.get("globalZIndex");
            if (globalZIndex != null) polygon.setGlobalZIndex((int)globalZIndex);

            final Object holes = option.get("holes");
            if (holes != null) polygon.setHoles(Convert.toHoles(holes));
        }

        void setMap(NaverMap naverMap) {
            polygon.setMap(naverMap);
        }

        void setOnClickListener(Overlay.OnClickListener listener){
            polygon.setOnClickListener(listener);
        }
    }
}
