package ducere.lechal.pod.beans;

import java.util.List;

/**
 * Created by Siva on 04-05-2016.
 */
public class Navigate {
    GeoCoordinate startLocation;
    GeoCoordinate endLocation;
    List<WayPoints> wayPoints;
    String startTitle,endTitle,startAddress,endAddress;
    int mode;

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public GeoCoordinate getStartLocation() {
        return startLocation;
    }

    public void setStartLocation(GeoCoordinate startLocation) {
        this.startLocation = startLocation;
    }

    public GeoCoordinate getEndLocation() {
        return endLocation;
    }

    public void setEndLocation(GeoCoordinate endLocation) {
        this.endLocation = endLocation;
    }

    public List<WayPoints> getWayPoints() {
        return wayPoints;
    }

    public void setWayPoints(List<WayPoints> wayPoints) {
        this.wayPoints = wayPoints;
    }

    public String getStartTitle() {
        return startTitle;
    }

    public void setStartTitle(String startTitle) {
        this.startTitle = startTitle;
    }

    public String getEndTitle() {
        return endTitle;
    }

    public void setEndTitle(String endTitle) {
        this.endTitle = endTitle;
    }

    public String getStartAddress() {
        return startAddress;
    }

    public void setStartAddress(String startAddress) {
        this.startAddress = startAddress;
    }

    public String getEndAddress() {
        return endAddress;
    }

    public void setEndAddress(String endAddress) {
        this.endAddress = endAddress;
    }
}
