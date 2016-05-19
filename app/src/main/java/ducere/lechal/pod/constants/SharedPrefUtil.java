package ducere.lechal.pod.constants;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by sunde on 22-04-2016.
 */
public class SharedPrefUtil {
    private static final String PODS_MACID = "podsMacId";
    public static final String IS_MOCK_ENABLE = "isMockEnable";
    private static final String IS_SWAPPED = "isSwapped";
    private static final String FOOTWEAR_TYPE = "footwearType";
    private static final String INTENSITY_TYPE = "intensityType";
    public static final String CURRENT_LOCATION = "currentLocation";
    public static final String CURRENT_VICINITY = "currentVicinity";
    public static final String CURRENT_LAT = "currentLat";
    public static final String CURRENT_LNG = "currentLng";
    public static final String MOCK_LOCATION = "mockLocation";
    public static final String MOCK_VICINITY = "mockVicinity";
    public static final String MOCK_LAT = "mockLat";
    public static final String MOCK_LNG = "mockLng";
    public static final String VOICE_PREFERENCE = "voicePreference";

    static SharedPreferences getPref(Context context) {
        return context.getSharedPreferences("lechalPref", 0);
    }

    public static String getPodsMacid(Context context) {
        return getPref(context).getString(PODS_MACID, null);
    }

    public static void setPodsMacid(Context context, String podsMacid) {
        getPref(context).edit().putString(PODS_MACID, podsMacid).apply();
    }

    public static void commitString(Context context, String key, String value) {
        getPref(context).edit().putString(key, value).apply();
    }

    public static String getString(Context context, String key) {
        return getPref(context).getString(key, null);
    }
    public static void commitInt(Context context, String key, int value) {
        getPref(context).edit().putInt(key, value).apply();
    }

    public static int getInt(Context context, String key) {
        return getPref(context).getInt(key, 0);
    }
    public static int getVoiceId(Context context, String key) {
        return getPref(context).getInt(key, 1003);
    }

    public static void commitBoolean(Context context, String key, boolean value) {
        getPref(context).edit().putBoolean(key, value).apply();
    }

    public static boolean getBoolean(Context context, String key) {
        return getPref(context).getBoolean(key, false);
    }

    public static void commitDouble(Context context, String key, float value) {
        getPref(context).edit().putFloat(key, value).apply();
    }

    public static float getDouble(Context context, String key) {
        return getPref(context).getFloat(key, 0);
    }

    public static boolean isPodSwapped(Context context) {
        return getPref(context).getBoolean(IS_SWAPPED, false);
    }

    public static void setPodSwapped(Context context, boolean isSwapped) {
        getPref(context).edit().putBoolean(IS_SWAPPED, isSwapped).apply();
    }

    public static int getFootwearType(Context context) {
        return getPref(context).getInt(FOOTWEAR_TYPE, -1);
    }

    public static void setFootwearType(Context context, int index) {
        getPref(context).edit().putInt(FOOTWEAR_TYPE, index).apply();
    }

    public static int getIntensityType(Context context) {
        return getPref(context).getInt(INTENSITY_TYPE, 0);
    }

    public static void setIntensityType(Context context, int index) {
        getPref(context).edit().putInt(INTENSITY_TYPE, index).apply();
    }
}
