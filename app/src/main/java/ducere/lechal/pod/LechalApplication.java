package ducere.lechal.pod;

import android.app.Application;

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

    public boolean isNavigating() {
        return isNavigating;
    }

    public void setNavigating(boolean navigating) {
        isNavigating = navigating;
    }
}
