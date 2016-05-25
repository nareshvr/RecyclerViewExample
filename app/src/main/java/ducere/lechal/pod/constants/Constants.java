package ducere.lechal.pod.constants;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Base64;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;

import java.io.ByteArrayOutputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import ducere.lechal.pod.ble.ActionsToService;

/**
 * Created by sunde on 25-04-2016.
 */
public class Constants {
    /**
     * never change
     */
    public static final int NOTIFICATION_ID = 12706;
    public static final String NOTIFICATION_TEXT_FORMAT = "Goal %s : Battery %s";
    public static final String CURRENT_LOCATION_FORMAT = "%s\n%s";

    public static final CharSequence[] INTENSITIES = {" Very High ", " High ", " Medium ", " Low ", " Very Low "};
    public static final SimpleDateFormat DATE_FORMAT_FITNESS_ID_DATE = new SimpleDateFormat("yyyyMMdd");//20160509

    public static final int DEFAULT_HEIGHT_UNITS = 0;//"Feet";
    public static final int DEFAULT_WEIGHT_UNITS = 0;
    public static final String DOMAIN = "http://ec2-52-27-57-49.us-west-2.compute.amazonaws.com/";
    public static final String USER = "user";
    public static final String NAVIGATE = "navigation";

    public static String getEncoded64ImageStringFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byteFormat = stream.toByteArray();
        // get the base 64 string
        return Base64.encodeToString(byteFormat, Base64.NO_WRAP);
    }

    public static String formatDistance(double distance) {
        String text;
        if (distance > 999) {
            text = new DecimalFormat("#.#km").format((distance / 1000));
        } else {
            text = new DecimalFormat("#m").format(distance);
        }
        return text;
    }

    public static String formatTime(long time) {
        if (time >= 60) {
            return (int) (time / 60) + " mins";
        } else {
            return time + " secs";
        }
    }

    public static String getETAFormat(double distanceLeft, int timeLeft) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.SECOND, timeLeft);
        return String.format("%s | %s", formatDistance(distanceLeft), simpleDateFormat.format(calendar.getTime()));
    }

    public interface SearchResultType {
        int OPTIONS = 0;
        int SEARCH = 1;
        int TAG = 2;
        int FAVOURITES = 4;
        int HISTORY = 8;
        int POI = 16;
        int size = 5;// total types. Options not included
    }

    public static RotateAnimation getRotateAnimation() {
        RotateAnimation rotate = new RotateAnimation(0, 360,Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,0.5f);
        rotate.setDuration(2000);
        rotate.setRepeatCount(Animation.INFINITE);
        rotate.setInterpolator(new LinearInterpolator());
        return rotate;
    }

    public static AlphaAnimation getAlphaAnimation() {
        AlphaAnimation fadeOut = new AlphaAnimation(1, 0);
        fadeOut.setDuration(2000);
        fadeOut.setStartOffset(1000);
        fadeOut.setInterpolator(new AccelerateInterpolator());
        return fadeOut;
    }

    public static void sendVibrate(Context context, String type, String left, String right) {
        String pattern = type + (SharedPrefUtil.isPodSwapped(context) ? right + left : left + right);
        Intent intent = new Intent(ActionsToService.VIBRATE);
        intent.putExtra(ActionsToService.VIBRATE, pattern);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    public static void sendIntensity(Context context, String pattern) {
        Intent intent = new Intent(ActionsToService.INTENSITY);
        intent.putExtra(ActionsToService.INTENSITY, pattern);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    public static void sendFootwear(Context context, String pattern) {
        Intent intent = new Intent(ActionsToService.FOOTWEAR_TYPE);
        intent.putExtra(ActionsToService.FOOTWEAR_TYPE, pattern);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    public static void sendVibrationLeft(Context context) {
        Intent intent = new Intent(ActionsToService.VIBRATE_LEFT);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    public static void sendVibrationRight(Context context) {
        Intent intent = new Intent(ActionsToService.VIBRATE_RIGHT);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

}
