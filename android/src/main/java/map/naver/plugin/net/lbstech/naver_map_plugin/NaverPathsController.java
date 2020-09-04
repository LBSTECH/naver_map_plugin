package map.naver.plugin.net.lbstech.naver_map_plugin;

import androidx.annotation.NonNull;

import com.naver.maps.map.NaverMap;
import com.naver.maps.map.overlay.Overlay;
import com.naver.maps.map.overlay.PathOverlay;

import java.util.HashMap;
import java.util.List;

@SuppressWarnings({"unchecked", "ConstantConditions"})
class NaverPathsController {
    private final NaverMap naverMap;
    private final Overlay.OnClickListener onClickListener;
    private final float density;

    private HashMap<String, PathController> idToController = new HashMap<>();

    NaverPathsController(NaverMap naverMap, Overlay.OnClickListener onClickListener, float density){
        this.naverMap = naverMap;
        this.onClickListener = onClickListener;
        this.density = density;
    }

    void set(List pathData){
        if (pathData == null || pathData.isEmpty()) return;
        for (Object json : pathData) {
            HashMap<String, Object> data = (HashMap<String, Object>) json;
            String  id = (String) data.get("pathOverlayId");
            if (idToController.containsKey(id)) {
                idToController.get(id).interpret(data);
            }else {
                PathController controller = new PathController(data);
                controller.setOnClickListener(onClickListener);
                idToController.put(controller.id, controller);
                controller.setMap(naverMap);
            }
        }
    }

    void remove(List pathIds){
        if (pathIds == null || pathIds.isEmpty()) return;
        for (Object json : pathIds) {
            String idToRemove = (String) json;
            PathController pathToRemove = idToController.get(idToRemove);
            pathToRemove.setMap(null);
            pathToRemove.setOnClickListener(null);
            idToController.remove(idToRemove);
        }
    }

    class PathController{
        @NonNull final String id;
        @NonNull final PathOverlay path;

        PathController(HashMap<String, Object> option) {
            path = new PathOverlay();
            this.id = (String) option.get("pathOverlayId");
            path.setTag(this);

            interpret(option);
        }

        void interpret(HashMap<String, Object> option){
            final List latLngData = (List) option.get("coords");
            if (latLngData != null && latLngData.size() >= 2)
                path.setCoords(Convert.toCoords(latLngData));

            final Object globalZIndex = option.get("globalZIndex");
            if (globalZIndex != null) path.setGlobalZIndex((int) globalZIndex);

            final Object hideCollidedCaptions = option.get("hideCollidedCaptions");
            if (hideCollidedCaptions != null) path.setHideCollidedCaptions((boolean) hideCollidedCaptions);

            final Object hideCollidedMarkers = option.get("hideCollidedMarkers");
            if (hideCollidedMarkers != null) path.setHideCollidedMarkers((boolean) hideCollidedMarkers);

            final Object hideCollidedSymbols = option.get("hideCollidedSymbols");
            if (hideCollidedSymbols != null) path.setHideCollidedSymbols((boolean) hideCollidedSymbols);

            final Object color = option.get("color");
            if (color != null) path.setColor(Convert.toColorInt(color));

            final Object outlineColor = option.get("outlineColor");
            if (outlineColor != null) path.setOutlineColor(Convert.toColorInt(outlineColor));

            final Object passedColor = option.get("passedColor");
            if (passedColor != null) path.setPassedColor(Convert.toColorInt(passedColor));

            final Object passedOutlineColor = option.get("passedOutlineColor");
            if (passedOutlineColor != null) path.setPassedOutlineColor(Convert.toColorInt(passedOutlineColor));

            final Object patternImage = option.get("patternImage");
            if (patternImage != null) path.setPatternImage(Convert.toOverlayImage(patternImage));

            final Object patternInterval = option.get("patternInterval");
            if (patternInterval != null) path.setPatternInterval(Math.round(((int) patternInterval)*density));

            final Object progress = option.get("progress");
            if (progress != null) path.setProgress((double) progress);

            final Object width = option.get("width");
            if (width != null) path.setWidth(Math.round(((int) width) * density));

            final Object outlineWidth = option.get("outlineWidth");
            if (outlineWidth != null) path.setOutlineWidth(Math.round((int)outlineWidth * density));

        }

        void setMap(NaverMap naverMap) {
            path.setMap(naverMap);
        }

        void setOnClickListener(Overlay.OnClickListener listener){
            path.setOnClickListener(listener);
        }
    }
}
