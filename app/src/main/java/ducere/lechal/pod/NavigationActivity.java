package ducere.lechal.pod;

import android.content.Context;
import android.location.LocationManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.here.android.mpa.common.GeoCoordinate;
import com.here.android.mpa.common.GeoPosition;
import com.here.android.mpa.common.Image;
import com.here.android.mpa.common.MapEngine;
import com.here.android.mpa.common.OnEngineInitListener;
import com.here.android.mpa.common.PositioningManager;
import com.here.android.mpa.mapping.Map;
import com.here.android.mpa.mapping.MapContainer;
import com.here.android.mpa.mapping.MapFragment;
import com.here.android.mpa.mapping.MapMarker;
import com.here.android.mpa.mapping.MapTransitLayer;

import java.io.IOException;
import java.lang.ref.WeakReference;

import ducere.lechal.pod.beans.Place;
import ducere.lechal.pod.constants.SharedPrefUtil;

public class NavigationActivity extends AppCompatActivity implements View.OnClickListener {

    TextView tvSearchLocationHead,tvSearchLocation,tvSearchAddress,tvSearchDistance;
    public boolean isMapEngineInitialize = false;
    // map embedded in the map fragment
    public static Map map = null;
    // map fragment embedded in this activity
    private MapFragment mapFragment = null;
    PositioningManager positioningManager;
    public static MapContainer mapcontainer = null;
    public static MapMarker mapmarker = null;
    ImageView ivBack;
    Place place;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        tvSearchLocationHead = (TextView)findViewById(R.id.tvSearchLocationHead);
        tvSearchLocation = (TextView)findViewById(R.id.tvSearchLocation);
        tvSearchAddress = (TextView)findViewById(R.id.tvSearchAdress);
        tvSearchDistance = (TextView)findViewById(R.id.tvSearchDistance);
        ivBack = (ImageView)findViewById(R.id.ivBack);
        initMapEngine();
        place = (Place)getIntent().getSerializableExtra("place");
        tvSearchLocationHead.setText(place.getTitle());
        tvSearchLocation.setText(place.getTitle());
        tvSearchAddress.setText(place.getVicinity().replace("<br/>",", ")+"");
        tvSearchDistance.setText(place.getDistance()/1000.0 +"km");
        ivBack.setOnClickListener(this);

    }
    public void initMapEngine() {
        final MapEngine mapEngine = MapEngine.getInstance(getApplicationContext());
        mapEngine.init(this, new OnEngineInitListener() {
            @Override
            public void onEngineInitializationCompleted(Error error) {
                // TODO Auto-generated method stub
                if (error == OnEngineInitListener.Error.NONE) {
                    isMapEngineInitialize = true;
                    initMap();
                    //MapEngine.setOnline(false);
                } else {
                    // handle factory initialization failure

                    Toast.makeText(NavigationActivity.this, "cannot initialize map engine : " + error.toString() + "", Toast.LENGTH_SHORT).show();
                    isMapEngineInitialize = false;
                }
            }
        });
    }

    public void initMap() {
        // SearchActivity for the map fragment to finish setup by calling init().
        mapFragment = (com.here.android.mpa.mapping.MapFragment) (getFragmentManager().findFragmentById(
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


                    positioningManager = PositioningManager.getInstance();
                    positioningManager.addListener(
                            new WeakReference<PositioningManager.OnPositionChangedListener>(positionListener));
                    positioningManager.start(PositioningManager.LocationMethod.GPS_NETWORK);
                    LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                    GeoCoordinate geo = new GeoCoordinate(place.getLat(),place.getLng());
                    map.setCenter(geo,
                                    Map.Animation.LINEAR);

                    // Display position indicator
                    map.getPositionIndicator().setVisible(true);

                    mapcontainer = new MapContainer();


                    Image myImage = new com.here.android.mpa.common.Image();
                    try {
                        myImage.setImageResource(R.drawable.ic_marker_location_lechal);
                    } catch (IOException e) {
                        Log.d("Exception", e.toString() + "");
                    }

                    mapmarker = new MapMarker(geo, myImage);


                    //mapmarker.setTitle(poiTitle.get(i)+","+poiAddress.get(i));
                    // mapmarker.setDescription(poiDistance.get(i) + "");
                    // mapmarker.showInfoBubble();
                    mapcontainer.addMapObject(mapmarker);
                                  /*  ClusterLayer cl = new ClusterLayer();
                                    cl.addMarker(mapmarker);*/
                    map.addMapObject(mapcontainer);
                    //map.addClusterLayer(cl);
                    map.setCenter(geo, Map.Animation.LINEAR);
                    map.setZoomLevel(17);


                } else {
                    //gps.showSettingsAlert();
                    //Log.d("gps", "4");
                    Toast.makeText(NavigationActivity.this, "Cannot initialize map fragment : " +
                            error.toString(), Toast.LENGTH_SHORT).show();
                    isMapEngineInitialize = false;
                    initMapEngine();
                }
            }

        });
    }
    // Define positioning listener
    private PositioningManager.OnPositionChangedListener positionListener = new
            PositioningManager.OnPositionChangedListener() {

                @Override
                public void onPositionUpdated(PositioningManager.LocationMethod locationMethod, GeoPosition geoPosition, boolean b) {

                }

                @Override
                public void onPositionFixChanged(PositioningManager.LocationMethod locationMethod, PositioningManager.LocationStatus locationStatus) {

                }
            };

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ivBack:
                finish();
                break;
        }
    }
}
