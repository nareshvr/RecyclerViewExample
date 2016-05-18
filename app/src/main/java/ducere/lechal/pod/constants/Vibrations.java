package ducere.lechal.pod.constants;

/**
 * Created by Siva on 05-12-2015.
 */

public interface Vibrations {
    String VB = "VB";

    String VIBRATE_LEFT_POD = VB + "0100";
    String VIBRATE_RIGHT_POD = VB + "0001";

    //RoundAbout
    String roundAboutRight = "24";
    String roundAboutLeft = "23";
    String straight = "20";

    interface NOW {
        String straight = Vibrations.straight;
        String slightTurn = "31";
        String normalTurn = "35";
        String sharpTurn = "51";
        String keep = "31";
        String uTurn = "43";
        String destination = "19";
        String roundAboutRight = Vibrations.roundAboutRight;
        String roundAboutLeft = Vibrations.roundAboutLeft;
    }

    interface IN_100_MTS {
        String straight = Vibrations.straight;
        String anyTurn = "16";
        String uTurn = "17";
        String roundAboutRight = Vibrations.roundAboutRight;
        String roundAboutLeft = Vibrations.roundAboutLeft;
    }

    interface IN_200_MTS {
        String straight = Vibrations.straight;
        String slightTurn = "30";
        String normalTurn = "34";
        String sharpTurn = "50";
        String keep = "30";
        String uTurn = "42";
        String destination = "34";
        String roundAboutRight = Vibrations.roundAboutRight;
        String roundAboutLeft = Vibrations.roundAboutLeft;
    }

    interface IN_500_MTS {
        String straight = Vibrations.straight;
        String slightTurn = "29";
        String normalTurn = "33";
        String sharpTurn = "49";
        String keep = "29";
        String uTurn = "41";
        String destination = "33";
        String roundAboutRight = Vibrations.roundAboutRight;
        String roundAboutLeft = Vibrations.roundAboutLeft;
    }

    interface IN_1000_MTS {
        String straight = Vibrations.straight;
        String slightTurn = "28";
        String normalTurn = "32";
        String sharpTurn = "48";
        String keep = "28";
        String uTurn = "40";
        String destination = "32";
        String roundAboutRight = Vibrations.roundAboutRight;
        String roundAboutLeft = Vibrations.roundAboutLeft;
    }

    interface INTENSITY{
        String intensity_Very_High = "VI5";
        String intensity_High = "VI4";
        String intensity_Medium = "VI3";
        String intensity_Low = "VI2";
        String intensity_Very_Low = "VI1";
    }


    //Re_Route
    String reRoute_left = "21";
    String reRoute_right = "22";

    //Orientation
    String orien_left = "01";
    String orien_right = "02";

    //suffix
    String suffix = "01";

    //Look at mobile
    String lookAtMobile = "01";

    String lowBattery = "46";

    //confirmation

    String confirmation = "01";
    String confirmation_2k = "15";

}
