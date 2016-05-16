package ducere.lechal.pod.utilities;

import android.content.Context;
import android.widget.Toast;

import com.here.android.mpa.common.GeoCoordinate;
import com.here.android.mpa.guidance.NavigationManager;
import com.here.android.mpa.routing.Maneuver;

import ducere.lechal.pod.LechalApplication;

/**
 * Created by Siva on 13-05-2016.
 */
public class NavigationFeedback {
    public static GeoCoordinate position;
    NavigationManager navigationManager;
    Maneuver currentManeuver;
    double distanceNext;
    Context context;


    public NavigationFeedback(GeoCoordinate position,Context context) {
        this.position=position;
        this.navigationManager = LechalApplication.getInstance().getNavigationManager();
        this.context = context;
    }
    public void findDistanceRange(){
        currentManeuver = navigationManager.getNextManeuver();
        distanceNext = navigationManager.getNextManeuverDistance();

        if(distanceNext>FeedbackRanges.turnDistance4a&&distanceNext<FeedbackRanges.turnDistance4 &&
                !LechalApplication.getInstance().isTurnTaken1()){

            sendVibration(navigationManager.getNextManeuver().getTurn(),4);

            LechalApplication.getInstance().setIsTurnTaken1(true);
            LechalApplication.getInstance().setIsTurnTaken2(true);
            LechalApplication.getInstance().setIsTurnTaken3(true);
            LechalApplication.getInstance().setIsTurnTaken4(true);
        }else if(distanceNext>FeedbackRanges.turnDistance3a&&distanceNext<FeedbackRanges.turnDistance3 &&
               !LechalApplication.getInstance().isTurnTaken2()){

            sendVibration(navigationManager.getNextManeuver().getTurn(),3);

            LechalApplication.getInstance().setIsTurnTaken2(true);
            LechalApplication.getInstance().setIsTurnTaken3(true);
            LechalApplication.getInstance().setIsTurnTaken4(true);
        }else if(distanceNext>FeedbackRanges.turnDistance2a&&distanceNext<FeedbackRanges.turnDistance2 &&
                !LechalApplication.getInstance().isTurnTaken3()){
            sendVibration(navigationManager.getNextManeuver().getTurn(),2);

            LechalApplication.getInstance().setIsTurnTaken3(true);
            LechalApplication.getInstance().setIsTurnTaken4(true);

        }else if(distanceNext>FeedbackRanges.turnDistance1a&&distanceNext<FeedbackRanges.turnDistance1 &&
               !LechalApplication.getInstance().isTurnTaken4()) {

            sendVibration(navigationManager.getNextManeuver().getTurn(),1);
            LechalApplication.getInstance().setIsTurnTaken4(true);

        }

    }

    private void sendVibration(Maneuver.Turn turn, int i) {


        if (currentManeuver.getTurn() == Maneuver.Turn.HEAVY_LEFT) {

            switch (i){
                case 1:
                    Toast.makeText(context, "SharpLeft1", Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    Toast.makeText(context, "SharpLeft2", Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    Toast.makeText(context, "SharpLeft3", Toast.LENGTH_SHORT).show();
                    break;
                case 4:
                    Toast.makeText(context, "SharpLeft4", Toast.LENGTH_SHORT).show();
                    break;
            }

        } else if (currentManeuver.getTurn() == Maneuver.Turn.KEEP_LEFT) {
            switch (i){
                case 1:
                    Toast.makeText(context, "SharpLeft1", Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    Toast.makeText(context, "SharpLeft2", Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    Toast.makeText(context, "SharpLeft3", Toast.LENGTH_SHORT).show();
                    break;
                case 4:
                    Toast.makeText(context, "SharpLeft4", Toast.LENGTH_SHORT).show();
                    break;
            }

        } else if (currentManeuver.getTurn() == Maneuver.Turn.LIGHT_LEFT) {
            switch (i){
                case 1:
                    Toast.makeText(context, "SharpLeft1", Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    Toast.makeText(context, "SharpLeft2", Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    Toast.makeText(context, "SharpLeft3", Toast.LENGTH_SHORT).show();
                    break;
                case 4:
                    Toast.makeText(context, "SharpLeft4", Toast.LENGTH_SHORT).show();
                    break;
            }

        } else if (currentManeuver.getTurn() == Maneuver.Turn.QUITE_LEFT) {
            switch (i){
                case 1:
                    Toast.makeText(context, "SharpLeft1", Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    Toast.makeText(context, "SharpLeft2", Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    Toast.makeText(context, "SharpLeft3", Toast.LENGTH_SHORT).show();
                    break;
                case 4:
                    Toast.makeText(context, "SharpLeft4", Toast.LENGTH_SHORT).show();
                    break;
            }

        } else if (currentManeuver.getTurn() == Maneuver.Turn.HEAVY_RIGHT) {
            switch (i){
                case 1:
                    Toast.makeText(context, "SharpLeft1", Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    Toast.makeText(context, "SharpLeft2", Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    Toast.makeText(context, "SharpLeft3", Toast.LENGTH_SHORT).show();
                    break;
                case 4:
                    Toast.makeText(context, "SharpLeft4", Toast.LENGTH_SHORT).show();
                    break;
            }

        } else if (currentManeuver.getTurn() == Maneuver.Turn.KEEP_RIGHT) {
            switch (i){
                case 1:
                    Toast.makeText(context, "SharpLeft1", Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    Toast.makeText(context, "SharpLeft2", Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    Toast.makeText(context, "SharpLeft3", Toast.LENGTH_SHORT).show();
                    break;
                case 4:
                    Toast.makeText(context, "SharpLeft4", Toast.LENGTH_SHORT).show();
                    break;
            }

        } else if (currentManeuver.getTurn() == Maneuver.Turn.LIGHT_RIGHT) {
            switch (i){
                case 1:
                    Toast.makeText(context, "SharpLeft1", Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    Toast.makeText(context, "SharpLeft2", Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    Toast.makeText(context, "SharpLeft3", Toast.LENGTH_SHORT).show();
                    break;
                case 4:
                    Toast.makeText(context, "SharpLeft4", Toast.LENGTH_SHORT).show();
                    break;
            }

        } else if (currentManeuver.getTurn() == Maneuver.Turn.QUITE_RIGHT) {
            switch (i){
                case 1:
                    Toast.makeText(context, "SharpLeft1", Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    Toast.makeText(context, "SharpLeft2", Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    Toast.makeText(context, "SharpLeft3", Toast.LENGTH_SHORT).show();
                    break;
                case 4:
                    Toast.makeText(context, "SharpLeft4", Toast.LENGTH_SHORT).show();
                    break;
            }

        } else if (currentManeuver.getTurn() == Maneuver.Turn.RETURN) {
            switch (i){
                case 1:
                    Toast.makeText(context, "SharpLeft1", Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    Toast.makeText(context, "SharpLeft2", Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    Toast.makeText(context, "SharpLeft3", Toast.LENGTH_SHORT).show();
                    break;
                case 4:
                    Toast.makeText(context, "SharpLeft4", Toast.LENGTH_SHORT).show();
                    break;
            }

        } else if (currentManeuver.getTurn() == Maneuver.Turn.KEEP_MIDDLE) {

            switch (i){
                case 1:
                    Toast.makeText(context, "SharpLeft1", Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    Toast.makeText(context, "SharpLeft2", Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    Toast.makeText(context, "SharpLeft3", Toast.LENGTH_SHORT).show();
                    break;
                case 4:
                    Toast.makeText(context, "SharpLeft4", Toast.LENGTH_SHORT).show();
                    break;
            }
        } else if (currentManeuver.getTurn() == Maneuver.Turn.UNDEFINED || currentManeuver.getTurn() == Maneuver.Turn.NO_TURN) {
            switch (i){
                case 1:
                    Toast.makeText(context, "SharpLeft1", Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    Toast.makeText(context, "SharpLeft2", Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    Toast.makeText(context, "SharpLeft3", Toast.LENGTH_SHORT).show();
                    break;
                case 4:
                    Toast.makeText(context, "SharpLeft4", Toast.LENGTH_SHORT).show();
                    break;
            }

        }

    }

}
