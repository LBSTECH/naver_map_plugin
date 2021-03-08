package map.naver.plugin.net.lbstech.naver_map_plugin;

import android.app.Activity;
import android.content.Context;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import io.flutter.plugin.common.BinaryMessenger;
import io.flutter.plugin.common.StandardMessageCodec;
import io.flutter.plugin.platform.PlatformView;
import io.flutter.plugin.platform.PlatformViewFactory;

@SuppressWarnings("rawtypes")
public class NaverMapFactory extends PlatformViewFactory {
    private final AtomicInteger activityState;
    private final BinaryMessenger binaryMessenger;
    private final Activity activity;

    NaverMapFactory(
            AtomicInteger state,
            BinaryMessenger binaryMessenger,
            Activity activity) {
        super(StandardMessageCodec.INSTANCE);
        this.activity = activity;
        this.binaryMessenger = binaryMessenger;
        activityState = state;
    }

    @SuppressWarnings({"unchecked", "ConstantConditions"})
    @Override
    public PlatformView create(Context context, int i, Object args) {
        Map<String, Object> params = (Map<String, Object>) args;
        NaverMapBuilder builder = new NaverMapBuilder();

        if (params.containsKey("initialCameraPosition")) {
            Map<String, Object> initPosition = (Map<String, Object>) params.get("initialCameraPosition");
            if(initPosition != null) builder.setInitialCameraPosition(initPosition);
        }
        if (params.containsKey("options")) {
            Map<String, Object> options = (Map<String, Object>) params.get("options");
            if (options.containsKey("useSurface")) {
                boolean useSurface = (boolean) options.get("useSurface");
                builder.setViewType(useSurface);
            }
            Convert.carveMapOptions(builder, options);
        }
        if (params.containsKey("markers")) {
            builder.setInitialMarkers((List) params.get("markers"));
        }
        if (params.containsKey("paths")) {
            builder.setInitialPaths((List) params.get("paths"));
        }
        if (params.containsKey("circles")) {
            builder.setInitialCircles((List) params.get("circles"));
        }
        if (params.containsKey("polygons")) {
            builder.setInitialPolygon((List) params.get("polygons"));
        }

        return builder.build(
                i,
                context,
                activityState,
                binaryMessenger,
                activity);
    }
}
