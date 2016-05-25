package ducere.lechal.pod;

import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.here.android.mpa.common.GeoCoordinate;
import com.here.android.mpa.common.Image;
import com.here.android.mpa.common.MapEngine;
import com.here.android.mpa.common.OnEngineInitListener;
import com.here.android.mpa.common.PositioningManager;
import com.here.android.mpa.mapping.Map;
import com.here.android.mpa.mapping.MapContainer;
import com.here.android.mpa.mapping.MapFragment;
import com.here.android.mpa.mapping.MapMarker;
import com.poliveira.apps.parallaxlistview.ParallaxScrollView;

import java.io.IOException;
import java.lang.ref.WeakReference;

import ducere.lechal.pod.constants.SharedPrefUtil;
import np.TextView;

/**
 * Created by VR Naresh on 03-05-2016.
 */
public class Journeys extends Fragment {

    View view;
    TextView tvLocation,tvAddress;
    private boolean isMapEngineInitialize=false;
    private MapFragment mapFragment;
    private Map map;
    CardView cardGroupJourney;

    public Journeys() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragmentview = inflater.inflate(R.layout.fragment_fitness, container, false);
        view = inflater.inflate(R.layout.fragment_journey, container, false);
        ParallaxScrollView mScrollView = (ParallaxScrollView) view.findViewById(R.id.view1);
        mScrollView.setParallaxView(getActivity().getLayoutInflater().inflate(R.layout.journey_header, mScrollView, false));
        cardGroupJourney = (CardView)view.findViewById(R.id.cardGroup);


        tvLocation = (TextView)view.findViewById(R.id.tvPlace);
        tvAddress = (TextView)view.findViewById(R.id.tvAddress);
        tvLocation.setText(SharedPrefUtil.getString(getActivity(),SharedPrefUtil.CURRENT_LOCATION));
        tvAddress.setText(SharedPrefUtil.getString(getActivity(),SharedPrefUtil.CURRENT_VICINITY).replace("<br/>" , ", "));
        cardGroupJourney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            startActivity(new Intent(getActivity(),SearchActivity.class).putExtra("from",2));
            }
        });

        return view;
    }
    public void initMapEngine() {
        final MapEngine mapEngine = MapEngine.getInstance(getActivity());
        mapEngine.init(getActivity(), new OnEngineInitListener() {
            @Override
            public void onEngineInitializationCompleted(Error error) {
                // TODO Auto-generated method stub
                if (error == OnEngineInitListener.Error.NONE) {
                    isMapEngineInitialize = true;
                    initMap();
                    //MapEngine.setOnline(false);
                } else {
                    // handle factory initialization failure

                    Toast.makeText(getActivity(), "cannot initialize map engine : " + error.toString() + "", Toast.LENGTH_SHORT).show();
                    isMapEngineInitialize = false;
                }
            }
        });
    }

    public void initMap() {
        // SearchActivity for the map fragment to finish setup by calling init().
        mapFragment = (com.here.android.mpa.mapping.MapFragment) (getActivity().getFragmentManager().findFragmentById(
                R.id.mapfragment));

        mapFragment.init(new OnEngineInitListener() {
            @Override
            public void onEngineInitializationCompleted(
                    OnEngineInitListener.Error error) {
                if (error == OnEngineInitListener.Error.NONE) {
                    map = mapFragment.getMap();

                    // Set the zoom level to the average between min and max
                    map.setZoomLevel(17);


                    //set the map projection
                    map.setProjectionMode(Map.Projection.GLOBE);



                    GeoCoordinate geo = new GeoCoordinate(SharedPrefUtil.getDouble(getActivity(),SharedPrefUtil.CURRENT_LAT), SharedPrefUtil.getDouble(getActivity(),SharedPrefUtil.CURRENT_LNG));
                    map.setCenter(geo,
                            Map.Animation.LINEAR);

                    // Display position indicator
                    map.getPositionIndicator().setVisible(true);


                    map.setZoomLevel(17);


                } else {
                    //gps.showSettingsAlert();
                    //Log.d("gps", "4");
                    Toast.makeText(getActivity(), "Cannot initialize map fragment : " +
                            error.toString(), Toast.LENGTH_SHORT).show();
                    isMapEngineInitialize = false;
                    initMapEngine();
                }
            }

        });
    }


}
