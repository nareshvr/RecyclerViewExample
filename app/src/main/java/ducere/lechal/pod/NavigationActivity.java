package ducere.lechal.pod;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.media.AudioManager;
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
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionMenu;
import com.here.android.mpa.cluster.ClusterLayer;
import com.here.android.mpa.common.GeoBoundingBox;
import com.here.android.mpa.common.GeoCoordinate;
import com.here.android.mpa.common.GeoPolyline;
import com.here.android.mpa.common.GeoPosition;
import com.here.android.mpa.common.Image;
import com.here.android.mpa.common.MapEngine;
import com.here.android.mpa.common.OnEngineInitListener;
import com.here.android.mpa.common.PositioningManager;
import com.here.android.mpa.guidance.NavigationManager;
import com.here.android.mpa.guidance.VoiceCatalog;
import com.here.android.mpa.guidance.VoicePackage;
import com.here.android.mpa.mapping.Map;
import com.here.android.mpa.mapping.MapContainer;
import com.here.android.mpa.mapping.MapFragment;
import com.here.android.mpa.mapping.MapMarker;
import com.here.android.mpa.mapping.MapObject;
import com.here.android.mpa.mapping.MapOverlay;
import com.here.android.mpa.mapping.MapRoute;
import com.here.android.mpa.mapping.MapTransitLayer;
import com.here.android.mpa.routing.Maneuver;
import com.here.android.mpa.routing.Route;
import com.here.android.mpa.routing.RouteManager;
import com.here.android.mpa.routing.RouteOptions;
import com.here.android.mpa.routing.RoutePlan;
import com.here.android.mpa.routing.RouteResult;
import com.here.android.mpa.search.AroundRequest;
import com.here.android.mpa.search.CategoryFilter;
import com.here.android.mpa.search.DiscoveryResult;
import com.here.android.mpa.search.DiscoveryResultPage;
import com.here.android.mpa.search.ErrorCode;
import com.here.android.mpa.search.PlaceLink;
import com.here.android.mpa.search.ResultListener;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import ca.barrenechea.widget.recyclerview.decoration.DividerDecoration;
import ducere.lechal.pod.adapters.DirectionsAdapter;
import ducere.lechal.pod.adapters.TagsAdapter;
import ducere.lechal.pod.beans.Navigate;
import ducere.lechal.pod.beans.Place;
import ducere.lechal.pod.constants.Convert;
import ducere.lechal.pod.constants.SharedPrefUtil;
import ducere.lechal.pod.sqlite.PlaceUtility;
import ducere.lechal.pod.utilities.GetWayPoints;
import ducere.lechal.pod.utilities.NavigationFeedback;

public class NavigationActivity extends AppCompatActivity implements View.OnClickListener, RouteHeaderFragment.OnClickRoute {

    TextView tvSearchLocationHead, tvSearchLocation, tvSearchAddress, tvSearchDistance;
    public boolean isMapEngineInitialize = false;
    // map embedded in the map fragment
    public static Map map = null;
    // map fragment embedded in this activity
    private MapFragment mapFragment = null;
    PositioningManager positioningManager;
    public static MapContainer mapcontainer = null;
    ImageView ivBack, ivBackNav;
    Place place;
    CoordinatorLayout coordinatorLayout;
    View bottomSheet;
    BottomSheetBehavior behavior;
    RelativeLayout rlResultLocation, rlMain;
    AppBarLayout appBarLayout;
    CardView cardNavigate, cardStart, cardStop;
    TextView tvFrom, tvTo;
    public static List<MapObject> markers = null;
    public static MapMarker mapmarker = null, mapMarkerStart = null;
    public static MapRoute mapRoute = null;
    public static MapRoute[] routes;
    List<RouteResult> routeResults;
    GeoCoordinate geoStart;
    int routeNumber = 0;
    private RecyclerView recyclerView;
    FloatingActionMenu fam;
    FloatingActionButton fabMode;
    Navigate navigate;
    TextView dot1, dot2, dot3;
    LinearLayout llInNavigation, llNavigationHead, llResultHead, llStop;
    TextView tvInstruction, tvSubInstruction, tvDistance, tvEta, tvDistanceLeft, tvTime;
    ImageView ivTurn, ivCenter, ivNavDots,ivRouteMenu;
    boolean isNavigate = false;
    NavigationManager navigationManager;
    ViewPager pager;
    public static int directionDrawable;
    DirectionsAdapter mAdapter;
    FloatingActionButton fabPause;
    boolean reverse = false;
    RouteOptions routeOptions;
    List<Integer> selected;
    ImageView ivWayPointsCancel;
    LinearLayout llRestaurant;
    RelativeLayout rlWayPoints;
    ClusterLayer clusterLayer;
    LinearLayout llClearWaypoints;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        if (Build.VERSION.SDK_INT >= 21) {
            //getWindow().setNavigationBarColor(getResources().getColor(R.color.colorPrimary));
            getWindow().setStatusBarColor(getResources().getColor(R.color.black));

        }

        if (getIntent().hasExtra("place")) {

            place = (Place) getIntent().getSerializableExtra("place");
        }


        tvSearchLocationHead = (TextView) findViewById(R.id.tvSearchLocationHead);
        tvSearchLocation = (TextView) findViewById(R.id.tvSearchLocation);
        tvSearchAddress = (TextView) findViewById(R.id.tvSearchAdress);
        tvSearchDistance = (TextView) findViewById(R.id.tvSearchDistance);
        ivBack = (ImageView) findViewById(R.id.ivBack);
        ivBackNav = (ImageView) findViewById(R.id.ivBackNav);
        rlResultLocation = (RelativeLayout) findViewById(R.id.rlResultLocation);
        rlMain = (RelativeLayout) findViewById(R.id.rlMain);
        appBarLayout = (AppBarLayout) findViewById(R.id.appBarLayout);
        cardNavigate = (CardView) findViewById(R.id.llNavigate);
        cardStart = (CardView) findViewById(R.id.llStart);
        cardStop = (CardView) findViewById(R.id.llStop);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.mainContent);
        tvFrom = (TextView) findViewById(R.id.tvFrom);
        tvTo = (TextView) findViewById(R.id.tvTo);

        tvInstruction = (TextView) findViewById(R.id.tvInstruction);
        tvSubInstruction = (TextView) findViewById(R.id.tvSubInstruction);
        tvDistance = (TextView) findViewById(R.id.tvDistance);
        tvEta = (TextView) findViewById(R.id.tvEta);
        tvDistanceLeft = (TextView) findViewById(R.id.tvDistanceLeft);
        tvTime = (TextView) findViewById(R.id.tvTime);
        ivTurn = (ImageView) findViewById(R.id.ivTurn);
        ivCenter = (ImageView) findViewById(R.id.ivCenter);
        ivNavDots = (ImageView) findViewById(R.id.ivNavDots);
        ivRouteMenu = (ImageView) findViewById(R.id.routeMenu);


        llInNavigation = (LinearLayout) findViewById(R.id.llInNavigation);
        llNavigationHead = (LinearLayout) findViewById(R.id.llNavigationHead);
        llResultHead = (LinearLayout) findViewById(R.id.llResultHead);
        llStop = (LinearLayout) findViewById(R.id.llStopMode);
        fabMode = (FloatingActionButton) findViewById(R.id.fabMode);
        fabPause = (FloatingActionButton)findViewById(R.id.fabPause);

        ivWayPointsCancel = (ImageView)findViewById(R.id.ivCancel);
        llRestaurant = (LinearLayout)findViewById(R.id.llRestaurant);
        llClearWaypoints = (LinearLayout)findViewById(R.id.llClearWaypoints);
        rlWayPoints = (RelativeLayout)findViewById(R.id.rlWaypoints);

        navigate = new Navigate();
        fam = (FloatingActionMenu) findViewById(R.id.famMode);
        dot1 = (TextView) findViewById(R.id.one);
        dot2 = (TextView) findViewById(R.id.two);
        dot3 = (TextView) findViewById(R.id.three);
        final com.github.clans.fab.FloatingActionButton fabWalk = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.fabWalk);
        final com.github.clans.fab.FloatingActionButton fabCar = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.fabDrive);
        final com.github.clans.fab.FloatingActionButton fabBus = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.fabBus);


        if (!fam.isOpened())
            fam.getMenuIconView().setImageResource(R.drawable.ic_car_white);

        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        // The View with the BottomSheetBehavior
        bottomSheet = coordinatorLayout.findViewById(R.id.bottom_sheet);
        behavior = BottomSheetBehavior.from(bottomSheet);
        bottomSheet.setVisibility(View.GONE);


        tvSearchLocationHead.setText(place.getTitle());
        tvSearchLocation.setText(place.getTitle());
        tvSearchAddress.setText(place.getVicinity().replace("<br/>", ", ") + "");
        tvSearchDistance.setText(Convert.metersToKms(place.getDistance()));
        ivBack.setOnClickListener(this);
        ivBackNav.setOnClickListener(this);

        initMapEngine();

        behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            boolean first = true;

            @Override
            public void onStateChanged(View bottomSheet, int newState) {
                // React to state change
                Log.e("onStateChanged", "onStateChanged:" + newState);
                if (newState == 1 || newState == 3) {
                    CoordinatorLayout.LayoutParams p = (CoordinatorLayout.LayoutParams) cardStart.getLayoutParams();
                    p.setAnchorId(R.id.viewLine);
                    cardStart.setLayoutParams(p);

                    CoordinatorLayout.LayoutParams pStop = (CoordinatorLayout.LayoutParams) llStop.getLayoutParams();
                    pStop.setAnchorId(R.id.viewLine);
                    llStop.setLayoutParams(pStop);

                    fam.setVisibility(View.GONE);


                } else if (newState == 4) {
                    CoordinatorLayout.LayoutParams p = (CoordinatorLayout.LayoutParams) cardStart.getLayoutParams();
                    p.setAnchorId(R.id.bottom_sheet);
                    cardStart.setLayoutParams(p);

                    CoordinatorLayout.LayoutParams pStop = (CoordinatorLayout.LayoutParams) llStop.getLayoutParams();
                    pStop.setAnchorId(R.id.bottom_sheet);
                    llStop.setLayoutParams(pStop);

                    if (!LechalApplication.getInstance().isNavigating()) {

                        fam.setVisibility(View.VISIBLE);
                    }

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

                fam.setVisibility(View.VISIBLE);


                calculateRoute(RouteOptions.TransportMode.CAR);
                saveInHistory(place);


            }
        });
        cardStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isNavigate = true;
                pager.setVisibility(View.INVISIBLE);
                showDots(1);
                cardStart.setVisibility(View.GONE);
                llStop.setVisibility(View.VISIBLE);
                llInNavigation.setVisibility(View.VISIBLE);
                llResultHead.setVisibility(View.GONE);
                llNavigationHead.setVisibility(View.VISIBLE);
                fam.setVisibility(View.GONE);
                clearMap();
                showModeFab();
                LechalApplication.getInstance().setRoute(routeResults.get(pager.getCurrentItem()).getRoute());
                map.addMapObject(new MapRoute(routeResults.get(pager.getCurrentItem()).getRoute()).setColor(0xFF26A7DF));

                navigationManager = NavigationManager.getInstance();
                LechalApplication.getInstance().setNavigationManager(navigationManager);
                LechalApplication.getInstance().setNavigating(true);
                navigationManager.setNaturalGuidanceMode(EnumSet.of(NavigationManager.NaturalGuidanceMode.JUNCTION, NavigationManager.NaturalGuidanceMode.STOP_SIGN));
                enableHereAudio(false);
                navigationManager.setMapUpdateMode(NavigationManager.MapUpdateMode.ROADVIEW);
                navigationManager.setMap(map);
                navigationManager.removeNewInstructionEventListener(newInstructionEventListener);
                navigationManager.removeRerouteListener(rerouteListener);
                navigationManager.addRerouteListener(new WeakReference<NavigationManager.RerouteListener>(rerouteListener));
                navigationManager.removeNavigationManagerEventListener(navigationManagerEventListener);
                navigationManager.addNavigationManagerEventListener(new WeakReference<NavigationManager.NavigationManagerEventListener>(navigationManagerEventListener));
                navigationManager.addNewInstructionEventListener(new WeakReference<NavigationManager.NewInstructionEventListener>(newInstructionEventListener));
                NavigationManager.Error errorVoice = navigationManager.setVoiceSkin(VoiceCatalog.getInstance().getLocalVoiceSkin(SharedPrefUtil.getVoiceId(NavigationActivity.this, SharedPrefUtil.VOICE_PREFERENCE)));

                NavigationManager.Error error = navigationManager.simulate(routeResults.get(pager.getCurrentItem()).getRoute(), 20);
                // navigationManager.setMapUpdateMode(NavigationManager.MapUpdateMode.ROADVIEW);
            }
        });
        cardStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCloseDialog();
            }
        });
        llInNavigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleBottomSheet();

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
                if (!opened) {
                    switch (navigate.getMode()) {
                        case 0:
                            fam.getMenuIconView().setImageResource(R.drawable.ic_walk_white);
                            fabWalk.setImageResource(R.drawable.ic_walk_circle_blue);
                            break;
                        case 1:
                            fam.getMenuIconView().setImageResource(R.drawable.ic_car_white);
                            fabCar.setImageResource(R.drawable.ic_car_circle_blue);
                            break;
                        case 2:
                            fam.getMenuIconView().setImageResource(R.drawable.ic_bus_white);
                            fabBus.setImageResource(R.drawable.ic_bus_circle_blue);
                            break;
                    }
                    fam.setMenuButtonColorNormal(0xff000000);
                    rlMain.setBackgroundColor(0x00000000);
                } else {
                    fam.getMenuIconView().setImageResource(R.drawable.fab_add);
                    fam.setMenuButtonColorNormal(0xff999999);
                    rlMain.setBackgroundColor(0xAA000000);
                    fabWalk.setImageResource(R.drawable.ic_walk_white);
                    fabCar.setImageResource(R.drawable.ic_car_circle);
                    fabBus.setImageResource(R.drawable.ic_bus_white);
                    switch (navigate.getMode()) {
                        case 0:
                            fabWalk.setImageResource(R.drawable.ic_walk_circle_blue);
                            break;
                        case 1:
                            fabCar.setImageResource(R.drawable.ic_car_circle_blue);
                            break;
                        case 2:
                            fabBus.setImageResource(R.drawable.ic_bus_circle_blue);
                            break;
                    }
                }
            }
        });
        fabPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (navigationManager.getRunningState()== NavigationManager.NavigationState.RUNNING){
                    navigationManager.pause();
                    fabPause.setImageResource(R.drawable.icon_play);
                }else  if (navigationManager.getRunningState()== NavigationManager.NavigationState.PAUSED){
                    navigationManager.resume();
                    fabPause.setImageResource(R.mipmap.icon_pause);
                }
            }
        });
        ivCenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        ivWayPointsCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rlWayPoints.setVisibility(View.GONE);
            }
        });
        llRestaurant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rlWayPoints.setVisibility(View.GONE);
                llClearWaypoints.setVisibility(View.VISIBLE);
                showWayPoints("eat-drink");
            }
        });
        llClearWaypoints.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llClearWaypoints.setVisibility(View.GONE);
                if (clusterLayer!=null){

                    map.removeClusterLayer(clusterLayer);
                }
            }
        });
        final PopupMenu popup = new PopupMenu(NavigationActivity.this, ivNavDots);
        MenuInflater inflaterMenu = popup.getMenuInflater();
        inflaterMenu.inflate(R.menu.navigation_menu, popup.getMenu());
        ivNavDots.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popup.show();
            }
        });
        popup.getMenu().findItem(R.id.action_traffic).setChecked(true);
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_way_point:
                        rlWayPoints.setVisibility(View.VISIBLE);
                        return true;
                    case R.id.action_traffic:
                        if (item.isChecked()) {
                            item.setChecked(false);

                        } else {
                            item.setChecked(true);
                        }
                        map.setMapScheme(Map.Scheme.HYBRID_TRAFFIC_DAY);
                        return true;
                    case R.id.action_public_transit:
                        if (item.isChecked()) {
                            item.setChecked(false);

                        } else {
                            item.setChecked(true);
                        }
                        map.setMapScheme(Map.Scheme.NORMAL_DAY);
                        map.setMapScheme(Map.Scheme.HYBRID_DAY_TRANSIT);
                        return true;
                    case R.id.action_satellite:
                        if (item.isChecked()) {
                            item.setChecked(false);

                        } else {
                            item.setChecked(true);
                        }
                        map.setMapScheme(Map.Scheme.SATELLITE_DAY);
                        return true;
                    case R.id.action_normal:
                        if (item.isChecked()) {
                            item.setChecked(false);

                        } else {
                            item.setChecked(true);
                        }
                        map.setMapScheme(Map.Scheme.NORMAL_DAY);
                        return true;
                    case R.id.action_terrain:
                        if (item.isChecked()) {
                            item.setChecked(false);

                        } else {
                            item.setChecked(true);
                        }
                        map.setMapScheme(Map.Scheme.TERRAIN_DAY);
                        return true;
                    case R.id.action_voice:
                        if (item.isChecked()) {
                            enableHereAudio(false);
                            item.setChecked(false);
                        }else{
                            enableHereAudio(true);
                            item.setChecked(true);
                        }

                        return true;
                    case R.id.action_share_directions:

                        return true;

                    default:
                        return true;
                }
            }
        });

        final PopupMenu popupRoute = new PopupMenu(NavigationActivity.this, ivRouteMenu);
        MenuInflater inflaterMenuRoute = popupRoute.getMenuInflater();
        inflaterMenuRoute.inflate(R.menu.routes_menu, popupRoute.getMenu());
        ivRouteMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupRoute.show();
            }
        });
        popupRoute.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_group_journey:
                        startActivity(new Intent(NavigationActivity.this,AddFriendsToJourenyActivity.class));
                        return true;
                    case R.id.action_reverse:

                        if (reverse){
                            reverse=false;
                            calculateRoute(RouteOptions.TransportMode.CAR);
                        }
                        else {
                            reverse = true;
                            calculateRoute(RouteOptions.TransportMode.CAR);
                        }

                        return true;

                    case R.id.action_satellite:
                        if (item.isChecked()) {
                            item.setChecked(false);

                        } else {
                            item.setChecked(true);
                        }
                        map.setMapScheme(Map.Scheme.SATELLITE_DAY);
                        return true;
                    case R.id.action_normal:
                        if (item.isChecked()) {
                            item.setChecked(false);

                        } else {
                            item.setChecked(true);
                        }
                        map.setMapScheme(Map.Scheme.NORMAL_DAY);
                        return true;
                    case R.id.action_terrain:
                        if (item.isChecked()) {
                            item.setChecked(false);

                        } else {
                            item.setChecked(true);
                        }
                        map.setMapScheme(Map.Scheme.TERRAIN_DAY);
                        return true;

                    case R.id.action_route_options:
                            showRouteOptions();
                        return true;

                    default:
                        return true;
                }
            }
        });

    }

    private void showWayPoints(String poi) {
        if (clusterLayer!=null){
            map.removeClusterLayer(clusterLayer);
        }
      //  GeoCoordinate  geo = new GeoCoordinate(SharedPrefUtil.getDouble(NavigationActivity.this, SharedPrefUtil.CURRENT_LAT), SharedPrefUtil.getDouble(NavigationActivity.this, SharedPrefUtil.CURRENT_LNG));


        AroundRequest request = new AroundRequest().setCategoryFilter(new CategoryFilter().add(poi)).setSearchArea(geoStart, routeResults.get(routeNumber).getRoute().getLength());

        request.setCollectionSize(99);
        ErrorCode error = request.execute(new SearchRequestListener());
    }

    void showModeFab() {
        switch (navigate.getMode()) {
            case 0:
                fabMode.setImageResource(R.drawable.ic_walk_white);
                break;
            case 1:
                fabMode.setImageResource(R.drawable.ic_car_white);
                break;
            case 2:
                fabMode.setImageResource(R.drawable.ic_bus_white);
                break;
        }
    }

    private void saveInHistory(Place place) {
        PlaceUtility placeUtility = new PlaceUtility(NavigationActivity.this);
        place.setType(1);
        placeUtility.updateTagWillDeleteAndInsert(place);
    }

    void calculateRoute(RouteOptions.TransportMode mode) {

        if (reverse){
            tvTo.setText("Current Location");
            tvFrom.setText(place.getTitle());
            navigate.setEndLocation(new ducere.lechal.pod.beans.GeoCoordinate(SharedPrefUtil.getDouble(getApplicationContext(), SharedPrefUtil.CURRENT_LAT), SharedPrefUtil.getDouble(getApplicationContext(), SharedPrefUtil.CURRENT_LNG)));
            navigate.setEndTitle(SharedPrefUtil.getString(getApplicationContext(), SharedPrefUtil.CURRENT_LOCATION));
            navigate.setEndAddress(SharedPrefUtil.getString(getApplicationContext(), SharedPrefUtil.CURRENT_VICINITY));
            navigate.setStartLocation(place.getGeo());
            navigate.setStartTitle(place.getTitle());
            navigate.setStartAddress(place.getVicinity());

        }else {
            tvFrom.setText("Current Location");
            tvTo.setText(place.getTitle());
            navigate.setStartLocation(new ducere.lechal.pod.beans.GeoCoordinate(SharedPrefUtil.getDouble(getApplicationContext(), SharedPrefUtil.CURRENT_LAT), SharedPrefUtil.getDouble(getApplicationContext(), SharedPrefUtil.CURRENT_LNG)));
            navigate.setStartTitle(SharedPrefUtil.getString(getApplicationContext(), SharedPrefUtil.CURRENT_LOCATION));
            navigate.setStartAddress(SharedPrefUtil.getString(getApplicationContext(), SharedPrefUtil.CURRENT_VICINITY));
            navigate.setEndLocation(place.getGeo());
            navigate.setEndTitle(place.getTitle());
            navigate.setEndAddress(place.getVicinity());

        }


        switch (mode) {
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


        LechalApplication.getInstance().setNavigate(navigate);
        LechalApplication.getInstance().setPlace(place);
        RouteManager routeManager = RouteManager.getInstance();

        RoutePlan routePlan = new RoutePlan();
       routeOptions = new RouteOptions();
        setRouteOptions();
        GeoCoordinate geo;

        if (SharedPrefUtil.getBoolean(NavigationActivity.this,SharedPrefUtil.IS_MOCK_ENABLE)){
            geo = new GeoCoordinate(SharedPrefUtil.getDouble(NavigationActivity.this, SharedPrefUtil.MOCK_LAT), SharedPrefUtil.getDouble(NavigationActivity.this, SharedPrefUtil.MOCK_LNG));
            cardStart.setVisibility(View.GONE);
            tvFrom.setText(SharedPrefUtil.getString(NavigationActivity.this,SharedPrefUtil.MOCK_LOCATION));
        }else{
            geo =new com.here.android.mpa.common.GeoCoordinate(navigate.getStartLocation().getLatitude(), navigate.getStartLocation().getLongitude());
           if (reverse)
                cardStart.setVisibility(View.GONE);
            else
               cardStart.setVisibility(View.VISIBLE);
        }
        routePlan.addWaypoint(geo);
        routePlan.addWaypoint(new com.here.android.mpa.common.GeoCoordinate(navigate.getEndLocation().getLatitude(), navigate.getEndLocation().getLongitude()));

        routeOptions.setRouteCount(3);
        setRouteOptions();
        routeOptions.setTransportMode(mode);
        routePlan.setRouteOptions(routeOptions);
        RouteManager.Error error =
                routeManager.calculateRoute(routePlan, routeManagerListener);
    }

    private RouteManager.Listener routeManagerListener =
            new RouteManager.Listener() {
                @Override
                public void onProgress(int i) {
                    Log.d("progress", i + "");

                }

                public void onCalculateRouteFinished(RouteManager.Error errorCode,
                                                     final List<RouteResult> result) {
                    if (errorCode == RouteManager.Error.NONE) {
                        routeResults = result;
                        drawSelectedRoute(0);
                        Log.d("routeResultSize", result.size() + "");
                        showDots(result.size());

                        pager = (ViewPager) findViewById(R.id.pager);
                        pager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));

                        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                            @Override
                            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                            }

                            @Override
                            public void onPageSelected(int position) {
                                drawSelectedRoute(position);
                                setDirections(routeResults.get(position).getRoute());
                                dotSelected(position);
                            }

                            @Override
                            public void onPageScrollStateChanged(int state) {

                            }
                        });
                    } else {
                        Log.d("routeResult", errorCode.name().toString());
                    }
                }
            };
    NavigationManager.RerouteListener rerouteListener = new NavigationManager.RerouteListener() {
        @Override
        public void onRerouteBegin() {
            super.onRerouteBegin();
        }

        @Override
        public void onRerouteEnd(Route route) {
            super.onRerouteEnd(route);
            LechalApplication.getInstance().resetTurns();
        }

        @Override
        public void onRerouteFailed() {
            super.onRerouteFailed();
        }
    };

    private void showDots(int size) {
        switch (size) {
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
        switch (pos) {
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
        if (LechalApplication.getInstance().isNavigating()) {
            super.onBackPressed();
        } else {
            if (appBarLayout.getVisibility() == View.VISIBLE) {
                clearMap();
                rlResultLocation.setVisibility(View.VISIBLE);
                cardNavigate.setVisibility(View.VISIBLE);

                appBarLayout.setVisibility(View.INVISIBLE);
                bottomSheet.setVisibility(View.GONE);
                cardStart.setVisibility(View.GONE);
                fam.setVisibility(View.GONE);
            } else {
                super.onBackPressed();
            }
        }


    }

    public void showNavigation() {
        isNavigate = true;
        rlResultLocation.setVisibility(View.GONE);
        cardNavigate.setVisibility(View.GONE);

        appBarLayout.setVisibility(View.VISIBLE);
        bottomSheet.setVisibility(View.VISIBLE);
        cardStart.setVisibility(View.GONE);
        // pager.setVisibility(View.INVISIBLE);
        showDots(1);
        llStop.setVisibility(View.VISIBLE);
        llInNavigation.setVisibility(View.VISIBLE);
        llResultHead.setVisibility(View.GONE);
        llNavigationHead.setVisibility(View.VISIBLE);
        fam.setVisibility(View.GONE);
        navigate = LechalApplication.getInstance().getNavigate();
        showModeFab();
        clearMap();
        setDirections(LechalApplication.getInstance().getRoute());
        navigationManager = LechalApplication.getInstance().getNavigationManager();
        navigationManager.setMapUpdateMode(NavigationManager.MapUpdateMode.ROADVIEW);
        navigationManager.setMap(map);
        navigationManager.setMapUpdateMode(NavigationManager.MapUpdateMode.POSITION);
        navigationManager.removeNewInstructionEventListener(newInstructionEventListener);
        navigationManager.removeRerouteListener(rerouteListener);
        navigationManager.addRerouteListener(new WeakReference<NavigationManager.RerouteListener>(rerouteListener));
        navigationManager.removeNavigationManagerEventListener(navigationManagerEventListener);
        navigationManager.addNavigationManagerEventListener(new WeakReference<NavigationManager.NavigationManagerEventListener>(navigationManagerEventListener));
        navigationManager.addNewInstructionEventListener(new WeakReference<NavigationManager.NewInstructionEventListener>(newInstructionEventListener));
        map.addMapObject(new MapRoute(LechalApplication.getInstance().getRoute()).setColor(0xFF26A7DF));

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
                    clusterLayer = new ClusterLayer();
                    // Set the zoom level to the average between min and max
                    map.setZoomLevel(17);


                    //set the map projection
                    map.setProjectionMode(Map.Projection.GLOBE);


                    positioningManager = PositioningManager.getInstance();
                    positioningManager.addListener(
                            new WeakReference<PositioningManager.OnPositionChangedListener>(positionListener));
                    positioningManager.start(PositioningManager.LocationMethod.GPS_NETWORK);
                    LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                    GeoCoordinate geo = new GeoCoordinate(place.getGeo().getLatitude(), place.getGeo().getLongitude());
                    map.setCenter(geo,
                            Map.Animation.LINEAR);

                    // Display position indicator
                    map.getPositionIndicator().setVisible(true);

                    mapcontainer = new MapContainer();


                    Image myImage = new com.here.android.mpa.common.Image();
                    try {
                        myImage.setImageResource(R.drawable.ic_marker_stop);
                    } catch (IOException e) {
                        Log.d("Exception", e.toString() + "");
                    }

                    mapmarker = new MapMarker(geo, myImage);


                    mapcontainer.addMapObject(mapmarker);

                    map.addMapObject(mapcontainer);
                    //map.addClusterLayer(cl);
                //    map.setCenter(geo, Map.Animation.LINEAR);
                    map.setZoomLevel(17);
                    if (LechalApplication.getInstance().isNavigating()) {
                        showNavigation();
                    }

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
                    if (isNavigate) {
                        updateNavigation();
                        NavigationFeedback feedback = new NavigationFeedback(geoPosition,geoPosition.getCoordinate(), getApplicationContext());
                        feedback.findDistanceRange();


                    }

                }

                @Override
                public void onPositionFixChanged(PositioningManager.LocationMethod locationMethod, PositioningManager.LocationStatus locationStatus) {

                }
            };

    NavigationManager.NewInstructionEventListener newInstructionEventListener = new NavigationManager.NewInstructionEventListener() {
        @Override
        public void onNewInstructionEvent() {
            super.onNewInstructionEvent();
            LechalApplication.getInstance().resetTurns();

        }
    };

    NavigationManager.NavigationManagerEventListener navigationManagerEventListener = new NavigationManager.NavigationManagerEventListener() {
        @Override
        public void onRunningStateChanged() {
            super.onRunningStateChanged();
        }

        @Override
        public void onNavigationModeChanged() {
            super.onNavigationModeChanged();
        }

        @Override
        public void onEnded(NavigationManager.NavigationMode navigationMode) {
            super.onEnded(navigationMode);
            navigationManager.stop();
            LechalApplication.getInstance().setNavigating(false);
            if (!isFinishing())
                showEndDialog();
        }

        @Override
        public void onMapUpdateModeChanged(NavigationManager.MapUpdateMode mapUpdateMode) {
            super.onMapUpdateModeChanged(mapUpdateMode);
        }

        @Override
        public void onRouteUpdated(Route route) {
            super.onRouteUpdated(route);
        }

        @Override
        public void onCountryInfo(String s, String s1) {
            super.onCountryInfo(s, s1);
        }
    };

    private void updateNavigation() {
        com.here.android.mpa.routing.Maneuver currentManeuver = NavigationManager.getInstance().getNextManeuver();
        if (currentManeuver != null) {
            String turnDirections = "", turnAddress = "";
            if (currentManeuver.getNextRoadName() != null && currentManeuver.getNextRoadName().length() > 1) {

                turnDirections = directions(currentManeuver, currentManeuver.getTurn(), 0) + "on to " + currentManeuver.getNextRoadName();
                turnAddress = "on to " + currentManeuver.getNextRoadName();
            } else {
                turnDirections = directions(currentManeuver, currentManeuver.getTurn(), 0);
            }
            String distanceNextString = "", distanceTotalString = "", mEta = "";
            if (NavigationManager.getInstance().getTta(Route.TrafficPenaltyMode.OPTIMAL, true) != null) {
                mEta = Math.round((NavigationManager.getInstance().getTta(Route.TrafficPenaltyMode.OPTIMAL, true).getDuration()) / 60) + " min";

            }

            double distanceNext = NavigationManager.getInstance().getNextManeuverDistance();
            double distanceTotal = navigationManager.getDestinationDistance();
            tvInstruction.setText(turnDirections);
            tvSubInstruction.setText(turnAddress);
            tvDistance.setText(Convert.metersToKms(distanceNext));
            ivTurn.setBackgroundResource(directionDrawable);
            tvEta.setText(mEta);
            tvDistanceLeft.setText(Convert.metersToKms(distanceTotal));
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivBack:
                finish();
                break;
            case R.id.ivBackNav:
                if (appBarLayout.getVisibility() == View.VISIBLE) {
                    clearMap();
                    rlResultLocation.setVisibility(View.VISIBLE);
                    cardNavigate.setVisibility(View.VISIBLE);

                    appBarLayout.setVisibility(View.INVISIBLE);
                    bottomSheet.setVisibility(View.GONE);
                    cardStart.setVisibility(View.GONE);
                    fam.setVisibility(View.GONE);
                }
                break;
        }
    }

    @Override
    public void onClick() {
        toggleBottomSheet();
    }

    void toggleBottomSheet() {
        if (behavior.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
            behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        } else {
            behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }
    }

    private class MyPagerAdapter extends FragmentPagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int pos) {
            drawSelectedRoute(pos);
            setDirections(routeResults.get(pos).getRoute());
            return RouteHeaderFragment.newInstance(routeResults.get(pos));

        }

        @Override
        public int getCount() {
            return routeResults.size();
        }
    }


    void setDirections(Route route) {
        final DividerDecoration divider = new DividerDecoration.Builder(this)

                .build();

        mAdapter = new DirectionsAdapter(route);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        // recyclerView.addItemDecoration(divider);
        recyclerView.setAdapter(mAdapter);
    }

    void clearMap() {
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
        if (mapMarkerStart != null && map != null)
            map.removeMapObject(mapMarkerStart);
        if (markers != null && map != null)
            map.removeMapObjects(markers);
    }

    void drawSelectedRoute(int routeNumber) {
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
        GeoCoordinate geo = new GeoCoordinate(place.getGeo().getLatitude(), place.getGeo().getLongitude());
        GeoCoordinate geoStart = new GeoCoordinate(navigate.getStartLocation().getLatitude(), navigate.getStartLocation().getLongitude());

        Image myImage = new com.here.android.mpa.common.Image();
        try {
            myImage.setImageResource(R.drawable.ic_marker_stop);
        } catch (IOException e) {
            Log.d("Exception", e.toString() + "");
        }
        Image myImageStart = new com.here.android.mpa.common.Image();
        try {
            myImageStart.setImageResource(R.drawable.ic_marker_start);
        } catch (IOException e) {
            Log.d("Exception", e.toString() + "");
        }
        mapmarker = new MapMarker(geo, myImage);
        mapMarkerStart = new MapMarker(geoStart, myImageStart);


        mapcontainer.addMapObject(mapmarker);
        mapcontainer.addMapObject(mapMarkerStart);

        map.addMapObject(mapcontainer);


        GeoBoundingBox gbb = routeResults.get(routeNumber).getRoute().getBoundingBox();

        map.zoomTo(gbb, Map.Animation.NONE,
                Map.MOVE_PRESERVE_ORIENTATION);
    }


    public static String directions(Maneuver maneuver, Maneuver.Turn directions, int color) {
        String turn = "";

        //Log.d("directions",directions.name()+"");
        if (directions == Maneuver.Turn.UNDEFINED || directions == Maneuver.Turn.NO_TURN) {
            turn = "Go Straight up to ";
            if (color == 0) {
                directionDrawable = R.drawable.turn_straight;
                // directionDrawableLock = R.drawable.nav_turn_go_straight_white_lock;
            } else {
                directionDrawable = R.drawable.ic_turn_straight_blue;
            }
        } else if (directions == Maneuver.Turn.KEEP_MIDDLE) {
            turn = "Keep middle ";
            if (color == 0) {
                directionDrawable = R.drawable.turn_keep_middle;
                //directionDrawableLock = R.drawable.nav_turn_keep_middle_white_lock;
            } else {
                directionDrawable = R.drawable.ic_turn_straight_blue;
            }
        } else if (directions == Maneuver.Turn.LIGHT_RIGHT) {
            turn = "Take Slight Right ";
            if (color == 0) {
                directionDrawable = R.drawable.turn_slight_right;
                //directionDrawableLock = R.drawable.nav_turn_slight_right_white_lock;
            } else {
                directionDrawable = R.drawable.ic_turn_straight_blue;
            }

        } else if (directions == Maneuver.Turn.KEEP_RIGHT) {
            turn = "Keep Right ";
            if (color == 0) {
                directionDrawable = R.drawable.turn_keep_right;
                //  directionDrawableLock = R.drawable.nav_turn_keep_right_white_lock;
            } else {
                directionDrawable = R.drawable.ic_turn_straight_blue;
            }
        } else if (directions == Maneuver.Turn.HEAVY_RIGHT) {
            turn = "Take Sharp Right ";
            if (color == 0) {
                directionDrawable = R.drawable.turn_sharp_right;
                // directionDrawableLock = R.drawable.nav_turn_sharp_right_white_lock;
            } else {
                directionDrawable = R.drawable.ic_turn_straight_blue;
            }
        } else if (directions == Maneuver.Turn.QUITE_RIGHT) {
            turn = "Turn Right ";
            if (color == 0) {
                directionDrawable = R.drawable.turn_right;
                // directionDrawableLock = R.drawable.nav_turn_right_white_lock;
            } else {
                directionDrawable = R.drawable.ic_turn_straight_blue;
            }
        } else if (directions == Maneuver.Turn.LIGHT_LEFT) {
            turn = "Take Slight Left ";
            if (color == 0) {
                directionDrawable = R.drawable.turn_slight_left;
                //  directionDrawableLock = R.drawable.nav_turn_slight_left_white_lock;
            } else {
                directionDrawable = R.drawable.ic_turn_straight_blue;
            }
        } else if (directions == Maneuver.Turn.KEEP_LEFT) {
            turn = "Keep Left ";
            if (color == 0) {
                directionDrawable = R.drawable.turn_keep_left;
                //  directionDrawableLock = R.drawable.nav_turn_keep_left_white_lock;
            } else {
                directionDrawable = R.drawable.ic_turn_straight_blue;
            }
        } else if (directions == Maneuver.Turn.HEAVY_LEFT) {
            turn = "Take Sharp Left ";
            if (color == 0) {
                directionDrawable = R.drawable.turn_sharp_left;
                // directionDrawableLock = R.drawable.nav_turn_sharp_left_white_lock;
            } else {
                directionDrawable = R.drawable.ic_turn_straight_blue;
            }
        } else if (directions == Maneuver.Turn.QUITE_LEFT) {
            turn = "Turn Left ";
            if (color == 0) {
                directionDrawable = R.drawable.turn_left;
                //directionDrawableLock = R.drawable.nav_turn_left_white_lock;
            } else {
                directionDrawable = R.drawable.ic_turn_straight_blue;
            }

        } else if (directions == Maneuver.Turn.RETURN) {
            turn = "Take About Turn ";
            if (color == 0) {
                directionDrawable = R.drawable.turn_uturn_right;
                // directionDrawableLock = R.drawable.nav_turn_uturn_right_white_lock;
            } else {
                directionDrawable = R.drawable.ic_turn_straight_blue;
                // directionDrawableLock = R.drawable.nav_turn_uturn_left_white_lock;
            }
        } else {
            turn = "Take Round About ";
            directionDrawable = R.drawable.turn_uturn_right;
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

    void showEndDialog() {
        final Dialog dialog = new Dialog(NavigationActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_layout);

        TextView title = (TextView) dialog.findViewById(R.id.tvTitle);
        TextView description = (TextView) dialog.findViewById(R.id.tvDescription);
        TextView cancel = (TextView) dialog.findViewById(R.id.tvCancel);
        TextView ok = (TextView) dialog.findViewById(R.id.tvOk);
        title.setText("Destination Reached");
        description.setText("Do you want to tag this location?");
        ok.setText("Yes");

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                finish();

            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();

    }

    void showCloseDialog() {
        final Dialog dialog = new Dialog(NavigationActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_layout);

        TextView title = (TextView) dialog.findViewById(R.id.tvTitle);
        TextView description = (TextView) dialog.findViewById(R.id.tvDescription);
        TextView cancel = (TextView) dialog.findViewById(R.id.tvCancel);
        TextView ok = (TextView) dialog.findViewById(R.id.tvOk);
        title.setText("End Navigation");
        description.setText("Do you want to stop navigation?");
        ok.setText("Yes");

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigationManager.stop();
                LechalApplication.getInstance().setNavigating(false);
                finish();

            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();

    }
    public  void enableHereAudio(boolean status){
        if (SharedPrefUtil.getInt(NavigationActivity.this,SharedPrefUtil.VOICE_PREFERENCE)==0){
            downloadEnglishVoice();
        }
        if (status) {
            navigationManager.setEnabledAudioEvents(EnumSet.of(NavigationManager.AudioEvent.GPS, NavigationManager.AudioEvent.MANEUVER, NavigationManager.AudioEvent.ROUTE));
            navigationManager.setNaturalGuidanceMode(EnumSet.of(NavigationManager.NaturalGuidanceMode.JUNCTION, NavigationManager.NaturalGuidanceMode.TRAFFIC_LIGHT, NavigationManager.NaturalGuidanceMode.STOP_SIGN));
        }else{
            navigationManager.setEnabledAudioEvents(EnumSet.of(NavigationManager.AudioEvent.GPS));
        }

    }
    public void downloadEnglishVoice(){
        VoiceCatalog voiceCatalog = VoiceCatalog.getInstance();
        voiceCatalog.downloadCatalog(new VoiceCatalog.OnDownloadDoneListener() {
            @Override
            public void onDownloadDone(VoiceCatalog.Error error) {
                if (error == VoiceCatalog.Error.NONE) {
                // catalog download successful
                }
            }

        });
        // Get the list of voice packages from the voice catalog list
        List<VoicePackage> voicePackages = VoiceCatalog.getInstance().getCatalogList();
        long id = -1;
        // select
        for (VoicePackage p : voicePackages) {
            if (p.getMarcCode().compareToIgnoreCase("eng") == 0) {
                if (p.isTts()) {
                    id = p.getId();
                    break;
                }
            }
        }

        if (!voiceCatalog.isLocalVoiceSkin(id))
        {
            final long finalId = id;
            voiceCatalog.downloadVoice(id, new VoiceCatalog.OnDownloadDoneListener() {
                @Override
                public void onDownloadDone(VoiceCatalog.Error error) {
                    if (error == VoiceCatalog.Error.NONE){
                    //voice skin download successful
                        Log.d("voiceId", finalId +"");
                        SharedPrefUtil.commitInt(NavigationActivity.this, SharedPrefUtil.VOICE_PREFERENCE, (int) finalId);
                        navigationManager.setVoiceSkin(VoiceCatalog.getInstance().getLocalVoiceSkin(SharedPrefUtil.getVoiceId(NavigationActivity.this, SharedPrefUtil.VOICE_PREFERENCE)));

                    }
                }


            });
        }


    }
    void setRouteOptions(){
        routeOptions.setFerriesAllowed(SharedPrefUtil.getBoolean(getApplicationContext(),SharedPrefUtil.FERRY));
        routeOptions.setDirtRoadsAllowed(SharedPrefUtil.getBoolean(getApplicationContext(),SharedPrefUtil.DIRT_ROAD));
        routeOptions.setHighwaysAllowed(SharedPrefUtil.getBoolean(getApplicationContext(),SharedPrefUtil.HIGHWAY));
        routeOptions.setParksAllowed(SharedPrefUtil.getBoolean(getApplicationContext(),SharedPrefUtil.PARK));
        routeOptions.setTollRoadsAllowed(SharedPrefUtil.getBoolean(getApplicationContext(),SharedPrefUtil.TOLL_ROAD));
        routeOptions.setTunnelsAllowed(SharedPrefUtil.getBoolean(getApplicationContext(),SharedPrefUtil.TUNNEL));
        routeOptions.setCarShuttleTrainsAllowed(SharedPrefUtil.getBoolean(getApplicationContext(),SharedPrefUtil.SHUTTLE_TRAIN));
        routeOptions.setCarpoolAllowed(SharedPrefUtil.getBoolean(getApplicationContext(),SharedPrefUtil.CAR_POOL));
    }
    public void showRouteOptions(){
       selected = new ArrayList<>();
        boolean isSelected[] = {SharedPrefUtil.getBoolean(getApplicationContext(),SharedPrefUtil.FERRY),
                SharedPrefUtil.getBoolean(getApplicationContext(),SharedPrefUtil.DIRT_ROAD),
                SharedPrefUtil.getBoolean(getApplicationContext(),SharedPrefUtil.HIGHWAY),
                SharedPrefUtil.getBoolean(getApplicationContext(),SharedPrefUtil.PARK),
                SharedPrefUtil.getBoolean(getApplicationContext(),SharedPrefUtil.TOLL_ROAD),
                SharedPrefUtil.getBoolean(getApplicationContext(),SharedPrefUtil.TUNNEL),
                SharedPrefUtil.getBoolean(getApplicationContext(),SharedPrefUtil.SHUTTLE_TRAIN),
                SharedPrefUtil.getBoolean(getApplicationContext(),SharedPrefUtil.CAR_POOL)
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(NavigationActivity.this);
        builder.setTitle("AVOID");
        builder.setMultiChoiceItems(R.array.avoid, isSelected, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                if(isChecked){
                    toggleOptions(which,true);
                }else {
                    toggleOptions(which,false);
                }
            }
        });
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                calculateRoute(RouteOptions.TransportMode.CAR);
                dialog.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();

    }
    void toggleOptions(int which,boolean value){
        switch (which){
            case 0:
                SharedPrefUtil.commitBoolean(getApplicationContext(),SharedPrefUtil.FERRY,value);
                break;
            case 1:
                SharedPrefUtil.commitBoolean(getApplicationContext(),SharedPrefUtil.DIRT_ROAD,value);
                break;
            case 2:
                SharedPrefUtil.commitBoolean(getApplicationContext(),SharedPrefUtil.HIGHWAY,value);
                break;
            case 3:
                SharedPrefUtil.commitBoolean(getApplicationContext(),SharedPrefUtil.PARK,value);
                break;
            case 4:
                SharedPrefUtil.commitBoolean(getApplicationContext(),SharedPrefUtil.TOLL_ROAD,value);
                break;
            case 5:
                SharedPrefUtil.commitBoolean(getApplicationContext(),SharedPrefUtil.TUNNEL,value);
                break;
            case 6:
                SharedPrefUtil.commitBoolean(getApplicationContext(),SharedPrefUtil.SHUTTLE_TRAIN,value);
                break;
            case 7:
                SharedPrefUtil.commitBoolean(getApplicationContext(),SharedPrefUtil.CAR_POOL,value);
                break;
        }
    }
    class SearchRequestListener implements ResultListener<DiscoveryResultPage> {
        @Override
        public void onCompleted(DiscoveryResultPage results, ErrorCode error) {
            if (error != ErrorCode.NONE) {
                // Handle error
                // progressDialog.cancel();
                Toast.makeText(getApplicationContext(), error.toString() + "error", Toast.LENGTH_SHORT).show();
            } else {

                List<DiscoveryResult> items = results.getItems();
                GeoPolyline geoPolyline = new GeoPolyline();
                geoPolyline.add(LechalApplication.getInstance().getRoute().getRouteGeometry());
                GetWayPoints getWayPoints = new GetWayPoints(geoPolyline,items);
                List<PlaceLink> placeLinkList = getWayPoints.getPlaceLinkList();
                for (PlaceLink place : placeLinkList) {


                        Image myImage = new com.here.android.mpa.common.Image();
                        try {
                            myImage.setImageResource(R.drawable.ic_marker_stop);
                        } catch (IOException e) {
                            Log.d("Exception", e.toString() + "");
                        }
                        MapMarker mm = new MapMarker(place.getPosition(),myImage);

                        mm.setDescription(place.getTitle());
                        clusterLayer.addMarker(mm);

                }

                map.addClusterLayer(clusterLayer);
            }
        }
    }
}
