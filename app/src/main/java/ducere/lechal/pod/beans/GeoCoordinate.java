package ducere.lechal.pod.beans;

import java.io.Serializable;

/**
 * Created by Siva on 02-05-2016.
 */
public class GeoCoordinate implements Serializable {
    double latitude;
    double longitude;

    public GeoCoordinate(double lat, double lng) {
        this.latitude = lat;
        this.longitude = lng;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}
