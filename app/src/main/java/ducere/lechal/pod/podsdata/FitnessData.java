package ducere.lechal.pod.podsdata;

import java.io.Serializable;
import java.text.DecimalFormat;

import ducere.lechal.pod.ble.PodsConnectivityService;

/**
 * Created by sunde on 06-04-2016.
 */
public class FitnessData implements Serializable {
    long day;
    long steps;
    float distance;
    long time;
    long cal;

    public FitnessData() {
    }

    public FitnessData(byte[] data) {
        if (data != null) {
            if(data[18]>15){
                steps = Math.round(PodsConnectivityService.byteToInt(new byte[]{data[2], data[3]}));
                DecimalFormat decimalFormat = new DecimalFormat("#.##");
                distance = PodsConnectivityService.byteToFloat(new byte[]{data[6], data[7], data[8], data[9]});
                time = Math.round(PodsConnectivityService.byteToFloat(new byte[]{data[10], data[11], data[12], data[13]}) / 60.0);
                cal = Math.round(PodsConnectivityService.byteToFloat(new byte[]{data[14], data[15], data[16], data[17]}));
            }else {
                steps = Math.round(PodsConnectivityService.byteToFloat(new byte[]{data[2], data[3], data[4], data[5]}));
                DecimalFormat decimalFormat = new DecimalFormat("#.##");
                distance = PodsConnectivityService.byteToFloat(new byte[]{data[6], data[7], data[8], data[9]}) ;
                time = Math.round(PodsConnectivityService.byteToFloat(new byte[]{data[10], data[11], data[12], data[13]}) / 60.0);
                cal = Math.round(PodsConnectivityService.byteToFloat(new byte[]{data[14], data[15], data[16], data[17]}));
            }

        }
    }

    public long getDay() {
        return day;
    }

    public long getSteps() {
        return steps;
    }

    public float getDistance() {
        return distance;
    }

    public long getTime() {
        return time;
    }

    public long getCal() {
        return cal;
    }

    public void setDay(long day) {
        this.day = day;
    }
}
