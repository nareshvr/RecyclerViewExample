package ducere.lechal.pod.utilities;

import android.app.ActivityManager;
import android.app.KeyguardManager;
import android.content.Context;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Toast;

import com.here.android.mpa.common.GeoCoordinate;
import com.here.android.mpa.common.GeoPosition;
import com.here.android.mpa.guidance.NavigationManager;
import com.here.android.mpa.mapping.Map;
import com.here.android.mpa.routing.Maneuver;
import com.here.android.mpa.routing.RouteOptions;

import java.util.List;

import ducere.lechal.pod.LechalApplication;
import ducere.lechal.pod.MainActivity;
import ducere.lechal.pod.R;
import ducere.lechal.pod.constants.Constants;
import ducere.lechal.pod.constants.Vibrations;

/**
 * Created by Siva on 13-05-2016.
 */
public class NavigationFeedback implements Vibrations{
    public static GeoCoordinate position;
    public static GeoPosition geoPosition;
    NavigationManager navigationManager;
    Maneuver currentManeuver;
    double distanceNext;
    Context context;
    double finalDist=0;


    public NavigationFeedback(GeoPosition geoPosition, GeoCoordinate position,Context context) {
        this.position=position;
        this.geoPosition = geoPosition;
        this.navigationManager = LechalApplication.getInstance().getNavigationManager();
        this.context = context;
    }
    public void findDistanceRange(){
        currentManeuver = navigationManager.getNextManeuver();
        distanceNext = navigationManager.getNextManeuverDistance();

        if(distanceNext<FeedbackRanges.turnDistance4
                &&  currentManeuver.getAction() != com.here.android.mpa.routing.Maneuver.Action.END){
           double d1 = geoPosition.getSpeed() * (6);  // 4 sec for vbt pattern and 1 sec for the user to take action
            //Added buffer meters
            if (LechalApplication.getInstance().getNavigate().getMode() == 1) {
                d1 = d1 + 25;
                finalDist=25;
            } else {
                d1 = d1 + 50;
                finalDist=40;
            }
           double accuracy = (geoPosition.getLongitudeAccuracy() + geoPosition.getLatitudeAccuracy()) ;


            if ((((distanceNext - accuracy) <= d1)|| distanceNext <finalDist) && !LechalApplication.getInstance().isTurnTaken1())  {
                sendVibration(4);

                LechalApplication.getInstance().setIsTurnTaken1(true);
                LechalApplication.getInstance().setIsTurnTaken2(true);
                LechalApplication.getInstance().setIsTurnTaken3(true);
                LechalApplication.getInstance().setIsTurnTaken4(true);
            }


        }else if(distanceNext>FeedbackRanges.turnDistance3a&&distanceNext<FeedbackRanges.turnDistance3 &&
               !LechalApplication.getInstance().isTurnTaken2()){

            sendVibration(3);

            LechalApplication.getInstance().setIsTurnTaken2(true);
            LechalApplication.getInstance().setIsTurnTaken3(true);
            LechalApplication.getInstance().setIsTurnTaken4(true);
        }else if(distanceNext>FeedbackRanges.turnDistance2a&&distanceNext<FeedbackRanges.turnDistance2 &&
                !LechalApplication.getInstance().isTurnTaken3()){
            sendVibration(2);

            LechalApplication.getInstance().setIsTurnTaken3(true);
            LechalApplication.getInstance().setIsTurnTaken4(true);

        }else if(distanceNext>FeedbackRanges.turnDistance1a&&distanceNext<FeedbackRanges.turnDistance1 &&
               !LechalApplication.getInstance().isTurnTaken4()) {

            sendVibration(1);
            LechalApplication.getInstance().setIsTurnTaken4(true);

        } if (distanceNext < 20 && !LechalApplication.getInstance().isDestinationReached() && currentManeuver.getAction() == com.here.android.mpa.routing.Maneuver.Action.END) {
            navigationManager.stop();
            sendVibration(4);
           LechalApplication.getInstance().setIsDestinationReached(true);
        }

    }

    private void sendVibration( int i) {


        if (currentManeuver.getTurn() == Maneuver.Turn.HEAVY_LEFT) {

            switch (i){
                case 1:
                    Constants.sendVibrate(context,VB, IN_1000_MTS.sharpTurn, "00" );
                    break;
                case 2:
                    Constants.sendVibrate(context, VB, IN_500_MTS.sharpTurn, "00");
                    break;
                case 3:
                    Constants.sendVibrate(context, VB, IN_200_MTS.sharpTurn, "00");
                    break;
                case 4:
                    Constants.sendVibrate(context, VB, NOW.sharpTurn, "00");
                    break;
            }

        } else if (currentManeuver.getTurn() == Maneuver.Turn.KEEP_LEFT) {
            switch (i){
                case 1:
                    Constants.sendVibrate(context, VB, IN_1000_MTS.keep, "00");
                    break;
                case 2:
                    Constants.sendVibrate(context, VB, IN_500_MTS.keep, "00");
                    break;
                case 3:
                    Constants.sendVibrate(context, VB, IN_200_MTS.keep, "00");
                    break;
                case 4:
                    Constants.sendVibrate(context, VB, NOW.keep, "00");
                    break;
            }

        } else if (currentManeuver.getTurn() == Maneuver.Turn.LIGHT_LEFT) {
            switch (i){
                 case 1:
                    Constants.sendVibrate(context, VB, IN_1000_MTS.slightTurn, "00");
                    break;
                case 2:
                    Constants.sendVibrate(context, VB, IN_500_MTS.slightTurn, "00");
                    break;
                case 3:
                    Constants.sendVibrate(context, VB, IN_200_MTS.slightTurn, "00");
                    break;
                case 4:
                    Constants.sendVibrate(context, VB, NOW.slightTurn, "00");
                    break;
            }

        } else if (currentManeuver.getTurn() == Maneuver.Turn.QUITE_LEFT) {
            switch (i){
                case 1:
                    Constants.sendVibrate(context, VB, IN_1000_MTS.normalTurn, "00");
                    break;
                case 2:
                    Constants.sendVibrate(context, VB, IN_500_MTS.normalTurn, "00");
                    break;
                case 3:
                    Constants.sendVibrate(context, VB, IN_200_MTS.normalTurn, "00");
                    break;
                case 4:
                    Constants.sendVibrate(context, VB, NOW.normalTurn, "00");
                    break;
            }

        } else if (currentManeuver.getTurn() == Maneuver.Turn.HEAVY_RIGHT) {
            switch (i){
                case 1:
                    Constants.sendVibrate(context, VB, "00", IN_1000_MTS.sharpTurn);
                    break;
                case 2:
                    Constants.sendVibrate(context, VB, "00", IN_500_MTS.sharpTurn);
                    break;
                case 3:
                    Constants.sendVibrate(context, VB, "00", IN_200_MTS.sharpTurn);
                    break;
                case 4:
                    Constants.sendVibrate(context, VB, "00", NOW.sharpTurn);
                    break;
            }

        } else if (currentManeuver.getTurn() == Maneuver.Turn.KEEP_RIGHT) {
            switch (i){
                case 1:
                    Constants.sendVibrate(context, VB, "00", IN_1000_MTS.keep);
                    break;
                case 2:
                    Constants.sendVibrate(context, VB, "00", IN_500_MTS.keep);
                    break;
                case 3:
                    Constants.sendVibrate(context, VB, "00", IN_200_MTS.keep);
                    break;
                case 4:
                    Constants.sendVibrate(context, VB, "00", NOW.keep);
                    break;
            }

        } else if (currentManeuver.getTurn() == Maneuver.Turn.LIGHT_RIGHT) {
            switch (i){
                case 1:
                    Constants.sendVibrate(context, VB, "00", IN_1000_MTS.slightTurn);
                    break;
                case 2:
                    Constants.sendVibrate(context, VB, "00", IN_500_MTS.slightTurn);
                    break;
                case 3:
                    Constants.sendVibrate(context, VB, "00", IN_200_MTS.slightTurn);
                    break;
                case 4:
                    Constants.sendVibrate(context, VB, "00", NOW.slightTurn);
                    break;
            }

        } else if (currentManeuver.getTurn() == Maneuver.Turn.QUITE_RIGHT) {
            switch (i){
                case 1:
                    Constants.sendVibrate(context, VB, "00", IN_1000_MTS.normalTurn);
                    break;
                case 2:
                    Constants.sendVibrate(context, VB, "00", IN_500_MTS.normalTurn);
                    break;
                case 3:
                    Constants.sendVibrate(context, VB, "00", IN_500_MTS.normalTurn);
                    break;
                case 4:
                    Constants.sendVibrate(context, VB, "00", NOW.normalTurn);
                    break;
            }

        } else if (currentManeuver.getTurn() == Maneuver.Turn.RETURN) {
            switch (i){
                case 1:
                    Constants.sendVibrate(context, VB, IN_1000_MTS.uTurn, "00");
                    break;
                case 2:
                    Constants.sendVibrate(context, VB, IN_500_MTS.uTurn, "00");
                    break;
                case 3:
                    Constants.sendVibrate(context, VB, IN_200_MTS.uTurn, "00");
                    break;
                case 4:
                    Constants.sendVibrate(context, VB, NOW.uTurn, "00");
                    break;
            }

        } else if (currentManeuver.getTurn() == Maneuver.Turn.KEEP_MIDDLE) {

            switch (i){
                case 1:
                    Constants.sendVibrate(context, VB, straight, straight);
                    break;
                case 2:
                    Constants.sendVibrate(context, VB, straight, straight);
                    break;
                case 3:
                    Constants.sendVibrate(context, VB, straight, straight);
                    break;
                case 4:
                    Constants.sendVibrate(context, VB, straight, straight);
                    break;
            }
        }else if (currentManeuver.getAction() == com.here.android.mpa.routing.Maneuver.Action.END) {
            double offRoadOnRoadAngle = LechalApplication.getInstance().getRoute().getDestination().getHeading(LechalApplication.getInstance().getRoute().getManeuvers().get(LechalApplication.getInstance().getRoute().getManeuvers().size() - 1).getCoordinate());
            double destToLastTurn = LechalApplication.getInstance().getRoute().getManeuvers().get(LechalApplication.getInstance().getRoute().getManeuvers().size() - 1).getCoordinate().getHeading(LechalApplication.getInstance().getRoute().getManeuvers().get(LechalApplication.getInstance().getRoute().getManeuvers().size() - 2).getCoordinate());

            switch (i){
                case 1:
                    Constants.sendVibrate(context, VB, IN_1000_MTS.destination, IN_1000_MTS.destination);
                    break;
                case 2:
                    Constants.sendVibrate(context, VB, IN_500_MTS.destination, IN_500_MTS.destination);
                    break;
                case 3:
                    Constants.sendVibrate(context, VB, IN_200_MTS.destination, IN_200_MTS.destination);
                    break;
                case 4:
                    Constants.sendVibrate(context, VB, NOW.destination, NOW.destination);
                    break;
            }
            if ((Math.sin((destToLastTurn - offRoadOnRoadAngle) * Math.PI / 180)) > 0) {//left
                new CountDownTimer(1000, 1000) {

                    @Override
                    public void onTick(long millisUntilFinished) {

                    }

                    @Override
                    public void onFinish() {
                        Constants.sendVibrate(context, VB, suffix, "00");

                    }
                }.start();

            } else if ((Math.sin((destToLastTurn - offRoadOnRoadAngle) * Math.PI / 180)) < 0) {//right
                new CountDownTimer(1000, 1000) {

                    @Override
                    public void onTick(long millisUntilFinished) {

                    }

                    @Override
                    public void onFinish() {
                        Constants.sendVibrate(context, VB,  "00", suffix);

                    }
                }.start();

            } else {


            }

        } else if (currentManeuver.getTurn() == Maneuver.Turn.UNDEFINED || currentManeuver.getTurn() == Maneuver.Turn.NO_TURN) {
            switch (i){
                case 1:
                    Constants.sendVibrate(context, VB, straight, straight);
                    break;
                case 2:
                    Constants.sendVibrate(context, VB, straight, straight);
                    break;
                case 3:
                    Constants.sendVibrate(context, VB, straight, straight);
                    break;
                case 4:
                    Constants.sendVibrate(context, VB, straight, straight);
                    break;
            }

        }

    }

}
