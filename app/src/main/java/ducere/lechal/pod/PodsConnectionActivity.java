package ducere.lechal.pod;

import android.Manifest;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ducere.lechal.pod.ble.ActionsToService;
import ducere.lechal.pod.ble.PodsConnectivityService;
import ducere.lechal.pod.ble.ServiceBroadcastActions;
import ducere.lechal.pod.constants.ActivityRequestCodes;
import ducere.lechal.pod.constants.BundleKeys;

public class PodsConnectionActivity extends AppCompatActivity {

    private View rootView;
    private Handler handler = new Handler();
    private boolean isPermissionsGranted = false;

    private Map<String, BluetoothDevice> foundDevices = new HashMap<>();
    private List<CharSequence> displayDeviceNames = new ArrayList<>();

    private AlertDialog devicesListAlert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ble_connection);

        rootView = findViewById(R.id.root_view);

        checkBluetoothPermission();
    }

    private void checkBluetoothPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_ADMIN) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.BLUETOOTH_ADMIN)) {
                Snackbar.make(rootView, getString(R.string.permission_reason_bluetooth), Snackbar.LENGTH_LONG).show();
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.BLUETOOTH_ADMIN},
                        ActivityRequestCodes.BLUETOOTH_PERMISSION_REQUEST_CODE);
            }
        } else {
            // have bluetooth permission. Check for location permission.
            checkLocationPermission();
        }
    }

    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                Snackbar.make(rootView, getString(R.string.permission_reason_location), Snackbar.LENGTH_LONG).show();
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        ActivityRequestCodes.LOCATION_PERMISSION_REQUEST_CODE);
            }
        } else {
            // have bluetooth and location permissions.
            isPermissionsGranted = true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case ActivityRequestCodes.BLUETOOTH_PERMISSION_REQUEST_CODE:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    checkLocationPermission();
                } else {
                    finish();
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            case ActivityRequestCodes.LOCATION_PERMISSION_REQUEST_CODE:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    startService(new Intent(this, PodsConnectivityService.class));
                } else {
                    finish();
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter intentFilter = new IntentFilter(ServiceBroadcastActions.BLE_DEVICE_FOUND);
        intentFilter.addAction(ServiceBroadcastActions.BLE_SCAN_STARTED);
        intentFilter.addAction(ServiceBroadcastActions.BLE_SCAN_STOPPED);
        intentFilter.addAction(ServiceBroadcastActions.PODS_CONNECTED);
        intentFilter.addAction(ServiceBroadcastActions.PODS_DIS_CONNECTED);
        LocalBroadcastManager.getInstance(this).registerReceiver(serviceActionsReceiver, intentFilter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        startService(new Intent(this, PodsConnectivityService.class));

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                foundDevices.clear();
                displayDeviceNames.clear();
                LocalBroadcastManager.getInstance(PodsConnectionActivity.this).sendBroadcast(new Intent(ActionsToService.SCAN_PODS));
            }
        }, 2000);

    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(serviceActionsReceiver);
        if (devicesListAlert != null) {
            devicesListAlert.dismiss();
        }
    }

    BroadcastReceiver serviceActionsReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case ServiceBroadcastActions.BLE_SCAN_STARTED:
                    Snackbar.make(rootView, "Scan started", Snackbar.LENGTH_LONG).show();
                    break;
                case ServiceBroadcastActions.BLE_SCAN_STOPPED:
                    Snackbar.make(rootView, "Scan stopped", Snackbar.LENGTH_LONG).show();
                    showListOfDevices();
                    break;
                case ServiceBroadcastActions.BLE_DEVICE_FOUND:
                    BluetoothDevice device = intent.getParcelableExtra(BundleKeys.BLE_DEVICE);
                    String deviceName = device.getName().trim();
                    if (!TextUtils.isEmpty(deviceName)) {
                        deviceName = deviceName.trim();
                        if (!deviceName.endsWith("MS")) {
                            return;
                        }
                    }
                    String toBeAdded = deviceName + "\n" + device.getAddress();

                    if (!displayDeviceNames.contains(toBeAdded)) {
                        displayDeviceNames.add(toBeAdded);
                    }
                    foundDevices.put(device.getAddress(), device);
//                    Snackbar.make(rootView, "Device found" + device.getAddress(), Snackbar.LENGTH_LONG).show();

                    /*D0:93:80:00:10:76*/
                    /*if (device.getAddress().equalsIgnoreCase("D0:93:80:B0:03:50")) { // TODO Add custom filters needed
                        LocalBroadcastManager.getInstance(PodsConnectionActivity.this).sendBroadcast(new Intent(ActionsToService.SCAN_STOP));

                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent connectDeviceIntent = new Intent(ActionsToService.CONNECT_TO_DEVICE);
                                connectDeviceIntent.putExtra(BundleKeys.BLE_DEVICE, device);
                                LocalBroadcastManager.getInstance(PodsConnectionActivity.this).sendBroadcast(connectDeviceIntent);
                            }
                        }, 2000);

                    }
*/
                    break;
                case ServiceBroadcastActions.PODS_CONNECTED:
                    Snackbar.make(rootView, "Connected to Pods", Snackbar.LENGTH_LONG).show();
                    finish();
                    startActivity(new Intent(PodsConnectionActivity.this, MainActivity.class));
                    break;
                case ServiceBroadcastActions.PODS_DIS_CONNECTED:
                    Snackbar.make(rootView, "Pods disconnected", Snackbar.LENGTH_LONG).show();
                    break;
            }
        }
    };

    private void showListOfDevices() {
        CharSequence[] list = new CharSequence[displayDeviceNames.size()];
        displayDeviceNames.toArray(list);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.alert_title_pods_found))
                .setItems(list, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        devicesListAlert.dismiss();
                        CharSequence charSequence = displayDeviceNames.get(which);
                        String macID = charSequence.toString().split("\n")[1];
                        BluetoothDevice device = foundDevices.get(macID);
                        Intent connectDeviceIntent = new Intent(ActionsToService.CONNECT_TO_DEVICE);
                        connectDeviceIntent.putExtra(BundleKeys.BLE_DEVICE, device);
                        LocalBroadcastManager.getInstance(PodsConnectionActivity.this).sendBroadcast(connectDeviceIntent);
                    }
                });

        devicesListAlert = builder.create();
        devicesListAlert.show();
    }
}
