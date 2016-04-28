package ducere.lechal.pod.ble;

/**
 * Created by sunde on 06-04-2016.
 */
public interface ServiceBroadcastActions {
    String pkg = "ducere.lechal.pod.ble";
    String FITNESS_DATA = pkg + "fitnessData";
    String BLE_SCAN_STARTED = pkg + "scanStarted";
    String BLE_SCAN_STOPPED = pkg + "scanStopped";
    String BLE_DEVICE_FOUND = pkg + "deviceFound";
    String PODS_CONNECTED = pkg + "podsConnected";
    String PODS_DIS_CONNECTED = pkg + "podsDisConnected";
    String LOCATION_UPDATED = pkg + "locationUpdated";
    String BATTERY = pkg + "battery";
}
