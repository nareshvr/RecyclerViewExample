package ducere.lechal.pod.ble;

/**
 * Created by sunde on 05-04-2016.
 */
public class PodsServiceCharacteristics {

    public static String CLIENT_CHARACTERISTIC_CONFIG = "00002902-0000-1000-8000-00805f9b34fb";

    /* Total Services Available in pods: 4*/
    public static final String SERVICE_A = "00001801-0000-1000-8000-00805f9b34fb";
    public static final String SERVICE_A_CHARACTERISTIC_A = "00002a05-0000-1000-8000-00805f9b34fb";

    public static final String SERVICE_B = "00001800-0000-1000-8000-00805f9b34fb";
    public static final String SERVICE_B_CHARACTERISTIC_A = "00002a00-0000-1000-8000-00805f9b34fb";
    public static final String SERVICE_B_CHARACTERISTIC_B = "00002a01-0000-1000-8000-00805f9b34fb";
    public static final String SERVICE_B_CHARACTERISTIC_C = "00002a04-0000-1000-8000-00805f9b34fb";

    public static final String SERVICE_C = "ed0ef62e-9b0d-11e4-89d3-123b93f75c00";
    public static final String SERVICE_C_CHARACTERISTIC_VIBRATE = "ed0efb1a-9b0d-11e4-89d3-123b93f75c01";
    public static final String SERVICE_C_CHARACTERISTIC_FITNESS = "ed0efb1a-9b0d-11e4-89d3-123b93f75c02";

    public static final String SERVICE_MISC = "ed0ef62e-9b0d-11e4-89d3-123b93f75c03";
    public static final String SERVICE_MISC_CHARACTERISTIC_A = "ed0efb1a-9b0d-11e4-89d3-123b93f75c04";
    public static final String SERVICE_MISC_CHARACTERISTIC_WRITE = "ed0efb1a-9b0d-11e4-89d3-123b93f75c05";
    public static final String SERVICE_MISC_CHARACTERISTIC_MS_NOTIFY = "ed0efb1a-9b0d-11e4-89d3-123b93f75c06";
    public static final String SERVICE_MISC_CHARACTERISTIC_QTR_NOTIFY = "ed0efb1a-9b0d-11e4-89d3-123b93f75c07";

}
