package ducere.lechal.pod.beans;

import com.google.gson.Gson;
import com.here.android.mpa.common.GeoCoordinate;
import com.here.android.mpa.search.PlaceLink;

import java.io.Serializable;

/**
 * Created by Siva on 26-04-2016.
 */
public class Place implements Serializable {
    int id;
    String title;
    String vicinity;
    double distance;
    ducere.lechal.pod.beans.GeoCoordinate geo;
    String mockName;
    int type;
    String placeId;
    boolean isSynced;
    Gson gson;
    long updated;



    public Place(String title, String vicinity, double distance, ducere.lechal.pod.beans.GeoCoordinate geo) {
        this.title = title;
        this.vicinity = vicinity;
        this.distance = distance;
        this.geo = geo;
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

    public ducere.lechal.pod.beans.GeoCoordinate getGeo() {
        return geo;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setVicinity(String vicinity) {
        this.vicinity = vicinity;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public void setGeo(ducere.lechal.pod.beans.GeoCoordinate geo) {
        this.geo = geo;
    }

    public String getMockName() {
        return mockName;
    }

    public void setMockName(String mockName) {
        this.mockName = mockName;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public boolean isSynced() {
        return isSynced;
    }

    public void setIsSynced(boolean isSynced) {
        this.isSynced = isSynced;
    }

    public Gson getGson() {
        return gson;
    }

    public void setGson(Gson gson) {
        this.gson = gson;
    }

    public long getUpdated() {
        return updated;
    }

    public void setUpdated(long updated) {
        this.updated = updated;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
