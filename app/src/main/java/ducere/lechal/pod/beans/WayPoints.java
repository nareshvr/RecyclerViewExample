package ducere.lechal.pod.beans;

/**
 * Created by Siva on 04-05-2016.
 */
public class WayPoints {
    GeoCoordinate geo;
    String title;
    String address;


    public WayPoints(GeoCoordinate geo, String title, String address) {
        this.geo = geo;
        this.title = title;
        this.address = address;
    }

    public GeoCoordinate getGeo() {
        return geo;
    }

    public void setGeo(GeoCoordinate geo) {
        this.geo = geo;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
