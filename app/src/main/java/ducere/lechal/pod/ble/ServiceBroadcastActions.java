package ducere.lechal.pod.ble;

/**
 * Created by sunde on 06-04-2016.
 */
public interface ServiceBroadcastActions {
    String pkg = "ducere.lechal.pod.ble.sa";
    String FITNESS_DATA = pkg + "fitnessData";
    String FITNESS_TODAY_DATA = pkg + "fitnessToday";
    String FITNESS_YESTERDAY_DATA = pkg + "fitnessYesterday";
    String FITNESS_DAY_B4_YESTERDAY_DATA = pkg + "fitnessDayB4Yesterday";
    String BLE_SCAN_STARTED = pkg + "scanStarted";
    String BLE_SCAN_STOPPED = pkg + "scanStopped";
    String BLE_DEVICE_FOUND = pkg + "deviceFound";
    String PODS_CONNECTED = pkg + "podsConnected";
    String PODS_DIS_CONNECTED = pkg + "podsDisConnected";
    String LOCATION_UPDATED = pkg + "locationUpdated";
    String BATTERY = pkg + "battery";
}
