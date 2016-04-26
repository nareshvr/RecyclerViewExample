package ducere.lechal.pod.beans;

import com.here.android.mpa.common.GeoCoordinate;
import com.here.android.mpa.search.PlaceLink;

import java.io.Serializable;

/**
 * Created by Siva on 26-04-2016.
 */
public class Place {
    String title;
    String vicinity;
    double distance;
    GeoCoordinate geo;

    public Place(PlaceLink placeLink) {
        title= placeLink.getTitle();
        vicinity = placeLink.getVicinity();
        geo = placeLink.getPosition();
        distance = placeLink.getDistance();

    }

    public String getTitle() {
        return title;
    }

    public String getVicinity() {
        return vicinity;
    }

    public double getDistance() {
        return distance;
    }

    public GeoCoordinate getGeo() {
        return geo;
    }
}
