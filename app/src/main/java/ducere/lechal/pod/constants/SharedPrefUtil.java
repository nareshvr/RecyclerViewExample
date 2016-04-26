package ducere.lechal.pod.constants;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by sunde on 22-04-2016.
 */
public class SharedPrefUtil {
    private static final String PODS_MACID = "podsMacId";
    public static final String IS_MOCK_ENABLE = "isMockEnable";

    static SharedPreferences getPref(Context context) {
        return context.getSharedPreferences("lechalPref", 0);
    }

    public static String getPodsMacid(Context context) {
        return getPref(context).getString(PODS_MACID, null);
    }

    public static void setPodsMacid(Context context, String podsMacid) {
        getPref(context).edit().putString(PODS_MACID, podsMacid).apply();
    }
    public static void commitString(Context context,String key, String value){
        getPref(context).edit().putString(key, value).apply();
    }
    public static String getString(Context context,String key){
        return getPref(context).getString(key, null);
    }
    public static void commitBoolean(Context context,String key, boolean value){
        getPref(context).edit().putBoolean(key, value).apply();
    }
    public static boolean getBoolean(Context context,String key){
        return getPref(context).getBoolean(key, false);
    }


}
