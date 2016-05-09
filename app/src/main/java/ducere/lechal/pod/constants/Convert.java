package ducere.lechal.pod.constants;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * Created by Siva on 09-05-2016.
 */
public class Convert {

    static DecimalFormat decimalFormat;

    public static DecimalFormat getFormatter(){
        decimalFormat = (DecimalFormat) NumberFormat.getInstance(new Locale("en", "UK"));
        decimalFormat.applyPattern("##.##");
        return decimalFormat;
    }

    public static String metersToKms(double meters){
        if(meters < 1000) {
            return (int)Math.round(meters) + " m";
        }
        else {
            return getFormatter().format((float) meters / 1000) + " km";
        }
    }

    public static String metersToYdsandMiles(double meters){
        int  yards = (int)(meters*1.093);
        if(yards < 800) {
            return yards + " yd";
        }
        else{
            return getFormatter().format(yards * 0.000568182) + " mi";
        }
    }

    public static String metersToFtandMiles(double meters){
        int  Feet = (int)(meters*3.28084);
        if(Feet < 900) {
            return Feet + " ft";
        }
        else{
            return getFormatter().format(Feet*0.000189394) + " mi";
        }
    }
}
