package ducere.lechal.pod.utilities;

import android.util.Log;

import com.here.android.mpa.common.GeoPolyline;
import com.here.android.mpa.common.Image;
import com.here.android.mpa.mapping.MapMarker;
import com.here.android.mpa.search.DiscoveryResult;
import com.here.android.mpa.search.PlaceLink;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ducere.lechal.pod.R;

/**
 * Created by Siva on 25-05-2016.
 */
public class GetWayPoints {
    GeoPolyline geoPolyline;
    List<DiscoveryResult> discoveryResultList;
    List<PlaceLink> placeLinkList = new ArrayList<PlaceLink>();

    public GetWayPoints(GeoPolyline geoPolyline, List<DiscoveryResult> discoveryResultList) {
        this.geoPolyline = geoPolyline;
        this.discoveryResultList = discoveryResultList;
    }

    public List<PlaceLink> getPlaceLinkList(){
        for (DiscoveryResult discoveryResult : discoveryResultList) {

            if (discoveryResult.getResultType() == DiscoveryResult.ResultType.PLACE) {


                final PlaceLink place = (PlaceLink) discoveryResult;
                if (geoPolyline.getNearest(place.getPosition()).distanceTo(place.getPosition())<100){
                    placeLinkList.add(place);
                }

            }

        }
        return placeLinkList;
    }
}
