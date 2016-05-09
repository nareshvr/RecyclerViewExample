package ducere.lechal.pod;

import android.content.Context;
import android.location.LocationManager;
import android.os.Build;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionMenu;
import com.here.android.mpa.common.GeoBoundingBox;
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
import com.here.android.mpa.mapping.MapObject;
import com.here.android.mpa.mapping.MapRoute;
import com.here.android.mpa.mapping.MapTransitLayer;
import com.here.android.mpa.routing.RouteManager;
import com.here.android.mpa.routing.RouteOptions;
import com.here.android.mpa.routing.RoutePlan;
import com.here.android.mpa.routing.RouteResult;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.List;

import ca.barrenechea.widget.recyclerview.decoration.DividerDecoration;
import ducere.lechal.pod.adapters.DirectionsAdapter;
import ducere.lechal.pod.adapters.TagsAdapter;
import ducere.lechal.pod.beans.Navigate;
import ducere.lechal.pod.beans.Place;
import ducere.lechal.pod.constants.SharedPrefUtil;
import ducere.lechal.pod.sqlite.PlaceUtility;

public class NavigationActivity extends AppCompatActivity implements View.OnClickListener {

    TextView tvSearchLocationHead,tvSearchLocation,tvSearchAddress,tvSearchDistance;
    public boolean isMapEngineInitialize = false;
    // map embedded in the map fragment
    public static Map map = null;
    // map fragment embedded in this activity
    private MapFragment mapFragment = null;
    PositioningManager positioningManager;
    public static MapContainer mapcontainer = null;
    ImageView ivBack,ivBackNav;
    Place place;
    CoordinatorLayout coordinatorLayout;
    View bottomSheet;
     BottomSheetBehavior behavior;
    RelativeLayout rlResultLocation,rlMain;
    AppBarLayout appBarLayout;
    CardView cardNavigate,cardStart;
    TextView tvFrom,tvTo;
    public static List<MapObject> markers = null;
    public static MapMarker mapmarker = null;
    public static MapRoute mapRoute = null;
    public static MapRoute[] routes;
    List<RouteResult> routeResults;
    GeoCoordinate geoStart;
    int routeNumber=0;
    private RecyclerView recyclerView;
    FloatingActionMenu fam;
    Navigate navigate;
    TextView dot1,dot2,dot3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        if (Build.VERSION.SDK_INT >= 21) {
            //getWindow().setNavigationBarColor(getResources().getColor(R.color.colorPrimary));
            getWindow().setStatusBarColor(getResources().getColor(R.color.black));

        }

        place = (Place)getIntent().getSerializableExtra("place");
        initMapEngine();

        tvSearchLocationHead = (TextView)findViewById(R.id.tvSearchLocationHead);
        tvSearchLocation = (TextView)findViewById(R.id.tvSearchLocation);
        tvSearchAddress = (TextView)findViewById(R.id.tvSearchAdress);
        tvSearchDistance = (TextView)findViewById(R.id.tvSearchDistance);
        ivBack = (ImageView)findViewById(R.id.ivBack);
        ivBackNav = (ImageView)findViewById(R.id.ivBackNav);
        rlResultLocation = (RelativeLayout)findViewById(R.id.rlResultLocation);
        rlMain = (RelativeLayout)findViewById(R.id.rlMain);
        appBarLayout = (AppBarLayout)findViewById(R.id.appBarLayout);
        cardNavigate = (CardView)findViewById(R.id.llNavigate);
        cardStart = (CardView)findViewById(R.id.llStart);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.mainContent);
        tvFrom = (TextView)findViewById(R.id.tvFrom);
        tvTo = (TextView)findViewById(R.id.tvTo);
        fam = (FloatingActionMenu)findViewById(R.id.famMode);
        dot1 = (TextView)findViewById(R.id.one);
        dot2 = (TextView)findViewById(R.id.two);
        dot3 = (TextView)findViewById(R.id.three);
        com.github.clans.fab.FloatingActionButton fabWalk = (com.github.clans.fab.FloatingActionButton)findViewById(R.id.fabWalk);
        com.github.clans.fab.FloatingActionButton fabCar = (com.github.clans.fab.FloatingActionButton)findViewById(R.id.fabDrive);
        com.github.clans.fab.FloatingActionButton fabBus = (com.github.clans.fab.FloatingActionButton)findViewById(R.id.fabBus);

        if (!fam.isOpened())
            fam.getMenuIconView().setImageResource(R.drawable.ic_car_white);

        recyclerView = (RecyclerView)findViewById(R.id.recycler);
        // The View with the BottomSheetBehavior
        bottomSheet = coordinatorLayout.findViewById(R.id.bottom_sheet);
        behavior = BottomSheetBehavior.from(bottomSheet);
        bottomSheet.setVisibility(View.GONE);


        tvSearchLocationHead.setText(place.getTitle());
        tvSearchLocation.setText(place.getTitle());
        tvSearchAddress.setText(place.getVicinity().replace("<br/>", ", ") + "");
        tvSearchDistance.setText(place.getDistance() / 1000.0 + "km");
        ivBack.setOnClickListener(this);
        ivBackNav.setOnClickListener(this);



        behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            boolean first = true;

            @Override
            public void onStateChanged(View bottomSheet, int newState) {
                // React to state change
                Log.e("onStateChanged", "onStateChanged:" + newState);
                if (newState == 3) {
                    CoordinatorLayout.LayoutParams p = (CoordinatorLayout.LayoutParams) cardStart.getLayoutParams();
                    p.setAnchorId(R.id.viewLine);
                    cardStart.setLayoutParams(p);
                    CoordinatorLayout.LayoutParams pp = (CoordinatorLayout.LayoutParams) fam.getLayoutParams();
                    pp.setAnchorId(R.id.viewLine);
                    fam.setLayoutParams(pp);
                    fam.setVisibility(View.GONE);
                } else if (newState == 4) {
                    CoordinatorLayout.LayoutParams p = (CoordinatorLayout.LayoutParams) cardStart.getLayoutParams();
                    p.setAnchorId(R.id.bottom_sheet);
                    cardStart.setLayoutParams(p);
                    CoordinatorLayout.LayoutParams pp = (CoordinatorLayout.LayoutParams) fam.getLayoutParams();
                    pp.setAnchorId(R.id.bottom_sheet);
                    fam.setLayoutParams(pp);
                    fam.setVisibility(View.VISIBLE);
                }


            }

            @Override
            public void onSlide(View bottomSheet, float slideOffset) {
                // React to dragging events
                Log.e("onSlide", "onSlide");

            }

        });
        behavior.setState(BottomSheetBehavior.STATE_SETTLING);



        cardNavigate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rlResultLocation.setVisibility(View.GONE);
                cardNavigate.setVisibility(View.GONE);

                appBarLayout.setVisibility(View.VISIBLE);
                bottomSheet.setVisibility(View.VISIBLE);
                cardStart.setVisibility(View.VISIBLE);
                fam.setVisibility(View.VISIBLE);
                tvFrom.setText("Current Location");
                tvTo.setText(place.getTitle());


              calculateRoute(RouteOptions.TransportMode.CAR);
                saveInHistory(place);


            }
        });
        cardStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });
        fabBus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fam.toggle(true);
                clearMap();
                calculateRoute(RouteOptions.TransportMode.PUBLIC_TRANSPORT);

            }
        });
        fabWalk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fam.toggle(true);
                clearMap();
                calculateRoute(RouteOptions.TransportMode.PEDESTRIAN);
            }
        });
        fabCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fam.toggle(true);
                clearMap();
                calculateRoute(RouteOptions.TransportMode.CAR);
            }
        });
        fam.setOnMenuToggleListener(new FloatingActionMenu.OnMenuToggleListener() {
            @Override
            public void onMenuToggle(boolean opened) {
                if(!opened){
                    switch (navigate.getMode()){
                        case 0:
                            fam.getMenuIconView().setImageResource(R.drawable.ic_walk_white);
                            break;
                        case 1:
                            fam.getMenuIconView().setImageResource(R.drawable.ic_car_white);
                            break;
                        case 2:
                            fam.getMenuIconView().setImageResource(R.drawable.ic_bus_white);
                            break;
                    }

                    fam.setMenuButtonColorNormal(0xff000000);
                    rlMain.setBackgroundColor(0x00000000);

                }else {
                    fam.getMenuIconView().setImageResource(R.drawable.fab_add);
                    fam.setMenuButtonColorNormal(0xff999999);
                    rlMain.setBackgroundColor(0xAA000000);
                }
            }
        });


    }

    private void saveInHistory(Place place) {
        PlaceUtility placeUtility = new PlaceUtility(NavigationActivity.this);
        place.setType(1);
        placeUtility.putTag(place);
    }

    void calculateRoute(RouteOptions.TransportMode mode){
        navigate = new Navigate();
        navigate.setStartLocation(new ducere.lechal.pod.beans.GeoCoordinate(SharedPrefUtil.getDouble(getApplicationContext(), SharedPrefUtil.CURRENT_LAT), SharedPrefUtil.getDouble(getApplicationContext(), SharedPrefUtil.CURRENT_LNG)));
        navigate.setStartTitle(SharedPrefUtil.getString(getApplicationContext(), SharedPrefUtil.CURRENT_LOCATION));
        navigate.setStartAddress(SharedPrefUtil.getString(getApplicationContext(), SharedPrefUtil.CURRENT_VICINITY));
        navigate.setEndLocation(place.getGeo());
        navigate.setStartTitle(place.getTitle());
        navigate.setEndAddress(place.getVicinity());
        switch (mode){
            case PEDESTRIAN:
                navigate.setMode(0);
                break;
            case CAR:
                navigate.setMode(1);
                break;
            case PUBLIC_TRANSPORT:
                navigate.setMode(2);
                break;
        }


        RouteManager routeManager = RouteManager.getInstance();

        RoutePlan routePlan = new RoutePlan();
        RouteOptions routeOptions = new RouteOptions();

        routePlan.addWaypoint(new com.here.android.mpa.common.GeoCoordinate(navigate.getStartLocation().getLatitude(), navigate.getStartLocation().getLongitude()));
        routePlan.addWaypoint(new com.here.android.mpa.common.GeoCoordinate(navigate.getEndLocation().getLatitude(), navigate.getEndLocation().getLongitude()));

        routeOptions.setRouteCount(3);
        routeOptions.setTransportMode(mode);
        routePlan.setRouteOptions(routeOptions);
        RouteManager.Error error =
                routeManager.calculateRoute(routePlan, routeManagerListener);
    }
    private RouteManager.Listener routeManagerListener =
            new RouteManager.Listener() {
                @Override
                public void onProgress(int i) {
                    Log.d("progress",i+"");

                }

                public void onCalculateRouteFinished(RouteManager.Error errorCode,
                                                     final List<RouteResult> result) {
                    if (errorCode == RouteManager.Error.NONE ) {
                        routeResults = result;
                        drawSelectedRoute(0);
                        Log.d("routeResultSize", result.size() + "");
                        showDots(result.size());

                        ViewPager pager = (ViewPager) findViewById(R.id.pager);
                        pager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
                        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                            @Override
                            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                            }

                            @Override
                            public void onPageSelected(int position) {
                                drawSelectedRoute(position);
                                setDirections(position);
                                dotSelected(position);
                            }

                            @Override
                            public void onPageScrollStateChanged(int state) {

                            }
                        });
                    }else{
                        Log.d("routeResult",errorCode.name().toString());
                    }
                }
            };

    private void showDots(int size) {
        switch (size){
            case 1:
                dot1.setVisibility(View.GONE);
                dot2.setVisibility(View.GONE);
                dot3.setVisibility(View.GONE);
                break;
            case 2:
                dot1.setVisibility(View.VISIBLE);
                dot2.setVisibility(View.VISIBLE);
                dot3.setVisibility(View.GONE);
                break;
            case 3:
                dot1.setVisibility(View.VISIBLE);
                dot2.setVisibility(View.VISIBLE);
                dot3.setVisibility(View.VISIBLE);
                break;
        }
    }
    private void dotSelected(int pos) {
        switch (pos){
            case 0:
                dot1.setTextColor(0xff26A7DF);
                dot2.setTextColor(0xff6D6D6D);
                dot3.setTextColor(0xff6D6D6D);
                break;
            case 1:
                dot2.setTextColor(0xff26A7DF);
                dot1.setTextColor(0xff6D6D6D);
                dot3.setTextColor(0xff6D6D6D);
                break;
            case 2:
                dot3.setTextColor(0xff26A7DF);
                dot2.setTextColor(0xff6D6D6D);
                dot1.setTextColor(0xff6D6D6D);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if(appBarLayout.getVisibility()==View.VISIBLE){
            clearMap();
            rlResultLocation.setVisibility(View.VISIBLE);
            cardNavigate.setVisibility(View.VISIBLE);

            appBarLayout.setVisibility(View.GONE);
            bottomSheet.setVisibility(View.GONE);
            cardStart.setVisibility(View.GONE);
            fam.setVisibility(View.GONE);
        }else{
            super.onBackPressed();
        }

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
                    GeoCoordinate geo = new GeoCoordinate(place.getGeo().getLatitude(),place.getGeo().getLongitude());
                    map.setCenter(geo,
                                    Map.Animation.LINEAR);

                    // Display position indicator
                    map.getPositionIndicator().setVisible(true);

                    mapcontainer = new MapContainer();


                    Image myImage = new com.here.android.mpa.common.Image();
                    try {
                        myImage.setImageResource(R.drawable.ic_flag_blue);
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
                    geoStart = geoPosition.getCoordinate();
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
            case R.id.ivBackNav:
                if(appBarLayout.getVisibility()==View.VISIBLE){
                    clearMap();
                    rlResultLocation.setVisibility(View.VISIBLE);
                    cardNavigate.setVisibility(View.VISIBLE);

                    appBarLayout.setVisibility(View.GONE);
                    bottomSheet.setVisibility(View.GONE);
                    cardStart.setVisibility(View.GONE);
                    fam.setVisibility(View.GONE);
                }
                break;
        }
    }
    private class MyPagerAdapter extends FragmentPagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int pos) {
            drawSelectedRoute(pos);
            setDirections(pos);
            return RouteHeaderFragment.newInstance(routeResults.get(pos));

        }

        @Override
        public int getCount() {
            return routeResults.size();
        }
    }

   void setDirections(int pos){
       final DividerDecoration divider = new DividerDecoration.Builder(this)

               .build();

       DirectionsAdapter mAdapter = new DirectionsAdapter(routeResults.get(pos).getRoute());
       RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
       recyclerView.setLayoutManager(mLayoutManager);
       recyclerView.setItemAnimator(new DefaultItemAnimator());
      // recyclerView.addItemDecoration(divider);
       recyclerView.setAdapter(mAdapter);
    }
    void clearMap(){
        if (map != null && mapRoute != null) {
            map.removeMapObject(mapRoute);
            mapRoute = null;
        }
        if (routes != null) {
            for (int i = 0; i < routes.length; i++) {
                map.removeMapObject(routes[i]);
            }
        }
        if (mapcontainer != null)
            mapcontainer.removeAllMapObjects();
        if (mapmarker != null && map != null)
            map.removeMapObject(mapmarker);
        if (markers != null && map != null)
            map.removeMapObjects(markers);
    }
    void drawSelectedRoute(int routeNumber){
        clearMap();

        routes = new MapRoute[routeResults.size()];
        for (int i = 0; i < routeResults.size(); i++) {
            if (i == routeNumber) {
                routes[i] = new MapRoute(routeResults.get(i).getRoute());
                routes[i].setColor(0xFF26A7DF);
                // map.addMapObject(routes[i]);
                mapRoute = routes[i];
            } else {
                routes[i] = new MapRoute(routeResults.get(i).getRoute());
                routes[i].setColor(0xFF999999);
                map.addMapObject(routes[i]);
            }

        }
        if (map != null) {
            map.addMapObject(mapRoute);

        }


        GeoBoundingBox gbb = routeResults.get(routeNumber).getRoute().getBoundingBox();

        map.zoomTo(gbb, Map.Animation.NONE,
                Map.MOVE_PRESERVE_ORIENTATION);
    }

}
