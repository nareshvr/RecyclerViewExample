package ducere.lechal.pod.utilities;

import com.here.android.mpa.common.GeoCoordinate;
import com.here.android.mpa.guidance.NavigationManager;
import com.here.android.mpa.routing.Maneuver;

/**
 * Created by Siva on 13-05-2016.
 */
public class NavigationFeedback {
    public static GeoCoordinate position;
    NavigationManager navigationManager;
    Maneuver currentManeuver;
    double distanceNext;


    public NavigationFeedback(GeoCoordinate position) {
        this.position=position;
        navigationManager = NavigationManager.getInstance();
    }
    void findDistanceRange(){
        currentManeuver = navigationManager.getNextManeuver();
        distanceNext = navigationManager.getNextManeuverDistance();

    }

}
