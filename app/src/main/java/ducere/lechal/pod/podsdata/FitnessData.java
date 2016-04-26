package ducere.lechal.pod.podsdata;

import java.io.Serializable;
import java.text.DecimalFormat;

import ducere.lechal.pod.ble.PodsConnectivityService;

/**
 * Created by sunde on 06-04-2016.
 */
public class FitnessData implements Serializable {
    long steps;
    String distance;
    long time;
    long cal;

    public FitnessData(byte[] data) {
        if (data != null) {
            steps = Math.round(PodsConnectivityService.byteToFloat(new byte[]{data[2], data[3], data[4], data[5]}));
            DecimalFormat decimalFormat = new DecimalFormat("#.##");
            distance = decimalFormat.format(PodsConnectivityService.byteToFloat(new byte[]{data[6], data[7], data[8], data[9]}) / 1000.0);
            distance = distance.replace(",", ".");
            time = Math.round(PodsConnectivityService.byteToFloat(new byte[]{data[10], data[11], data[12], data[13]}) / 60.0);
            cal = Math.round(PodsConnectivityService.byteToFloat(new byte[]{data[14], data[15], data[16], data[17]}));
        }
    }

    public long getSteps() {
        return steps;
    }

    public String getDistance() {
        return distance;
    }

    public long getTime() {
        return time;
    }

    public long getCal() {
        return cal;
    }
}