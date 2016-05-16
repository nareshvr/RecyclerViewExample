package ducere.lechal.pod;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.InflateException;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.AbsoluteLayout;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.here.android.mpa.common.CopyrightLogoPosition;
import com.here.android.mpa.common.GeoCoordinate;
import com.here.android.mpa.common.GeoPolyline;
import com.here.android.mpa.common.GeoPosition;
import com.here.android.mpa.common.Image;
import com.here.android.mpa.common.MapEngine;
import com.here.android.mpa.common.OnEngineInitListener;
import com.here.android.mpa.common.PositioningManager;
import com.here.android.mpa.common.ViewObject;
import com.here.android.mpa.guidance.NavigationManager;
import com.here.android.mpa.mapping.LocationInfo;
import com.here.android.mpa.mapping.Map;
import com.here.android.mpa.mapping.MapCartoMarker;
import com.here.android.mpa.mapping.MapContainer;
import com.here.android.mpa.mapping.MapFragment;
import com.here.android.mpa.mapping.MapGesture;
import com.here.android.mpa.mapping.MapMarker;
import com.here.android.mpa.mapping.MapPolyline;
import com.here.android.mpa.mapping.MapProxyObject;
import com.here.android.mpa.mapping.MapRoute;
import com.here.android.mpa.mapping.MapTransitLayer;
import com.here.android.mpa.mapping.TransitAccessObject;
import com.here.android.mpa.mapping.TransitLineObject;
import com.here.android.mpa.mapping.TransitStopObject;
import com.here.android.mpa.routing.RouteOptions;
import com.here.android.mpa.search.DiscoveryResultPage;
import com.here.android.mpa.search.HereRequest;
import com.here.android.mpa.search.Location;
import com.here.android.mpa.search.PlaceLink;
import com.here.android.mpa.search.ResultListener;
import com.here.android.mpa.search.ReverseGeocodeRequest2;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;
import com.what3words.W3wLanguageEnum;
import com.what3words.core.W3wPosition;
import com.what3words.sdk.android.W3wAlreadyInitedException;
import com.what3words.sdk.android.W3wAndroidSDK;
import com.what3words.sdk.android.W3wAndroidSDKFactory;
import com.what3words.sdk.android.W3wLoadedListener;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.List;

import ducere.lechal.pod.beans.Place;
import ducere.lechal.pod.constants.SharedPrefUtil;
import ducere.lechal.pod.interfaces.OnBackPressed;
import ducere.lechal.pod.interfaces.OnFragmentInteractionListener;
import ducere.lechal.pod.interfaces.OnUpdateSearchLocation;
import ducere.lechal.pod.sqlite.PlaceUtility;
import ducere.lechal.pod.utilities.NavigationFeedback;


public class HomeFragment extends Fragment implements OnUpdateSearchLocation,OnBackPressed {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    View view;
    public boolean isMapEngineInitialize = false;
    // map embedded in the map fragment
    public static Map map = null;
    // map fragment embedded in this activity
    private MapFragment mapFragment = null;
    PositioningManager positioningManager;
    public static MapContainer mapcontainer = null;
    public static MapMarker mapmarker = null;
    W3wAndroidSDK core;

    LinearLayout llCurrentLoc, llSearch, llSearchBg,llTag;
    CardView llSave;
    RelativeLayout rlTransparent;
    TextView tvLocationName, tvLocationAddress, tvW3w, tvEditLocation,tvNavigatingTo;
    ImageView ivBack, ivMockLocation, ivSwitchCurrentLoc;
    SharedPrefUtil prefUtil;
    EditText etTag;
    Place placeTag;
    boolean flag=true;

    long milli;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (view != null) {

            ViewGroup parent = (ViewGroup) view.getParent();

            if (parent != null)
                parent.removeView(view);
        } else
            try {
                view = inflater.inflate(R.layout.fragment_home, container, false);
                milli = System.currentTimeMillis();
                tvLocationName = (TextView) view.findViewById(R.id.tvLocName);
                tvLocationAddress = (TextView) view.findViewById(R.id.tvLocAddress);
                tvW3w = (TextView) view.findViewById(R.id.tvW3w);
                tvEditLocation = (TextView) view.findViewById(R.id.tvEditLocation);
                tvNavigatingTo = (TextView) view.findViewById(R.id.tvNavigatingTo);
                llCurrentLoc = (LinearLayout) view.findViewById(R.id.llCurrentLoc);
                llSearch = (LinearLayout) view.findViewById(R.id.llSearch);
                llSearchBg = (LinearLayout) view.findViewById(R.id.llSearchBg);
                rlTransparent = (RelativeLayout) view.findViewById(R.id.rlTransparent);
                ivBack = (ImageView) view.findViewById(R.id.ivBack);
                ivMockLocation = (ImageView) view.findViewById(R.id.ivMockLocation);
                ivSwitchCurrentLoc = (ImageView) view.findViewById(R.id.ivSwitchCurrentLoc);
                llTag = (LinearLayout) view.findViewById(R.id.llTag);
                llSave = (CardView) view.findViewById(R.id.llSave);
                etTag = (EditText)view.findViewById(R.id.etTag);
                llSearchBg = (LinearLayout) view.findViewById(R.id.llSearchBg);
                    prefUtil = new SharedPrefUtil();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && getActivity().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 111);
                    //After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overriden method

                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && getActivity().checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                            112);
                    //After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overriden method

                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && getActivity().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            113);


                } else {
                    initMapEngine();
                }
                initiateW3w();
                // Views Initialization
                llCurrentLoc.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (LechalApplication.getInstance().isNavigating()){
                            startActivity(new Intent(getActivity(),NavigationActivity.class).putExtra("place",LechalApplication.getInstance().getPlace()));
                        }else {

                            toggleTabs();
                        }
                    }
                });
                ivBack.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(llTag.getVisibility()==View.VISIBLE){
                            if (mapcontainer != null)
                                mapcontainer.removeAllMapObjects();
                            if (mapmarker != null)
                                map.removeMapObject(mapmarker);
                            llTag.setVisibility(View.GONE);
                            llSave.setVisibility(View.GONE);

                        }
                        else
                            toggleTabs();
                    }
                });
                llSearch.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(getActivity(), SearchActivity.class).putExtra("from", 0),
                                ActivityOptions.makeSceneTransitionAnimation(getActivity()).toBundle());
                    }
                });
                ivMockLocation.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivityForResult(new Intent(getActivity(), SearchActivity.class).putExtra("from", 1), 101,
                                ActivityOptions.makeSceneTransitionAnimation(getActivity()).toBundle());
                    }
                });
                ivSwitchCurrentLoc.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showDialogSwitchLocation();

                    }


                });
                llSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(etTag.getText().toString().length()>0 && placeTag!=null){
                            PlaceUtility placeUtility = new PlaceUtility(getActivity());
                            long id = placeUtility.putTag(placeTag);
                            Log.d("insertId",id+"");
                            hideTag();
                        }else{

                        }
                    }
                });


            } catch (InflateException e) {

            }

        return view;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (requestCode == 111
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // Do something with granted permission
            initMapEngine();
        }
        if (requestCode == 112
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // Do something with granted permission
            initMapEngine();
        }
    }
    public void toggleTabs() {
        if (MainActivity.tabLayout.getVisibility() == View.VISIBLE) {
            MainActivity.tabLayout.setVisibility(View.GONE);
            llSearchBg.setVisibility(View.VISIBLE);
            MainActivity.toolbar.setVisibility(View.GONE);


            final Animation slideUp = AnimationUtils.loadAnimation(getContext(),
                    R.anim.slide_up);
            llSearch.startAnimation(slideUp);
           // invisibleView(rlTransparent);
            rlTransparent.setVisibility(View.GONE);

            slideUp.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {

                    //  llSearch.setBackgroundColor(0xff000000);
                    ivBack.setImageResource(R.drawable.ic_back_white);
                    ivMockLocation.setVisibility(View.VISIBLE);
                    tvEditLocation.setText(tvLocationName.getText());
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });


        } else {
            llSearchBg.setVisibility(View.GONE);

            ivBack.setImageResource(R.drawable.search_nav_white);
            tvEditLocation.setText("Enter destination");

            ivMockLocation.setVisibility(View.GONE);
            viewVisible(rlTransparent);



        }
    }


    private void initiateW3w() {
        try {
            new W3wAndroidSDKFactory(getActivity(), R.raw.w3w_master, R.raw.w3w_ybuckets,
                    R.raw.w3w_en_words, new W3wLoadedListener() {
                @Override
                public void loaded(W3wAndroidSDK w3wSDK) {

                    core = w3wSDK;
                }

                @Override
                public void fail(IOException e) {
                }
            }).addEnglish()

                    // .add(this, W3wLanguageEnum.RUSSIAN, R.raw.w3w_ru_words, R.raw.w3w_ru_blocks)
                    .init();
        } catch (W3wAlreadyInitedException e) {
            e.printStackTrace();
        }
    }


    public void initMapEngine() {
        final MapEngine mapEngine = MapEngine.getInstance(getActivity().getApplicationContext());
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
                    map.setZoomLevel(20);

                    //set the map projection
                    map.setProjectionMode(Map.Projection.GLOBE);


                    positioningManager = PositioningManager.getInstance();
                    positioningManager.addListener(
                            new WeakReference<PositioningManager.OnPositionChangedListener>(positionListener));
                    positioningManager.start(PositioningManager.LocationMethod.GPS_NETWORK);
                    LocationManager lm = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
                    mapFragment.getMapGesture().addOnGestureListener(gestureListener);
                    if (lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                        if (positioningManager.hasValidPosition()) {
                            map.setCenter(positioningManager.getPosition().getCoordinate(),
                                    Map.Animation.LINEAR);

                        } else if (positioningManager.getLastKnownPosition().isValid()) {
                            map.setCenter(positioningManager.getLastKnownPosition().getCoordinate(),
                                    Map.Animation.LINEAR);

                        } else {
                            Toast.makeText(getActivity(), "Waiting for Gps Fix", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        showSettingsAlert();
                    }

                    // Display position indicator
                    map.getPositionIndicator().setVisible(true);

                    Image myImage = new com.here.android.mpa.common.Image();
                    try {
                        myImage.setImageResource(R.drawable.ic_marker_location_lechal);
                    } catch (IOException e) {
                        Log.d("Exception", e.toString() + "");
                    }

                    map.getPositionIndicator().setMarker(myImage);
                    map.getPositionIndicator().setAccuracyIndicatorVisible(true);
                    // Listen for gesture events. For example tapping on buildings


                    map.getMapTransitLayer().setMode(MapTransitLayer.Mode.EVERYTHING);


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

    /**
     * Function to show settings alert dialog
     * On pressing Settings button will lauch Settings Options
     */
    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());

        // Setting Dialog Title
        alertDialog.setTitle("GPS is settings");

        // Setting Dialog Message
        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");

        // On pressing Settings button
        alertDialog.setPositiveButton("SETTINGS", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                getActivity().startActivity(intent);
            }
        });

        // on pressing cancel button
        alertDialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();

            }
        });

        // Showing Alert Message
        alertDialog.show();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    // Define positioning listener
    private PositioningManager.OnPositionChangedListener positionListener = new
            PositioningManager.OnPositionChangedListener() {

                @Override
                public void onPositionUpdated(PositioningManager.LocationMethod locationMethod, final GeoPosition geoPosition, boolean b) {

                            if ((System.currentTimeMillis()-milli)>30000 || flag){
                                getNearLocation(geoPosition.getCoordinate());
                                what3Words(geoPosition.getCoordinate());
                                milli=System.currentTimeMillis();
                                flag=false;
                            }
                    if (LechalApplication.getInstance().isNavigating()){
                        NavigationFeedback feedback = new NavigationFeedback(geoPosition.getCoordinate(),getActivity());
                        feedback.findDistanceRange();
                    }

                }

                @Override
                public void onPositionFixChanged(PositioningManager.LocationMethod locationMethod, PositioningManager.LocationStatus locationStatus) {

                }
            };

    @Override
    public void onResume() {
        super.onResume();
        if (LechalApplication.getInstance().isNavigating()){
            tvNavigatingTo.setText("Navigating to "+LechalApplication.getInstance().getNavigate().getEndTitle());
        }else{
            tvNavigatingTo.setText("");
        }
    }


    /**
     * Getting current address
     *
     * @param point
     */
    void getNearLocation(com.here.android.mpa.common.GeoCoordinate point) {
        if (point != null && point.isValid()) {
            // Example code for createReverseGeocodeRequest(GeoCoordinate)
            if (isMapEngineInitialize) {


                ResultListener<DiscoveryResultPage> placesListener = new SearchRequestListener(HomeFragment.this);
                HereRequest hereRequest = new HereRequest();
                hereRequest.setSearchCenter(point);
                hereRequest.setCollectionSize(1);
                hereRequest.execute(placesListener);
            } else {
                initMap();
            }
        }
    }

    public void what3Words(GeoCoordinate geo) {
        // from geo to w3w!
        if (isMapEngineInitialize && geo != null && core != null && geo.isValid()) {
            W3wPosition w3w = core.convertPositionToW3W(W3wLanguageEnum.ENGLISH, geo.getLatitude(), geo.getLongitude());
            if (isMapEngineInitialize) {
                if (geo.isValid() && w3w != null) {
                    w3w = core.convertPositionToW3W(W3wLanguageEnum.ENGLISH, geo.getLatitude(), geo.getLongitude());

                    tvW3w.setText("" + w3w.getW3W());
                }
            } else {
                initMap();
            }

        /*ConnectionDetector connectionDetector = new ConnectionDetector(context);
        if(connectionDetector.isConnectingToInternet()) {
            PostWhat3Words post = new PostWhat3Words(NavigationFragment.this);
            post.execute(new String[]{geo.getLatitude() + "," + geo.getLongitude()});
        }*/
        }
    }

    @Override
    public void onUpdateSearchLocation(PlaceLink placeLink) {
        if(placeLink != null && prefUtil!=null && getActivity()!=null){

            prefUtil.commitString(getActivity(), prefUtil.CURRENT_LOCATION, placeLink.getTitle());
            prefUtil.commitString(getActivity(), prefUtil.CURRENT_VICINITY, placeLink.getVicinity());
            prefUtil.commitDouble(getActivity(), prefUtil.CURRENT_LAT, (float) placeLink.getPosition().getLatitude());
            prefUtil.commitDouble(getActivity(),prefUtil.CURRENT_LNG,(float)placeLink.getPosition().getLongitude());
            setCurrentLocationTexts();
        }

    }

    @Override
    public void onUpdateSearchFullLocation(String loc) {

    }

    @Override
    public void onUpdateSearchLocationAddress(String loc) {
        //tvLocationAddress.setText(loc);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK ) {
            toggleTabs();
            ivSwitchCurrentLoc.setVisibility(View.VISIBLE);
            Place place = (Place)data.getSerializableExtra("place");
            prefUtil.commitString(getActivity(), prefUtil.MOCK_LOCATION, place.getTitle());
            prefUtil.commitDouble(getActivity(), prefUtil.MOCK_LAT, (float) place.getGeo().getLatitude());
            prefUtil.commitDouble(getActivity(),prefUtil.MOCK_LNG,(float)place.getGeo().getLongitude());
            prefUtil.commitString(getActivity(), prefUtil.MOCK_VICINITY, place.getVicinity());
            prefUtil.commitBoolean(getActivity(), prefUtil.IS_MOCK_ENABLE, true);

            setCurrentLocationTexts();

        }
    }
    private void setCurrentLocationTexts() {
        if (!prefUtil.getBoolean(getActivity(),prefUtil.IS_MOCK_ENABLE)) {
            tvLocationName.setText(prefUtil.getString(getActivity(),prefUtil.CURRENT_LOCATION));
            tvLocationAddress.setText(prefUtil.getString(getActivity(), prefUtil.CURRENT_VICINITY).split("<br/>")[0]);
        }else{
            tvLocationName.setText(prefUtil.getString(getActivity(), prefUtil.MOCK_LOCATION));
            tvLocationAddress.setText(prefUtil.getString(getActivity(), prefUtil.MOCK_VICINITY).split("<br/>")[0]);
        }
    }
    void showDialogSwitchLocation(){
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_layout);

        TextView title = (TextView) dialog.findViewById(R.id.tvTitle);
        TextView description = (TextView) dialog.findViewById(R.id.tvDescription);
        TextView cancel = (TextView) dialog.findViewById(R.id.tvCancel);
        TextView ok = (TextView) dialog.findViewById(R.id.tvOk);
        title.setText("Current location");
        description.setText("Do you want to switch your location from "+tvLocationName.getText()+" to your current location?");
        ok.setText("Yes");

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(prefUtil.getBoolean(getActivity(),prefUtil.IS_MOCK_ENABLE)){
                    prefUtil.commitBoolean(getActivity(), prefUtil.IS_MOCK_ENABLE, false);
                    ivSwitchCurrentLoc.setVisibility(View.GONE);
                    setCurrentLocationTexts();
                }
                dialog.dismiss();
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


    @Override
    public void onBackPressed() {
        if(llTag.getVisibility()==View.VISIBLE){
            if (mapcontainer != null)
                mapcontainer.removeAllMapObjects();
            if (mapmarker != null)
                map.removeMapObject(mapmarker);
            llTag.setVisibility(View.GONE);
            llSave.setVisibility(View.GONE);

        }
        else
            toggleTabs();
    }

    void viewVisible(View myView){
        // previously invisible view


    // get the center for the clipping circle
        int cx = myView.getWidth() / 2;
        int cy = myView.getHeight() / 2;

    // get the final radius for the clipping circle
        float finalRadius = (float) Math.hypot(cx, cy);

    // create the animator for this view (the start radius is zero)
        Animator anim =
                ViewAnimationUtils.createCircularReveal(myView, cx, cy, 0, finalRadius);
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationCancel(Animator animation) {
                super.onAnimationCancel(animation);

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                MainActivity.tabLayout.setVisibility(View.VISIBLE);
                MainActivity.toolbar.setVisibility(View.VISIBLE);
                llSearch.setVisibility(View.VISIBLE);
            }
        });

// make the view visible and start the animation
        myView.setVisibility(View.VISIBLE);
        anim.start();
    }

    void invisibleView(final View myView){
        int cx = myView.getWidth() / 2;
        int cy = myView.getHeight() / 2;

    // get the initial radius for the clipping circle
        float initialRadius = (float) Math.hypot(cx, cy);

    // create the animation (the final radius is zero)
        Animator anim =
                ViewAnimationUtils.createCircularReveal(myView, cx, cy, initialRadius, 0);

    // make the view invisible when the animation is done
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                myView.setVisibility(View.INVISIBLE);
            }
        });

    // start the animation
        anim.start();
    }
    private MapGesture.OnGestureListener gestureListener =
            new MapGesture.OnGestureListener.OnGestureListenerAdapter() {

                @Override
                public boolean onMapObjectsSelected(List<ViewObject> objects) {
                    //  Log.d("tapped","tapped");
                    for (ViewObject viewObj : objects) {


                        switch (viewObj.getBaseType()) {
                            case PROXY_OBJECT:
                                MapProxyObject proxyObj = (MapProxyObject) viewObj;
                                switch (proxyObj.getType()) {
                                    case TRANSIT_ACCESS:
                                        TransitAccessObject transitAccessObj =
                                                (TransitAccessObject) proxyObj;
                                        Log.d("proxyObj", "Found a TransitAccessObject");
                                        showPlaceMarker(transitAccessObj.getCoordinate(),transitAccessObj.getTransitAccessInfo().getName(),"","");

                                        break;
                                    case TRANSIT_LINE:
                                        TransitLineObject transitLineObj =
                                                (TransitLineObject) proxyObj;
                                        Log.d("proxyObj", "Found a TransitLineObject");
                                        // showPlaceMarker(transitLineObj., transitStopObj.getTransitStopInfo().getInformalName(), transitStopObj.getTransitStopInfo().getOfficialName());
                                        break;
                                    case TRANSIT_STOP:
                                        TransitStopObject transitStopObj =
                                                (TransitStopObject) proxyObj;
                                        Log.d("proxyObj", "Found a TransitStopObject" +transitStopObj.getTransitStopInfo().getInformalName()+"-"+transitStopObj.getTransitStopInfo().getOfficialName());
                                        showPlaceMarker(transitStopObj.getCoordinate(),transitStopObj.getTransitStopInfo().getOfficialName(),
                                                transitStopObj.getTransitStopInfo().getInformalName(),"");
                                        break;
                                    case MAP_CARTO_MARKER:
                                        MapCartoMarker mapCartoMarker =
                                                (MapCartoMarker) proxyObj;
                                        showPlaceMarker(mapCartoMarker.getLocation().getCoordinate(),mapCartoMarker.getLocation().getInfo().getField(LocationInfo.Field.PLACE_NAME),
                                                mapCartoMarker.getLocation().getInfo().getField(LocationInfo.Field.LOCATION_TEXT),mapCartoMarker.getLocation().getInfo().getField(LocationInfo.Field.PLACE_PHONE_NUMBER));
                                        Log.d("proxyObj", "Found a MapCartoMarker "+mapCartoMarker.getLocation().getInfo().getField(LocationInfo.Field.LOCATION_TEXT));
                                        break;
                                    default:
                                        Log.d("proxyObj", "ProxyObject.getType() unknown");
                                }
                                break;
                            // User objects are more likely to be handled
                            // as in the previous example
                            case USER_OBJECT:
                            default:
                                Log.d("proxyObj",
                                        "ViewObject.getBaseType() is USER_OBJECT or unknown");
                                break;
                        }


                    }
                    return false;
                }
            };

    void showPlaceMarker(final GeoCoordinate geo, final String title, final String address,final String phone   ) {

        if (mapcontainer != null)
            mapcontainer.removeAllMapObjects();
        if (mapmarker != null)
            map.removeMapObject(mapmarker);
        Image myImage = new com.here.android.mpa.common.Image();
        try {
            myImage.setImageResource(R.drawable.location_tag_gray);
        } catch (IOException e) {
            Log.d("Exception", e.toString() + "");
        }
        mapmarker = new MapMarker(geo, myImage);
        mapcontainer = new MapContainer();

        //progressDialog = new ProgressDialog(getActivity());
        //   progressDialog.setMessage("Getting Address...");
        //progressDialog.show();
        map.addMapObject(mapcontainer);
        mapcontainer.addMapObject(mapmarker);
        tvEditLocation.setText(title + ", " + address);
        llTag.setVisibility(View.VISIBLE);
        llSave.setVisibility(View.VISIBLE);
        etTag.setText(title);
        placeTag = new Place(title, address, 0, new ducere.lechal.pod.beans.GeoCoordinate(geo.getLatitude(),geo.getLongitude())) ;
        placeTag.setMockName(etTag.getText().toString());
        placeTag.setType(0);
        placeTag.setIsSynced(false);
    }

    void hideTag(){

            if (mapcontainer != null)
                mapcontainer.removeAllMapObjects();
            if (mapmarker != null)
                map.removeMapObject(mapmarker);
            llTag.setVisibility(View.GONE);
            llSave.setVisibility(View.GONE);


    }

}
