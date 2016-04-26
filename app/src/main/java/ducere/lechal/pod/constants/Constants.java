package ducere.lechal.pod.constants;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;

import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;

import java.util.concurrent.TimeUnit;

import ducere.lechal.pod.ble.ActionsToService;

/**
 * Created by sunde on 25-04-2016.
 */
public class Constants {

    public static void sendVibrate(Context context, String pattern) {
        Intent intent = new Intent(ActionsToService.VIBRATE);
        intent.putExtra(ActionsToService.VIBRATE, pattern);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }
}
