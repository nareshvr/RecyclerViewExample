package ducere.lechal.pod.constants;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.google.gson.Gson;

import ducere.lechal.pod.server_models.User;

/**
 * Created by sunde on 22-04-2016.
 */
public class SharedPrefUtil {
    private static final String PODS_MACID = "podsMacId";
    public static final String IS_MOCK_ENABLE = "isMockEnable";
    private static final String IS_SWAPPED = "isSwapped";
    private static final String FOOTWEAR_TYPE = "footwearType";
    private static final String GENDER_TYPE = "gender";
    private static final String INTENSITY_TYPE = "intensityType";
    public static final String CURRENT_LOCATION = "currentLocation";
    public static final String CURRENT_VICINITY = "currentVicinity";
    public static final String CURRENT_LAT = "currentLat";
    public static final String CURRENT_LNG = "currentLng";
    public static final String MOCK_LOCATION = "mockLocation";
    public static final String MOCK_VICINITY = "mockVicinity";
    public static final String MOCK_LAT = "mockLat";
    public static final String MOCK_LNG = "mockLng";
    public static final String HEIGHT_UNITS = "heightUnits";
    public static final String WEIGHT_UNITS = "weightUnits";
    private static final String USER = "user";
    private static final String USER_ID = "userID";
    public static final String GENDER = "gender";
    public static final String MAP_UNITS = "mapUnits";
    public static final String DAY_NIGHT_TYPE = "dayNight";
public static final String ROUTE_PERFERENCE = "setRoutePerference";
    static SharedPreferences getPref(Context context) {
        return context.getSharedPreferences("lechalPref", 0);
    }

    public static User getUser(Context context) {
        String string = getPref(context).getString(USER, "");
        if (TextUtils.isEmpty(string)) {
            return null;
        } else {
            return new Gson().fromJson(string, User.class);
        }
    }

    public static void setUser(Context context, User user) {
        getPref(context).edit().putString(USER, new Gson().toJson(user)).apply();
    }

    public static String getUserId(Context context) {
        return getPref(context).getString(USER_ID, null);
    }

    public static void setUserId(Context context, String userId) {
        getPref(context).edit().putString(USER_ID, userId).apply();
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

    public static int getDayNight(Context context) {
        return getPref(context).getInt(DAY_NIGHT_TYPE, -1);
    }

    public static int getMapUnits(Context context) {
        return getPref(context).getInt(MAP_UNITS, -1);
    }

    public static int getGenderType(Context context) {
        return getPref(context).getInt(GENDER_TYPE, -1);
    }

    public static void setFootwearType(Context context, int index) {
        getPref(context).edit().putInt(FOOTWEAR_TYPE, index).apply();
    }

    public static void setDayNightType(Context context, int index) {
        getPref(context).edit().putInt(DAY_NIGHT_TYPE, index).apply();
    }

    public static void setMapUnits(Context context, int index) {
        getPref(context).edit().putInt(MAP_UNITS, index).apply();
    }

    public static void setGenderType(Context context, int index) {
        getPref(context).edit().putInt(GENDER_TYPE, index).apply();
    }

    public static int getIntensityType(Context context) {
        return getPref(context).getInt(INTENSITY_TYPE, 0);
    }

    public static void setIntensityType(Context context, int index) {
        getPref(context).edit().putInt(INTENSITY_TYPE, index).apply();
    }

    public static int getHeightUnits(Context context) {
        return getPref(context).getInt(HEIGHT_UNITS, Constants.DEFAULT_HEIGHT_UNITS);

    }

    public static int getWeightUnits(Context context) {
        return getPref(context).getInt(WEIGHT_UNITS, Constants.DEFAULT_WEIGHT_UNITS);

    }

    public static void setHeightUnits(Context context, int height) {
        getPref(context).edit().putInt(HEIGHT_UNITS, height).apply();
    }

    public static void setWeightUnits(Context context, int weight) {
        getPref(context).edit().putInt(WEIGHT_UNITS, weight).apply();
    }

    public static void setGender(Context context, String gender) {
        getPref(context).edit().putString(GENDER, gender).apply();
    }

    public static String getGender(Context context) {
        return getPref(context).getString(GENDER, Constants.DEFAULT_GENDER);
    }

    public static void setRoutePerference(Context context,int index){
        getPref(context).edit().putInt(ROUTE_PERFERENCE,index).apply();

    }

    public static int getRoutePerference(Context context){
        return getPref(context).getInt(ROUTE_PERFERENCE,-1);
    }
}
