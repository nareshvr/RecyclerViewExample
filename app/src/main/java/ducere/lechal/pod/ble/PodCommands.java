package ducere.lechal.pod.ble;

/**
 * Created by sunde on 05-04-2016.
 */
public interface PodCommands {

    byte[] START_FITNESS_COMMAND = new byte[]{(byte) 0xFE, (byte) 0x0B, (byte) 0x01};
    byte[] GET_TODAY_FITNESS = new byte[]{(byte) 0xFE, (byte) 0x10};
    byte[] GET_YESTERDAY_FITNESS = new byte[]{(byte) 0xFE, (byte) 0x11};
    byte[] GET_DAY_B4_YESTERDAY_FITNESS = new byte[]{(byte) 0xFE, (byte) 0x12};

    void enableFitnessNotification(boolean isEnabled);

    void startSendingFitnessData();

    void stopSendingFitnessData();

    void vibrate(String pattern);

    void sendRBT();

    void enableMSNotification(boolean isEnabled);

    void enableQTRNotification(boolean isEnabled);

    void setIntensity(String intensity);

    void setFootwearType(String footwearType);

    void getTodayFitness();

    void getYesterdayFitness();

    void getDayB4YesterdayFitness();
}
