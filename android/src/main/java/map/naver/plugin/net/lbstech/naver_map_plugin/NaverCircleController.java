package map.naver.plugin.net.lbstech.naver_map_plugin;

import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;

import com.naver.maps.map.NaverMap;
import com.naver.maps.map.overlay.CircleOverlay;
import com.naver.maps.map.overlay.Overlay;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SuppressWarnings({"unchecked", "ConstantConditions"})
class NaverCircleController {
    private final NaverMap naverMap;
    private final Overlay.OnClickListener listener;
    private final float density;

    private HashMap<String, CircleController> idToController = new HashMap<>();
    private Handler handler = new Handler(Looper.getMainLooper());

    NaverCircleController(NaverMap naverMap, Overlay.OnClickListener listener, float density){
        this.naverMap = naverMap;
        this.listener = listener;
        this.density = density;
    }

    void add(List jsonArray) {
        if (jsonArray == null || jsonArray.isEmpty()) return;
        ExecutorService service = Executors.newCachedThreadPool();
        service.execute(()->{
            for (Object json : jsonArray) {
                HashMap<String, Object> data = (HashMap<String, Object>) json;
                CircleController circle = new CircleController(data);
                circle.setOnClickListener(listener);
                idToController.put(circle.id, circle);
            }
            handler.post(()->{
                List<CircleController> circles = new ArrayList(idToController.values());
                for (CircleController circle : circles) {
                    circle.setMap(naverMap);
                }
            });
        });
        service.shutdown();
    }

    void modify(List jsonArray) {
        if (jsonArray == null || jsonArray.isEmpty()) return;
        for (Object json : jsonArray) {
            HashMap<String, Object> data = (HashMap<String, Object>) json;
            String id = (String) data.get("overlayId");
            idToController.get(id).interpret(data);
        }
    }

    void remove(List jsonArray) {
        if (jsonArray == null || jsonArray.isEmpty()) return;
        for (Object json : jsonArray) {
            String id = (String) json;
            CircleController marker = idToController.get(id);
            marker.setOnClickListener(null);
            marker.setMap(null);
            idToController.remove(id);
        }
    }

    class  CircleController {
        @NonNull final String id;
        @NonNull final CircleOverlay circle;

        CircleController(HashMap<String, Object> json){
            circle = new CircleOverlay();
            this.id = (String) json.get("overlayId");
            circle.setTag(this);

            interpret(json);
        }

        void interpret(HashMap<String, Object> json) {
            if (json.containsKey("center"))
                circle.setCenter(Convert.toLatLng(json.get("center")));
            if (json.containsKey("radius"))
                circle.setRadius((double) json.get("radius"));
            if(json.containsKey("color"))
                circle.setColor(Convert.toColorInt(json.get("color")));
            if(json.containsKey("outlineColor"))
                circle.setOutlineColor(Convert.toColorInt(json.get("outlineColor")));
            if(json.containsKey("outlineWidth"))
                circle.setOutlineWidth(Math.round((int)json.get("outlineWidth")*density));
            if(json.containsKey("zIndex"))
                circle.setZIndex((int)json.get("zIndex"));
            if(json.containsKey("globalZIndex"))
                circle.setGlobalZIndex(((int)json.get("globalZIndex")) + 1);
            if(json.containsKey("minZoom"))
                circle.setMinZoom((int)json.get("minZoom"));
            if(json.containsKey("maxZoom"))
                circle.setMaxZoom((int)json.get("maxZoom"));
        }

        void setMap(NaverMap naverMap){
            circle.setMap(naverMap);
        }

        void setOnClickListener(Overlay.OnClickListener onClickListener){
            circle.setOnClickListener(onClickListener);
        }
    }
}
