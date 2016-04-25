package ducere.lechal.pod;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;

import com.google.android.gms.location.LocationResult;

import ducere.lechal.pod.constants.BundleKeys;

public class LocationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        boolean hasResult = LocationResult.hasResult(intent);
        if (hasResult) {
            LocationResult locationResult = LocationResult.extractResult(intent);
            if (locationResult == null) {
                return;
            }
            Location location = locationResult.getLastLocation();
            Intent locationIntent = new Intent();
            intent.putExtra(BundleKeys.LOCATION, location);
            context.sendBroadcast(locationIntent);
        }
    }
}