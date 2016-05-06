package ducere.lechal.pod.ble;

/**
 * Created by sunde on 06-04-2016.
 */
public interface ActionsToService {
    String pkg = "ducere.lechal.pod.ble";
    String VIBRATE = pkg + "Vibrate";
    String FITNESS_NOTIFICATION = pkg + "fitnessNotification";
    String FITNESS_START = pkg + "fitnessStart";
    String FITNESS_TODAY_DATA = pkg + "fitnessToday";
    String FITNESS_YESTERDAY_DATA = pkg + "fitnessYesterday";
    String FITNESS_DAY_B4_YESTERDAY_DATA = pkg + "fitnessDayB4Yesterday";
    String RBT = pkg + "RBT";
    String SCAN_PODS = pkg + "scanPods";
    String SCAN_STOP = pkg + "scanStop";
    String CONNECT_TO_DEVICE = pkg + "connectToDevice";
    String INTENSITY = pkg + "intensity";
    String FOOTWEAR_TYPE = pkg + "footwearType";
    String GET_BATTERY = pkg + "getBattery";
    String VIBRATE_LEFT = pkg + "vibrateLeft";
    String VIBRATE_RIGHT = pkg + "vibrateRight";

    /**
     * Dont forget to add actions in the array
     */
    String[] actions = {VIBRATE, FITNESS_NOTIFICATION, FITNESS_START, FITNESS_TODAY_DATA, FITNESS_YESTERDAY_DATA, FITNESS_DAY_B4_YESTERDAY_DATA, RBT,
            SCAN_PODS, SCAN_STOP, CONNECT_TO_DEVICE, INTENSITY, FOOTWEAR_TYPE, GET_BATTERY, VIBRATE_LEFT, VIBRATE_RIGHT};

}
