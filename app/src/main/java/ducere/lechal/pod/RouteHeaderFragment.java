package ducere.lechal.pod;

import android.app.Activity;
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
    OnClickRoute onClickRoute;
    public interface OnClickRoute{
        void onClick();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.route_header_fragment, container, false);
        tvDistance = (TextView)v.findViewById(R.id.tvDistance);
        tvTime = (TextView)v.findViewById(R.id.tvTime);
        tvVia = (TextView)v.findViewById(R.id.tvVia);
        tvDistance.setText("("+Math.round(routeResult.getRoute().getLength()/1000.0) + "Km)");
        tvTime.setText(Math.round((routeResult.getRoute().getTta(Route.TrafficPenaltyMode.OPTIMAL, Route.WHOLE_ROUTE).getDuration()) / 60) + " min");
    v.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            onClickRoute.onClick();
        }
    });

        return v;
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            onClickRoute = (OnClickRoute ) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement MyInterface ");
        }
    }
    public static  RouteHeaderFragment newInstance(RouteResult result) {

        RouteHeaderFragment f = new RouteHeaderFragment();
        routeResult = result;
        Bundle b = new Bundle();

        f.setArguments(b);

        return f;
    }

}
