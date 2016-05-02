package ducere.lechal.pod.beans;

import com.here.android.mpa.common.GeoCoordinate;
import com.here.android.mpa.search.PlaceLink;

import java.io.Serializable;

/**
 * Created by Siva on 26-04-2016.
 */
public class Place implements Serializable {
    String title;
    String vicinity;
    double distance;
    double lat;
    double lng;


    public Place(String title, String vicinity, double distance, double lat, double lng) {
        this.title = title;
        this.vicinity = vicinity;
        this.distance = distance;
        this.lat = lat;
        this.lng = lng;
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

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }
}
