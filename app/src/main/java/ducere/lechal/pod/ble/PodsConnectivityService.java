package ducere.lechal.pod.ble;

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanRecord;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import ducere.lechal.pod.R;
import ducere.lechal.pod.constants.BundleKeys;
import ducere.lechal.pod.constants.Constants;
import ducere.lechal.pod.constants.SharedPrefUtil;
import ducere.lechal.pod.podsdata.FitnessData;

/**
 * Service runs in background. Auto connects to pods if reachable.
 */
public class PodsConnectivityService extends Service implements PodCommands {

    // Ping for battery information every 25 secs
    private static final long BATTERY_PING_FREQUENCY = 25000;

    // Scan only for ten seconds
    private static final long SCAN_PERIOD = 10000;

    private BluetoothAdapter bluetoothAdapter;
    private BluetoothLeScanner bluetoothLeScanner;
    private BluetoothGatt bluetoothGatt;

    private BluetoothGattService fitnessService;
    private BluetoothGattService miscellaneousService;

    private List<BluetoothGattService> bluetoothGattServices;

    private Handler handler;

    private String msBattery = "";
    private String qtrBattery = "";

    /**
     * this method will not be called if service is already running
     */
    @Override
    public void onCreate() {
        super.onCreate();

        handler = new Handler();
        registerReceiverForCommands();
    }

    private void startScanOnThread() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
                    final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
                    bluetoothAdapter = bluetoothManager.getAdapter();
                    registerForBluetoothStateChange();
                    if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled()) {
                        // bluetooth not enabled. stop the service
                        stopSelf();
                    } else {
                        startBleScan();
                    }

                } else {
                    // bluetooth not available stop the service
                    stopSelf();
                }
            }
        }.start();
    }

    private void registerReceiverForCommands() {
        IntentFilter filter = new IntentFilter(ActionsToService.VIBRATE);
        filter.addAction(ActionsToService.FITNESS_NOTIFICATION);
        filter.addAction(ActionsToService.FITNESS_START);
        filter.addAction(ActionsToService.RBT);
        filter.addAction(ActionsToService.SCAN_PODS);
        filter.addAction(ActionsToService.SCAN_STOP);
        filter.addAction(ActionsToService.CONNECT_TO_DEVICE);
        filter.addAction(ActionsToService.INTENSITY);
        filter.addAction(ActionsToService.FOOTWEAR_TYPE);
        filter.addAction(ActionsToService.GET_BATTERY);
        filter.addAction(ActionsToService.VIBRATE_LEFT);
        filter.addAction(ActionsToService.VIBRATE_RIGHT);
        // Add more commands
        LocalBroadcastManager.getInstance(PodsConnectivityService.this).registerReceiver(broadcastReceiverForCommands, filter);
    }

    private BroadcastReceiver broadcastReceiverForCommands = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case ActionsToService.VIBRATE:
                    String pattern = intent.getStringExtra(ActionsToService.VIBRATE);
                    vibrate(pattern);
                    break;
                case ActionsToService.FITNESS_NOTIFICATION:
                    boolean enable = intent.getBooleanExtra(ActionsToService.FITNESS_NOTIFICATION, false);
                    enableFitnessNotification(enable);
                    break;
                case ActionsToService.FITNESS_START:
                    startSendingFitnessData();
                    break;
                case ActionsToService.RBT:
                    sendRBT();
                    break;
                case ActionsToService.SCAN_PODS:
                    startScanOnThread();
                    break;
                case ActionsToService.SCAN_STOP:
                    stopBleScan();
                    break;
                case ActionsToService.CONNECT_TO_DEVICE:
                    BluetoothDevice device = intent.getParcelableExtra(BundleKeys.BLE_DEVICE);
                    Log.i(PodsConnectivityService.class.getName(), "Connect to " + device.getAddress());
                    bluetoothGatt = device.connectGatt(PodsConnectivityService.this, true, gattCallback);
                    break;
                case ActionsToService.INTENSITY:
                    pattern = intent.getStringExtra(ActionsToService.INTENSITY);
                    setIntensity(pattern);
                    break;
                case ActionsToService.FOOTWEAR_TYPE:
                    pattern = intent.getStringExtra(ActionsToService.FOOTWEAR_TYPE);
                    setFootwearType(pattern);
                    break;
                case ActionsToService.GET_BATTERY:
                    intent = new Intent(ServiceBroadcastActions.BATTERY);
                    intent.putExtra(ServiceBroadcastActions.BATTERY, getRemainingBattery());
                    LocalBroadcastManager.getInstance(PodsConnectivityService.this).sendBroadcast(intent);
                    break;
                case ActionsToService.VIBRATE_LEFT:
                    vibrate("VB0100"); // TODO move strings to vibrations patterns
                    break;
                case ActionsToService.VIBRATE_RIGHT:
                    vibrate("VB0001"); // TODO move strings to vibrations patterns

                    break;
            }
        }
    };

    private void registerForBluetoothStateChange() {
        IntentFilter intentFilter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
        registerReceiver(bluetoothStateChangeReceiver, intentFilter);
    }

    private void unregisterForBluetoothStateChange() {
        unregisterReceiver(bluetoothStateChangeReceiver);
    }

    private void unregisterReceiverForCommands() {
        LocalBroadcastManager.getInstance(PodsConnectivityService.this).unregisterReceiver(broadcastReceiverForCommands);
    }

    @SuppressLint("NewApi")
    private ScanCallback scanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            super.onScanResult(callbackType, result);
            BluetoothDevice device = result.getDevice();
            int rssi = result.getRssi();
            ScanRecord scanRecord = result.getScanRecord();

            deviceFound(device, rssi, scanRecord.getBytes());
        }

        @Override
        public void onBatchScanResults(List<ScanResult> results) {
            super.onBatchScanResults(results);
        }

        @Override
        public void onScanFailed(int errorCode) {
            super.onScanFailed(errorCode);
        }
    };

    private BluetoothAdapter.LeScanCallback leScanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
            deviceFound(device, rssi, scanRecord);
        }
    };

    private void deviceFound(BluetoothDevice device, int rssi, byte[] scanRecord) {
        Log.i(PodsConnectivityService.class.getName(), "Device Found: " + device.getAddress() + ": " + rssi + ":" + device.getName());

        Intent intent = new Intent(ServiceBroadcastActions.BLE_DEVICE_FOUND);
        intent.putExtra(BundleKeys.BLE_DEVICE, device);
        intent.putExtra(BundleKeys.BLE_RSSI, rssi);

        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);

        if (device.getAddress().equalsIgnoreCase(SharedPrefUtil.getPodsMacid(this))) {
            // found device previously connected
            Intent connectDeviceIntent = new Intent(ActionsToService.CONNECT_TO_DEVICE);
            connectDeviceIntent.putExtra(BundleKeys.BLE_DEVICE, device);
            LocalBroadcastManager.getInstance(this).sendBroadcast(connectDeviceIntent);

            // stop scan
            LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(ActionsToService.SCAN_STOP));
        }
    }

    private BluetoothGattCallback gattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                Intent intent = new Intent(ServiceBroadcastActions.PODS_CONNECTED);
                intent.putExtra(BundleKeys.BLE_DEVICE, gatt.getDevice());
                LocalBroadcastManager.getInstance(PodsConnectivityService.this).sendBroadcast(intent);
                Log.i(PodsConnectivityService.class.getName(), "Connected to GATT server.");
                bluetoothGatt = gatt;
                bluetoothGatt.discoverServices();
            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                // TODO broadcast gatt disconnected state to the app
                Log.i(PodsConnectivityService.class.getName(), "Disconnected from GATT server.");
                NotificationManager mNotificationManager =
                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                mNotificationManager.cancel(Constants.NOTIFICATION_ID);
            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            super.onServicesDiscovered(gatt, status);
            bluetoothGattServices = bluetoothGatt.getServices();

            readServices();


            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    enableMSNotification(true);
                }
            }, 2000);

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    enableQTRNotification(true);
                }
            }, 4000);

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    sendRBT();
                }
            }, 6000);

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    enableFitnessNotification(true);
                }
            }, 8000);

            /*handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    startSendingFitnessData();
                }
            }, 10000);*/

            showConnectedPodsNotification(78, getRemainingBattery());
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicRead(gatt, characteristic, status);
            Log.i(PodsConnectivityService.class.getName(), "Characteristic read: " + status);
            handleCharacteristic(characteristic);
        }

        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicWrite(gatt, characteristic, status);
            Log.i(PodsConnectivityService.class.getName(), "Characteristic write status : " + status);
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            super.onCharacteristicChanged(gatt, characteristic);
//            Log.i(PodsConnectivityService.class.getName(), "Characteristic changed");
            handleCharacteristic(characteristic);
        }
    };

    private void showConnectedPodsNotification(int goalCompletedPercent, int remainingBatteryPercent) {
        String info = "";
        if (goalCompletedPercent > 0) {
            info = "Goal " + goalCompletedPercent + "%";
        }
        if (remainingBatteryPercent != -1) {
            info = info.concat("Battery " + remainingBatteryPercent + "%");
        }
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_menu_camera)
                        .setContentTitle("Lechal connected")
                        .setContentText(info)
                        .setOngoing(true);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(Constants.NOTIFICATION_ID, mBuilder.build());
    }

    int getRemainingBattery() {
        if (TextUtils.isEmpty(msBattery) || TextUtils.isEmpty(qtrBattery)) {
            return -1;
        }
        int msRemaining = Integer.parseInt(msBattery.substring(2, 5));
        int qtrRemaining = Integer.parseInt(qtrBattery.substring(2, 5));
        if (msRemaining > qtrRemaining) {
            return qtrRemaining;
        }
        return msRemaining;
    }

    private void handleCharacteristic(BluetoothGattCharacteristic characteristic) {
        byte[] data;
        switch (characteristic.getUuid().toString()) {
            case PodsServiceCharacteristics.SERVICE_C_CHARACTERISTIC_FITNESS:
                Log.i(PodsConnectivityService.class.getName(), "Found Fitness characteristic: " + characteristic.getUuid());
                data = characteristic.getValue();
                if (data[0] == (byte) 0xFE && data[1] == (byte) 0x0B) {
                    FitnessData fitnessData = new FitnessData(data);
                    Intent intent = new Intent(ServiceBroadcastActions.FITNESS_DATA);
                    intent.putExtra(ServiceBroadcastActions.FITNESS_DATA, fitnessData);
                    LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
                }
                break;
            case PodsServiceCharacteristics.SERVICE_MISC_CHARACTERISTIC_MS_NOTIFY:
//                Log.i(PodsConnectivityService.class.getName(), "Found MS notify: " + characteristic.getUuid());
                data = characteristic.getValue();
                String msMsg = new String(data);
                if (!TextUtils.isEmpty(msMsg)) {

                    Log.i("MS Notify", msMsg);
                    if (msMsg.startsWith("BT")) {
                        msBattery = msMsg;

                        int remainingBattery = getRemainingBattery();
                        showConnectedPodsNotification(78, remainingBattery);
                    }
                }
                // Ask for battery status after given time
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        sendRBT();
                    }
                }, BATTERY_PING_FREQUENCY);
                break;
            case PodsServiceCharacteristics.SERVICE_MISC_CHARACTERISTIC_QTR_NOTIFY:
//                Log.i(PodsConnectivityService.class.getName(), "Found QTR Notify: " + characteristic.getUuid());
                data = characteristic.getValue();
                String qtrMsg = new String(data);
                if (!TextUtils.isEmpty(qtrMsg)) {

                    Log.i("QTR Notify", qtrMsg);
                    if (qtrMsg.startsWith("BT")) {
                        qtrBattery = qtrMsg;
                    }
                }
                break;
            default:
                Log.w(PodsConnectivityService.class.getName(), "Found unknown characteristic: " + characteristic.getUuid());
                break;
        }
    }

    @Override
    public void enableFitnessNotification(boolean isEnabled) {
        BluetoothGattCharacteristic fitnessNotificationCharacteristic = fitnessService.getCharacteristic(UUID.fromString(PodsServiceCharacteristics.SERVICE_C_CHARACTERISTIC_FITNESS));
        if (fitnessNotificationCharacteristic != null) {
            bluetoothGatt.setCharacteristicNotification(fitnessNotificationCharacteristic, isEnabled);

            enableDescriptor(fitnessNotificationCharacteristic);
        }
    }

    private void enableDescriptor(BluetoothGattCharacteristic bluetoothGattCharacteristic) {
        BluetoothGattDescriptor descriptor = bluetoothGattCharacteristic.getDescriptor(
                UUID.fromString(PodsServiceCharacteristics.CLIENT_CHARACTERISTIC_CONFIG));
        if (descriptor == null)
            return;
        descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
        bluetoothGatt.writeDescriptor(descriptor);
    }

    @Override
    public void startSendingFitnessData() {
        BluetoothGattCharacteristic fitnessCharacteristic = fitnessService.getCharacteristic(UUID.fromString(PodsServiceCharacteristics.SERVICE_C_CHARACTERISTIC_FITNESS));
        if (fitnessCharacteristic != null) {
            fitnessCharacteristic.setValue(START_FITNESS_COMMAND);
            bluetoothGatt.writeCharacteristic(fitnessCharacteristic);
        }
    }

    @Override
    public void stopSendingFitnessData() {

    }

    @Override
    public void vibrate(String pattern) {
        BluetoothGattCharacteristic vibCharacteristic = fitnessService.getCharacteristic(UUID.fromString(PodsServiceCharacteristics.SERVICE_C_CHARACTERISTIC_VIBRATE));
        if (vibCharacteristic != null) {
            vibCharacteristic.setValue(pattern);
            bluetoothGatt.writeCharacteristic(vibCharacteristic);
        }
    }

    @Override
    public void sendRBT() {
        BluetoothGattCharacteristic characteristic = miscellaneousService.getCharacteristic(UUID.fromString(PodsServiceCharacteristics.SERVICE_MISC_CHARACTERISTIC_WRITE));
        if (characteristic != null) {
            characteristic.setValue("RBT");
            bluetoothGatt.writeCharacteristic(characteristic);
        }
    }

    @Override
    public void enableMSNotification(boolean isEnabled) {
        BluetoothGattCharacteristic characteristic = miscellaneousService.getCharacteristic(UUID.fromString(PodsServiceCharacteristics.SERVICE_MISC_CHARACTERISTIC_MS_NOTIFY));
        bluetoothGatt.setCharacteristicNotification(characteristic, true);
        enableDescriptor(characteristic);
    }

    @Override
    public void enableQTRNotification(boolean isEnabled) {
        BluetoothGattCharacteristic characteristic = miscellaneousService.getCharacteristic(UUID.fromString(PodsServiceCharacteristics.SERVICE_MISC_CHARACTERISTIC_QTR_NOTIFY));
        bluetoothGatt.setCharacteristicNotification(characteristic, true);
        enableDescriptor(characteristic);
    }

    @Override
    public void setIntensity(String intensity) {
        BluetoothGattCharacteristic vibCharacteristic = fitnessService.getCharacteristic(UUID.fromString(PodsServiceCharacteristics.SERVICE_C_CHARACTERISTIC_VIBRATE));
        if (vibCharacteristic != null) {
            vibCharacteristic.setValue(intensity);
            bluetoothGatt.writeCharacteristic(vibCharacteristic);
        }
    }

    @Override
    public void setFootwearType(String footwearType) {
        BluetoothGattCharacteristic characteristic = miscellaneousService.getCharacteristic(UUID.fromString(PodsServiceCharacteristics.SERVICE_MISC_CHARACTERISTIC_WRITE));
        if (characteristic != null) {
            characteristic.setValue(footwearType);
            bluetoothGatt.writeCharacteristic(characteristic);
        }
    }

    private void readServices() {
        Log.i(PodsConnectivityService.class.getName(), "Discovered Services : " + bluetoothGattServices.size());
        for (BluetoothGattService bluetoothGattService : bluetoothGattServices) {
            Log.i(PodsConnectivityService.class.getName(), "Service uuid: " + bluetoothGattService.getUuid().toString());
            switch (bluetoothGattService.getUuid().toString()) {
                case PodsServiceCharacteristics.SERVICE_C:
                    fitnessService = bluetoothGattService;
                    break;
                case PodsServiceCharacteristics.SERVICE_MISC:
                    miscellaneousService = bluetoothGattService;
                    break;
            }
        }
    }

    private void startBleScan() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            ScanSettings.Builder scanSettingsBuilder = new ScanSettings.Builder();
            scanSettingsBuilder.setScanMode(ScanSettings.SCAN_MODE_LOW_POWER);
            ScanSettings scanSettings = scanSettingsBuilder.build();
            // TODO Apply scan filters to refine scan

            bluetoothLeScanner = bluetoothAdapter.getBluetoothLeScanner();
            bluetoothLeScanner.startScan(new ArrayList<ScanFilter>(), scanSettings, scanCallback);
        } else {
            bluetoothAdapter.startLeScan(leScanCallback);
        }

        LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(ServiceBroadcastActions.BLE_SCAN_STARTED));

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                stopBleScan();
            }
        }, SCAN_PERIOD);
    }

    private void stopBleScan() {
        // stop only when it's turned ON
        if (bluetoothAdapter != null && bluetoothAdapter.getState() == BluetoothAdapter.STATE_ON) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                bluetoothLeScanner.stopScan(scanCallback);
            } else {
                bluetoothAdapter.stopLeScan(leScanCallback);
            }
        }

        LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(ServiceBroadcastActions.BLE_SCAN_STOPPED));
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.w(PodsConnectivityService.class.getName(), "Service Stopped...");
        unregisterForBluetoothStateChange();
        unregisterReceiverForCommands();
        close();
    }

    public void close() {
        if (bluetoothGatt == null) {
            return;
        }

        bluetoothGatt.close();
        bluetoothGatt = null;
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
                        Log.i(PodsConnectivityService.class.getName(), "Bluetooth turned OFF");
                        break;
                    case BluetoothAdapter.STATE_TURNING_OFF:
//                        setButtonText("Turning Bluetooth off...");
                        break;
                    case BluetoothAdapter.STATE_ON:
                        Log.i(PodsConnectivityService.class.getName(), "Bluetooth turned ON");
                        LocalBroadcastManager.getInstance(PodsConnectivityService.this).sendBroadcast(new Intent(ActionsToService.SCAN_PODS));
                        break;
                    case BluetoothAdapter.STATE_TURNING_ON:
//                        setButtonText("Turning Bluetooth on...");
                        break;
                }
            }
        }
    };

    public static float byteToFloat(byte[] data) {
        return ByteBuffer.wrap(data).order(ByteOrder.LITTLE_ENDIAN).getFloat();
    }

}
