package ducere.lechal.pod.ble;

/**
 * Created by sunde on 05-04-2016.
 */
public interface PodCommands {

    byte[] START_FITNESS_COMMAND = new byte[]{(byte) 0xFE, (byte) 0x0B, (byte) 0x01};

    void enableFitnessNotification(boolean isEnabled);

    void startSendingFitnessData();

    void stopSendingFitnessData();

    void vibrate(String pattern);

    void sendRBT();

    void enableMSNotification(boolean isEnabled);

    void enableQTRNotification(boolean isEnabled);
}
