package ducere.lechal.pod;

import android.Manifest;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ducere.lechal.pod.ble.ActionsToService;
import ducere.lechal.pod.ble.PodsConnectivityService;
import ducere.lechal.pod.ble.ServiceBroadcastActions;
import ducere.lechal.pod.constants.ActivityRequestCodes;
import ducere.lechal.pod.constants.BundleKeys;
import ducere.lechal.pod.constants.Constants;
import ducere.lechal.pod.constants.LocationConstants;
import ducere.lechal.pod.constants.SharedPrefUtil;
import ducere.lechal.pod.customViews.CircleProgressView;

public class PodsConnectionActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private View coordinate;
    private CircleProgressView progress;
    private Handler handler = new Handler();

    private Map<String, BluetoothDevice> foundDevices = new HashMap<>();
    private List<CharSequence> displayDeviceNames = new ArrayList<>();

    private AlertDialog devicesListAlert;
    private TextView connectivityStatus;

    private GoogleApiClient googleApiClient;

    boolean isGoogleApiClientConnected = false;
    boolean isPermissionsGranted = false;

    private BluetoothAdapter bluetoothAdapter;

    private String podsMacID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ble_connection);

        podsMacID = SharedPrefUtil.getPodsMacid(PodsConnectionActivity.this);

        BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        bluetoothAdapter = bluetoothManager.getAdapter();

        coordinate = findViewById(R.id.coordinate);
        progress = (CircleProgressView) findViewById(R.id.progress);
        progress.setProgress(10);
        progress.setVisibility(View.INVISIBLE);
        connectivityStatus = (TextView) findViewById(R.id.connectivity_status);

        // Create an instance of GoogleAPIClient.
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

    }

    private void checkBluetoothPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_ADMIN) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.BLUETOOTH_ADMIN)) {
                Snackbar.make(coordinate, getString(R.string.permission_reason_bluetooth), Snackbar.LENGTH_LONG).show();
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.BLUETOOTH_ADMIN},
                        ActivityRequestCodes.BLUETOOTH_PERMISSION_REQUEST_CODE);
            }
        } else {
            // have bluetooth and location permissions.
            isPermissionsGranted = true;
            startServiceAndScanPods();
        }
    }

    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                View.OnClickListener onClickListener = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        intent.setData(Uri.parse("package:" + getPackageName()));
                        startActivity(intent);
                    }
                };
                Snackbar.make(coordinate, getString(R.string.permission_reason_location), Snackbar.LENGTH_INDEFINITE).setAction("Settings", onClickListener).show();
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        ActivityRequestCodes.LOCATION_PERMISSION_REQUEST_CODE);
            }
        } else {
            // have bluetooth permission. Check for location permission.
            checkBluetoothPermission();
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
                    isPermissionsGranted = true;
                    startServiceAndScanPods();
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
                    checkBluetoothPermission();
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
        googleApiClient.connect();

        IntentFilter intentFilter = new IntentFilter(ServiceBroadcastActions.BLE_DEVICE_FOUND);
        intentFilter.addAction(ServiceBroadcastActions.BLE_SCAN_STARTED);
        intentFilter.addAction(ServiceBroadcastActions.BLE_SCAN_STOPPED);
        intentFilter.addAction(ServiceBroadcastActions.PODS_CONNECTED);
        intentFilter.addAction(ServiceBroadcastActions.PODS_DIS_CONNECTED);
        intentFilter.addAction(ServiceBroadcastActions.LOCATION_UPDATED);
        LocalBroadcastManager.getInstance(this).registerReceiver(serviceActionsReceiver, intentFilter);

        IntentFilter btIntentFilter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
        registerReceiver(bluetoothStateChangeReceiver, btIntentFilter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkLocationPermission();
    }

    @Override
    protected void onStop() {
        super.onStop();
        googleApiClient.disconnect();
        isGoogleApiClientConnected = false;

        LocalBroadcastManager.getInstance(this).unregisterReceiver(serviceActionsReceiver);

        unregisterReceiver(bluetoothStateChangeReceiver);

        if (devicesListAlert != null) {
            devicesListAlert.dismiss();
        }
    }

    private void startServiceAndScanPods() {
        if (isPermissionsGranted && isGoogleApiClientConnected) {
            requestLocationUpdates();

            // If pods not connected at least once. Check bluetooth enabled
            if (TextUtils.isEmpty(podsMacID)) {
                // not connected at least once
                if (bluetoothAdapter.isEnabled()) {
                    // bluetooth turned on
                    startAnimation();
                } else {
                    // bluetooth not turned on
                    Snackbar snackbar = Snackbar.make(coordinate, "Bluetooth is turned OFF", Snackbar.LENGTH_INDEFINITE);
                    snackbar.setAction("Turn ON", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            bluetoothAdapter.enable();
                        }
                    });
                    snackbar.show();
                }
            } else {
                // connected at least once
                startAnimation();
            }

        }
    }

    private void startAnimation() {
        startService(new Intent(this, PodsConnectivityService.class));
        AlphaAnimation alphaAnimation = Constants.getAlphaAnimation();
        alphaAnimation.setAnimationListener(fadeOutAnimationListener);
        findViewById(R.id.splash_walk).startAnimation(alphaAnimation);
    }

    private Animation.AnimationListener fadeOutAnimationListener = new Animation.AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {
            sendScanPodsAction();
            if (!TextUtils.isEmpty(podsMacID)) {
                // Pods already connected, Let the auto connect do the work
                findViewById(R.id.splash_walk).setVisibility(View.GONE);
                startActivity(new Intent(PodsConnectionActivity.this, MainActivity.class));
                finish();
            } else {
                // TODO remove red image
                findViewById(R.id.splash_walk).setVisibility(View.GONE);
                findViewById(R.id.splash_red).setVisibility(View.GONE);
            }
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    };

    private void sendScanPodsAction() {
        foundDevices.clear();
        displayDeviceNames.clear();
        LocalBroadcastManager.getInstance(PodsConnectionActivity.this).sendBroadcast(new Intent(ActionsToService.SCAN_PODS));
    }

    private BroadcastReceiver serviceActionsReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case ServiceBroadcastActions.BLE_SCAN_STARTED:
                    Log.i(PodsConnectionActivity.class.getName(), "Scan Started");
                    progress.startAnimation(Constants.getRotateAnimation());
                    progress.setVisibility(View.VISIBLE);
                    connectivityStatus.setText(R.string.scanning_pods);
                    break;
                case ServiceBroadcastActions.BLE_SCAN_STOPPED:
                    progress.clearAnimation();
                    progress.setVisibility(View.INVISIBLE);
                    connectivityStatus.setText(R.string.scan_stopped);
                    if (TextUtils.isEmpty(podsMacID)) {
                        showListOfDevices();
                    }
                    break;
                case ServiceBroadcastActions.BLE_DEVICE_FOUND:
                    BluetoothDevice device = intent.getParcelableExtra(BundleKeys.BLE_DEVICE);
                    String deviceName = device.getName();

                    if (TextUtils.isEmpty(deviceName)) {
                        return;
                    }
                    deviceName = deviceName.trim();
                    if (!deviceName.endsWith("MS")) {
                        return;
                    }
                    String toBeAdded = deviceName + "\n" + device.getAddress();

                    if (!displayDeviceNames.contains(toBeAdded)) {
                        displayDeviceNames.add(toBeAdded);
                    }
                    foundDevices.put(device.getAddress(), device);
//                    Snackbar.make(coordinate, "Device found" + device.getAddress(), Snackbar.LENGTH_LONG).show();

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
                    Snackbar.make(coordinate, "Connected to Pods", Snackbar.LENGTH_LONG).show();
                    BluetoothDevice bluetoothDevice = intent.getParcelableExtra(BundleKeys.BLE_DEVICE);
                    SharedPrefUtil.setPodsMacid(PodsConnectionActivity.this, bluetoothDevice.getAddress());
                    finish();
                    startActivity(new Intent(PodsConnectionActivity.this, MainActivity.class));
                    break;
                case ServiceBroadcastActions.PODS_DIS_CONNECTED:
                    Snackbar.make(coordinate, "Pods disconnected", Snackbar.LENGTH_LONG).show();
                    break;
                case ServiceBroadcastActions.LOCATION_UPDATED:

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
                        connectivityStatus.setText(R.string.connecting_to_pods);
                        progress.setVisibility(View.VISIBLE);
                        progress.startAnimation(Constants.getRotateAnimation());
                    }
                }).setPositiveButton("Scan Again", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                LocalBroadcastManager.getInstance(PodsConnectionActivity.this).sendBroadcast(new Intent(ActionsToService.SCAN_PODS));
            }
        }).setNegativeButton(getString(android.R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        });

        devicesListAlert = builder.create();
        devicesListAlert.show();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        isGoogleApiClientConnected = true;
        startServiceAndScanPods();
    }

    private void requestLocationUpdates() {
        Intent intervalIntent = new Intent(this, LocationReceiver.class);
        PendingIntent intervalPendingIntent = PendingIntent.getBroadcast(this, 0, intervalIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, getLocationRequest(), intervalPendingIntent);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private LocationRequest getLocationRequest() {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        locationRequest.setInterval(LocationConstants.LOCATION_UPDATE_INTERVAL);
        locationRequest.setFastestInterval(LocationConstants.LOCATION_UPDATE_FASTEST_INTERVAL);
        return locationRequest;
    }

    private final BroadcastReceiver bluetoothStateChangeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE,
                        BluetoothAdapter.ERROR);
                switch (state) {
                    case BluetoothAdapter.STATE_OFF:
                        Log.i(PodsConnectionActivity.class.getName(), "Bluetooth turned OFF");
                        break;
                    case BluetoothAdapter.STATE_TURNING_OFF:
                        break;
                    case BluetoothAdapter.STATE_ON:
                        Log.i(PodsConnectionActivity.class.getName(), "Bluetooth turned ON");
                        startServiceAndScanPods();
                        break;
                    case BluetoothAdapter.STATE_TURNING_ON:
                        break;
                }
            }
        }
    };
}
