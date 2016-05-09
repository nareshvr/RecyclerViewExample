package ducere.lechal.pod;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.here.android.mpa.guidance.NavigationManager;
import com.here.android.mpa.routing.Maneuver;
import com.here.android.mpa.routing.Route;
import com.here.android.mpa.routing.RouteResult;

import ducere.lechal.pod.adapters.TurnListCustomAdapter;

/**
 * Created by Siva on 04-05-2016.
 */
public class RouteHeaderFragment extends Fragment {

    TextView tvDistance,tvTime,tvVia;
    static RouteResult routeResult;
    public static int directionDrawable, directionDrawableLock;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.route_header_fragment, container, false);
        tvDistance = (TextView)v.findViewById(R.id.tvDistance);
        tvTime = (TextView)v.findViewById(R.id.tvTime);
        tvVia = (TextView)v.findViewById(R.id.tvVia);
        tvDistance.setText("("+Math.round(routeResult.getRoute().getLength()/1000.0) + "Km)");
        tvTime.setText(Math.round((routeResult.getRoute().getTta(Route.TrafficPenaltyMode.OPTIMAL, Route.WHOLE_ROUTE).getDuration()) / 60) + " min");


        return v;
    }

    public static  RouteHeaderFragment newInstance(RouteResult result) {

        RouteHeaderFragment f = new RouteHeaderFragment();
        routeResult = result;
        Bundle b = new Bundle();

        f.setArguments(b);

        return f;
    }
    public static String directions(Maneuver maneuver, Maneuver.Turn directions, int color) {
        String turn = "";

        //Log.d("directions",directions.name()+"");
        if (directions == Maneuver.Turn.UNDEFINED || directions == Maneuver.Turn.NO_TURN) {
            turn = "Go Straight up to ";
            if (color == 0) {
                directionDrawable = R.drawable.ic_turn_straight_blue;
                // directionDrawableLock = R.drawable.nav_turn_go_straight_white_lock;
            } else {
                directionDrawable = R.drawable.ic_turn_straight_blue;
            }
        } else if (directions == Maneuver.Turn.KEEP_MIDDLE) {
            turn = "Keep middle ";
            if (color == 0) {
                directionDrawable = R.drawable.ic_turn_straight_blue;
                //directionDrawableLock = R.drawable.nav_turn_keep_middle_white_lock;
            } else {
                directionDrawable = R.drawable.ic_turn_straight_blue;
            }
        } else if (directions == Maneuver.Turn.LIGHT_RIGHT) {
            turn = "Take Slight Right ";
            if (color == 0) {
                directionDrawable = R.drawable.ic_turn_straight_blue;
                //directionDrawableLock = R.drawable.nav_turn_slight_right_white_lock;
            } else {
                directionDrawable = R.drawable.ic_turn_straight_blue;
            }

        } else if (directions == Maneuver.Turn.KEEP_RIGHT) {
            turn = "Keep Right ";
            if (color == 0) {
                directionDrawable = R.drawable.ic_turn_straight_blue;
                //  directionDrawableLock = R.drawable.nav_turn_keep_right_white_lock;
            } else {
                directionDrawable = R.drawable.ic_turn_straight_blue;
            }
        } else if (directions == Maneuver.Turn.HEAVY_RIGHT) {
            turn = "Take Sharp Right ";
            if (color == 0) {
                directionDrawable = R.drawable.ic_turn_straight_blue;
                // directionDrawableLock = R.drawable.nav_turn_sharp_right_white_lock;
            } else {
                directionDrawable = R.drawable.ic_turn_straight_blue;
            }
        } else if (directions == Maneuver.Turn.QUITE_RIGHT) {
            turn = "Turn Right ";
            if (color == 0) {
                directionDrawable = R.drawable.ic_turn_straight_blue;
                // directionDrawableLock = R.drawable.nav_turn_right_white_lock;
            } else {
                directionDrawable = R.drawable.ic_turn_straight_blue;
            }
        } else if (directions == Maneuver.Turn.LIGHT_LEFT) {
            turn = "Take Slight Left ";
            if (color == 0) {
                directionDrawable = R.drawable.ic_turn_straight_blue;
                //  directionDrawableLock = R.drawable.nav_turn_slight_left_white_lock;
            } else {
                directionDrawable = R.drawable.ic_turn_straight_blue;
            }
        } else if (directions == Maneuver.Turn.KEEP_LEFT) {
            turn = "Keep Left ";
            if (color == 0) {
                directionDrawable = R.drawable.ic_turn_straight_blue;
                //  directionDrawableLock = R.drawable.nav_turn_keep_left_white_lock;
            } else {
                directionDrawable = R.drawable.ic_turn_straight_blue;
            }
        } else if (directions == Maneuver.Turn.HEAVY_LEFT) {
            turn = "Take Sharp Left ";
            if (color == 0) {
                directionDrawable = R.drawable.ic_turn_straight_blue;
                // directionDrawableLock = R.drawable.nav_turn_sharp_left_white_lock;
            } else {
                directionDrawable = R.drawable.ic_turn_straight_blue;
            }
        } else if (directions == Maneuver.Turn.QUITE_LEFT) {
            turn = "Turn Left ";
            if (color == 0) {
                directionDrawable = R.drawable.ic_turn_straight_blue;
                //directionDrawableLock = R.drawable.nav_turn_left_white_lock;
            } else {
                directionDrawable = R.drawable.ic_turn_straight_blue;
            }

        } else if (directions == Maneuver.Turn.RETURN) {
            turn = "Take About Turn ";
            if (color == 0) {
                directionDrawable = R.drawable.ic_turn_straight_blue;
                // directionDrawableLock = R.drawable.nav_turn_uturn_right_white_lock;
            } else {
                directionDrawable = R.drawable.ic_turn_straight_blue;
                // directionDrawableLock = R.drawable.nav_turn_uturn_left_white_lock;
            }
        } else {
            turn = "Take Round About ";
            if (directions == Maneuver.Turn.ROUNDABOUT_1) {

                // directionDrawableLock = R.drawable.nav_round_about_1;
                directionDrawable = R.drawable.ic_turn_straight_blue;

            } else if (directions == Maneuver.Turn.ROUNDABOUT_2) {

                // directionDrawableLock = R.drawable.nav_round_about_2;
                directionDrawable = R.drawable.ic_turn_straight_blue;

            } else if (directions == Maneuver.Turn.ROUNDABOUT_3) {

                // directionDrawableLock = R.drawable.nav_round_about_3;
                directionDrawable = R.drawable.ic_turn_straight_blue;

            } else if (directions == Maneuver.Turn.ROUNDABOUT_4) {

                // directionDrawableLock = R.drawable.nav_round_about_4;
                directionDrawable = R.drawable.ic_turn_straight_blue;

            } else if (directions == Maneuver.Turn.ROUNDABOUT_5) {

                //  directionDrawableLock = R.drawable.nav_round_about_5;
                directionDrawable = R.drawable.ic_turn_straight_blue;

            } else if (directions == Maneuver.Turn.ROUNDABOUT_6) {

                // directionDrawableLock = R.drawable.nav_round_about_6;
                directionDrawable = R.drawable.ic_turn_straight_blue;

            } else if (directions == Maneuver.Turn.ROUNDABOUT_7) {

                //  directionDrawableLock = R.drawable.nav_round_about_7;
                directionDrawable = R.drawable.ic_turn_straight_blue;

            } else if (directions == Maneuver.Turn.ROUNDABOUT_8) {

                // directionDrawableLock = R.drawable.nav_round_about_8;
                directionDrawable = R.drawable.ic_turn_straight_blue;

            } else if (directions == Maneuver.Turn.ROUNDABOUT_9) {

                // directionDrawableLock = R.drawable.nav_round_about_9;
                directionDrawable = R.drawable.ic_turn_straight_blue;

            } else if (directions == Maneuver.Turn.ROUNDABOUT_10) {

                //  directionDrawableLock = R.drawable.nav_round_about_10;
                directionDrawable = R.drawable.ic_turn_straight_blue;

            } else if (directions == Maneuver.Turn.ROUNDABOUT_11) {

                //  directionDrawableLock = R.drawable.nav_round_about_11;
                directionDrawable = R.drawable.ic_turn_straight_blue;


            } else if (directions == Maneuver.Turn.ROUNDABOUT_12) {

                // directionDrawableLock = R.drawable.nav_round_about_12;
                directionDrawable = R.drawable.ic_turn_straight_blue;
            }


        }

        return turn;
    }
}
