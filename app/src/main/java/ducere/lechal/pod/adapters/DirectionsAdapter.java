package ducere.lechal.pod.adapters;

/**
 * Created by Siva on 06-05-2016.
 */
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.here.android.mpa.routing.Maneuver;
import com.here.android.mpa.routing.Route;

import java.util.List;

import ducere.lechal.pod.R;
import ducere.lechal.pod.beans.Place;

public class DirectionsAdapter extends RecyclerView.Adapter<DirectionsAdapter.MyViewHolder>  {


    private Route route;
    public static int directionDrawable, directionDrawableLock;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView tvTurnInstruction;
        public TextView tvTurnDistance;
        public TextView tvTurnInstructionDetails;
        public ImageView ivTurn;


        public MyViewHolder(View view) {
            super(view);
            tvTurnInstruction = (TextView) view.findViewById(R.id.tvTurnInstruction);
            tvTurnInstructionDetails = (TextView) view.findViewById(R.id.tvTurnInstructionDeatils);
            tvTurnDistance=(TextView)view.findViewById(R.id.tvTurnDistance);
            ivTurn=(ImageView)view.findViewById(R.id.ivTurn);



        }


        @Override
        public void onClick(View v) {



        }
    }


    public DirectionsAdapter(Route route) {
        this.route = route;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.turn_list, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
       // Place place = placeList.get(position);
        holder.tvTurnInstruction.setText(directions(route.getManeuvers().get(position), route.getManeuvers().get(position).getTurn(), 0));
        holder.tvTurnInstructionDetails.setText( route.getManeuvers().get(position).getNextRoadName() + "" );
        holder.tvTurnDistance.setText(Math.round(route.getManeuvers().get(position).getDistanceToNextManeuver()/1000.0)+"km");
        holder.ivTurn.setBackgroundResource(directionDrawable);


    }


    @Override
    public int getItemCount() {
        return route.getManeuvers().size();
    }

    public static String directions(Maneuver maneuver, Maneuver.Turn directions, int color) {
        String turn = "";

        //Log.d("directions",directions.name()+"");
        if (directions == Maneuver.Turn.UNDEFINED || directions == Maneuver.Turn.NO_TURN) {
            turn = "Go Straight up to ";
            if (color == 0) {
                directionDrawable = R.drawable.turn_go_straight;
                // directionDrawableLock = R.drawable.nav_turn_go_straight_white_lock;
            } else {
                directionDrawable = R.drawable.ic_turn_straight_blue;
            }
        } else if (directions == Maneuver.Turn.KEEP_MIDDLE) {
            turn = "Keep middle ";
            if (color == 0) {
                directionDrawable = R.drawable.turn_keep_middle_blue;
                //directionDrawableLock = R.drawable.nav_turn_keep_middle_white_lock;
            } else {
                directionDrawable = R.drawable.ic_turn_straight_blue;
            }
        } else if (directions == Maneuver.Turn.LIGHT_RIGHT) {
            turn = "Take Slight Right ";
            if (color == 0) {
                directionDrawable = R.drawable.turn_slight_right_blue;
                //directionDrawableLock = R.drawable.nav_turn_slight_right_white_lock;
            } else {
                directionDrawable = R.drawable.ic_turn_straight_blue;
            }

        } else if (directions == Maneuver.Turn.KEEP_RIGHT) {
            turn = "Keep Right ";
            if (color == 0) {
                directionDrawable = R.drawable.turn_keep_right_blue;
                //  directionDrawableLock = R.drawable.nav_turn_keep_right_white_lock;
            } else {
                directionDrawable = R.drawable.ic_turn_straight_blue;
            }
        } else if (directions == Maneuver.Turn.HEAVY_RIGHT) {
            turn = "Take Sharp Right ";
            if (color == 0) {
                directionDrawable = R.drawable.turn_sharp_right_blue;
                // directionDrawableLock = R.drawable.nav_turn_sharp_right_white_lock;
            } else {
                directionDrawable = R.drawable.ic_turn_straight_blue;
            }
        } else if (directions == Maneuver.Turn.QUITE_RIGHT) {
            turn = "Turn Right ";
            if (color == 0) {
                directionDrawable = R.drawable.turn_right_blue;
                // directionDrawableLock = R.drawable.nav_turn_right_white_lock;
            } else {
                directionDrawable = R.drawable.ic_turn_straight_blue;
            }
        } else if (directions == Maneuver.Turn.LIGHT_LEFT) {
            turn = "Take Slight Left ";
            if (color == 0) {
                directionDrawable = R.drawable.turn_slight_left_blue;
                //  directionDrawableLock = R.drawable.nav_turn_slight_left_white_lock;
            } else {
                directionDrawable = R.drawable.ic_turn_straight_blue;
            }
        } else if (directions == Maneuver.Turn.KEEP_LEFT) {
            turn = "Keep Left ";
            if (color == 0) {
                directionDrawable = R.drawable.turn_keep_left_blue;
                //  directionDrawableLock = R.drawable.nav_turn_keep_left_white_lock;
            } else {
                directionDrawable = R.drawable.ic_turn_straight_blue;
            }
        } else if (directions == Maneuver.Turn.HEAVY_LEFT) {
            turn = "Take Sharp Left ";
            if (color == 0) {
                directionDrawable = R.drawable.turn_sharp_left_blue;
                // directionDrawableLock = R.drawable.nav_turn_sharp_left_white_lock;
            } else {
                directionDrawable = R.drawable.ic_turn_straight_blue;
            }
        } else if (directions == Maneuver.Turn.QUITE_LEFT) {
            turn = "Turn Left ";
            if (color == 0) {
                directionDrawable = R.drawable.turn_left_blue;
                //directionDrawableLock = R.drawable.nav_turn_left_white_lock;
            } else {
                directionDrawable = R.drawable.ic_turn_straight_blue;
            }

        } else if (directions == Maneuver.Turn.RETURN) {
            turn = "Take About Turn ";
            if (color == 0) {
                directionDrawable = R.drawable.turn_uturn_right_blue;
                // directionDrawableLock = R.drawable.nav_turn_uturn_right_white_lock;
            } else {
                directionDrawable = R.drawable.ic_turn_straight_blue;
                // directionDrawableLock = R.drawable.nav_turn_uturn_left_white_lock;
            }
        } else {
            turn = "Take Round About ";
            directionDrawable = R.drawable.turn_uturn_right_blue;
           /* if (directions == Maneuver.Turn.ROUNDABOUT_1) {

                // directionDrawableLock = R.drawable.nav_round_about_1;


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
            }*/


        }

        return turn;
    }

}

