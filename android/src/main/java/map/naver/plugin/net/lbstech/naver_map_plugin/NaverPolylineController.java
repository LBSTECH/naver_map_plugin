package map.naver.plugin.net.lbstech.naver_map_plugin;

import androidx.annotation.NonNull;

import com.naver.maps.map.NaverMap;
import com.naver.maps.map.overlay.Overlay;
import com.naver.maps.map.overlay.PolylineOverlay;

import java.util.HashMap;
import java.util.List;

@SuppressWarnings({"unchecked", "ConstantConditions"})
class NaverPolylineController {
    private final NaverMap naverMap;
    private final Overlay.OnClickListener onClickListener;
    private final float density;

    private HashMap<String, PolylineController> idToController = new HashMap<>();

    NaverPolylineController(NaverMap naverMap, Overlay.OnClickListener onClickListener, float density) {
        this.naverMap = naverMap;
        this.onClickListener = onClickListener;
        this.density = density;
    }

    void set(List polylineData) {
        if (polylineData == null || polylineData.isEmpty()) return;
        for (Object json : polylineData) {
            HashMap<String, Object> data = (HashMap<String, Object>) json;
            String id = (String) data.get("pathOverlayId");
            if (idToController.containsKey(id)) {
                idToController.get(id).interpret(data);
            } else {
                PolylineController controller = new PolylineController(data);
                controller.setOnClickListener(onClickListener);
                idToController.put(controller.id, controller);
                controller.setMap(naverMap);
            }
        }
    }

    void remove(List polylineData) {
        if (polylineData == null || polylineData.isEmpty()) return;
        for (Object json : polylineData) {
            String idToRemove = (String) json;
            PolylineController pathToRemove = idToController.get(idToRemove);
            pathToRemove.setMap(null);
            pathToRemove.setOnClickListener(null);
            idToController.remove(idToRemove);
        }
    }

    class PolylineController {
        @NonNull
        final String id;
        @NonNull
        final PolylineOverlay polyline;

        PolylineController(HashMap<String, Object> option) {
            polyline = new PolylineOverlay();
            this.id = (String) option.get("polylineOverlayId");
            polyline.setTag(this);

            interpret(option);
        }

        void interpret(HashMap<String, Object> option) {
            final List latLngData = (List) option.get("coords");
            if (latLngData != null && latLngData.size() >= 2)
                polyline.setCoords(Convert.toCoords(latLngData));

            final Object globalZIndex = option.get("globalZIndex");
            if (globalZIndex != null) polyline.setGlobalZIndex((int) globalZIndex);

            final Object width = option.get("width");
            if (width != null) polyline.setWidth(Math.round(((int) width) * density));

            final Object color = option.get("color");
            if (color != null) polyline.setColor(Convert.toColorInt(color));

            final Object pattern = option.get("pattern");
            final int[] patternArr = new int[((List) pattern).size()];
            for (int i = 0; i < patternArr.length; i++) {
                patternArr[i] = Math.round(((List<Integer>) pattern).get(i) * density);
            }
            if (pattern != null) polyline.setPattern(patternArr);

            final Object capType = option.get("capType");
            if (capType != null)
                polyline.setCapType(PolylineOverlay.LineCap.values()[(int) capType]);

            final Object joinType = option.get("joinType");
            if (capType != null)
                polyline.setJoinType(PolylineOverlay.LineJoin.values()[(int) joinType]);
        }

        void setMap(NaverMap naverMap) {
            polyline.setMap(naverMap);
        }

        void setOnClickListener(Overlay.OnClickListener listener) {
            polyline.setOnClickListener(listener);
        }
    }
}
