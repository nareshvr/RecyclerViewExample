package ducere.lechal.pod;

import android.app.Application;

import com.here.android.mpa.guidance.NavigationManager;
import com.here.android.mpa.routing.Route;

import ducere.lechal.pod.beans.Navigate;
import ducere.lechal.pod.beans.Place;

/**
 * Created by sunde on 13-05-2016.
 */
public class LechalApplication extends Application {

    private static LechalApplication lechalApplication;

    public static LechalApplication getInstance() {
        if (lechalApplication == null) {
            lechalApplication = new LechalApplication();
        }
        return lechalApplication;
    }

    /**
     * tells whether app is in navigation mode or standby
     */
    private boolean isNavigating;
    public NavigationManager navigationManager;
    Navigate navigate;
    Place place;
    Route route;
    boolean isTurnTaken1=false,
            isTurnTaken2=false,
            isTurnTaken3=false,
            isTurnTaken4=false,
            isDestinationReached=false;


    public boolean isNavigating() {
        return isNavigating;
    }

    public void setNavigating(boolean navigating) {
        isNavigating = navigating;
    }

    public NavigationManager getNavigationManager() {
        return navigationManager;
    }

    public void setNavigationManager(NavigationManager navigationManager) {
        this.navigationManager = navigationManager;
    }

    public boolean isTurnTaken1() {
        return isTurnTaken1;
    }

    public void setIsTurnTaken1(boolean isTurnTaken1) {
        this.isTurnTaken1 = isTurnTaken1;
    }

    public boolean isTurnTaken2() {
        return isTurnTaken2;
    }

    public void setIsTurnTaken2(boolean isTurnTaken2) {
        this.isTurnTaken2 = isTurnTaken2;
    }

    public boolean isTurnTaken3() {
        return isTurnTaken3;
    }

    public void setIsTurnTaken3(boolean isTurnTaken3) {
        this.isTurnTaken3 = isTurnTaken3;
    }

    public boolean isTurnTaken4() {
        return isTurnTaken4;
    }

    public boolean isDestinationReached() {
        return isDestinationReached;
    }

    public void setIsDestinationReached(boolean isDestinationReached) {
        this.isDestinationReached = isDestinationReached;
    }

    public void setIsTurnTaken4(boolean isTurnTaken4) {
        this.isTurnTaken4 = isTurnTaken4;
    }
    public  void resetTurns(){
        this.isTurnTaken1=false;
        this.isTurnTaken2=false;
        this.isTurnTaken3=false;
        this.isTurnTaken4=false;
    }

    public Place getPlace() {
        return place;
    }

    public void setPlace(Place place) {
        this.place = place;
    }

    public Navigate getNavigate() {
        return navigate;
    }

    public void setNavigate(Navigate navigate) {
        this.navigate = navigate;
    }

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }
}
