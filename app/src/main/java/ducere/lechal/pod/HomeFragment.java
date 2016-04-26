package ducere.lechal.pod;

import android.Manifest;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.AlertDialog;
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
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
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
import com.here.android.mpa.guidance.NavigationManager;
import com.here.android.mpa.mapping.Map;
import com.here.android.mpa.mapping.MapContainer;
import com.here.android.mpa.mapping.MapFragment;
import com.here.android.mpa.mapping.MapMarker;
import com.here.android.mpa.mapping.MapPolyline;
import com.here.android.mpa.mapping.MapRoute;
import com.here.android.mpa.mapping.MapTransitLayer;
import com.here.android.mpa.routing.RouteOptions;
import com.here.android.mpa.search.DiscoveryResultPage;
import com.here.android.mpa.search.HereRequest;
import com.here.android.mpa.search.Location;
import com.here.android.mpa.search.PlaceLink;
import com.here.android.mpa.search.ResultListener;
import com.here.android.mpa.search.ReverseGeocodeRequest2;
import com.what3words.W3wLanguageEnum;
import com.what3words.core.W3wPosition;
import com.what3words.sdk.android.W3wAlreadyInitedException;
import com.what3words.sdk.android.W3wAndroidSDK;
import com.what3words.sdk.android.W3wAndroidSDKFactory;
import com.what3words.sdk.android.W3wLoadedListener;

import java.io.IOException;
import java.lang.ref.WeakReference;

import ducere.lechal.pod.beans.Place;
import ducere.lechal.pod.constants.SharedPrefUtil;
import ducere.lechal.pod.interfaces.OnFragmentInteractionListener;
import ducere.lechal.pod.interfaces.OnUpdateSearchLocation;


public class HomeFragment extends Fragment implements OnUpdateSearchLocation {
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

    LinearLayout llCurrentLoc, llSearch, llSearchBg;
    RelativeLayout rlTransparent;
    TextView tvLocationName, tvLocationAddress, tvW3w, tvEditLocation;
    ImageView ivBack, ivMockLocation, ivSwitchCurrentLoc;
    SharedPrefUtil prefUtil;

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
                tvLocationName = (TextView) view.findViewById(R.id.tvLocName);
                tvLocationAddress = (TextView) view.findViewById(R.id.tvLocAddress);
                tvW3w = (TextView) view.findViewById(R.id.tvW3w);
                tvEditLocation = (TextView) view.findViewById(R.id.tvEditLocation);
                llCurrentLoc = (LinearLayout) view.findViewById(R.id.llCurrentLoc);
                llSearch = (LinearLayout) view.findViewById(R.id.llSearch);
                llSearchBg = (LinearLayout) view.findViewById(R.id.llSearchBg);
                rlTransparent = (RelativeLayout) view.findViewById(R.id.rlTransparent);
                ivBack = (ImageView) view.findViewById(R.id.ivBack);
                ivMockLocation = (ImageView) view.findViewById(R.id.ivMockLocation);
                ivSwitchCurrentLoc = (ImageView) view.findViewById(R.id.ivSwitchCurrentLoc);
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
                        toggleTabs();
                    }
                });
                ivBack.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        toggleTabs();
                    }
                });
                llSearch.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(getActivity(), SearchActivity.class).putExtra("from", 0));
                    }
                });
                ivMockLocation.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivityForResult(new Intent(getActivity(), SearchActivity.class).putExtra("from", 1), 101);
                    }
                });
                ivSwitchCurrentLoc.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(prefUtil.getBoolean(getActivity(),prefUtil.IS_MOCK_ENABLE)){
                            prefUtil.commitBoolean(getActivity(), prefUtil.IS_MOCK_ENABLE, false);
                            ivSwitchCurrentLoc.setVisibility(View.GONE);
                        }
                    }
                });


            } catch (InflateException e) {

            }

        return view;
    }

    public void toggleTabs() {
        if (MainActivity.tabLayout.getVisibility() == View.VISIBLE) {
            MainActivity.tabLayout.setVisibility(View.GONE);
            llSearchBg.setVisibility(View.VISIBLE);
            MainActivity.toolbar.setVisibility(View.GONE);
            // MainActivity.toolbar.animate().translationY(-MainActivity.toolbar.getBottom()).setInterpolator(new AccelerateInterpolator()).start();

            Animation bottomDown = AnimationUtils.loadAnimation(getContext(),
                    R.anim.bottom_down);
            rlTransparent.startAnimation(bottomDown);

            int colorFrom = Color.BLACK;
            int colorTo = Color.TRANSPARENT;
            int duration = 100;
            ObjectAnimator.ofObject(rlTransparent, "backgroundColor", new ArgbEvaluator(), colorFrom, colorTo)
                    .setDuration(duration)
                    .start();

            final Animation slideUp = AnimationUtils.loadAnimation(getContext(),
                    R.anim.slide_up);
            llSearch.startAnimation(slideUp);

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

            bottomDown.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {

                    rlTransparent.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        } else {

            //MainActivity.toolbar.animate().translationY(0).setInterpolator(new DecelerateInterpolator()).start();
            int colorFrom = Color.TRANSPARENT;
            int colorTo = 0xAA000000;
            int duration = 100;
            ObjectAnimator.ofObject(rlTransparent, "backgroundColor", new ArgbEvaluator(), colorFrom, colorTo)
                    .setDuration(duration)
                    .start();

            Animation slideDown = AnimationUtils.loadAnimation(getContext(),
                    R.anim.slide_down);
            llSearch.startAnimation(slideDown);
            slideDown.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                    ivBack.setImageResource(R.drawable.ic_search);
                    tvEditLocation.setText("Enter destination");
                }

                @Override
                public void onAnimationEnd(Animation animation) {

                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });

            Animation bottomUp = AnimationUtils.loadAnimation(getContext(),
                    R.anim.bottom_up);
            rlTransparent.startAnimation(bottomUp);
            bottomUp.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    ivMockLocation.setVisibility(View.GONE);


                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    MainActivity.tabLayout.setVisibility(View.VISIBLE);
                    MainActivity.toolbar.setVisibility(View.VISIBLE);
                    llSearchBg.setVisibility(View.GONE);
                    rlTransparent.setVisibility(View.VISIBLE);


                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });

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
                    map.setZoomLevel(17);


                    //set the map projection
                    map.setProjectionMode(Map.Projection.GLOBE);


                    positioningManager = PositioningManager.getInstance();
                    positioningManager.addListener(
                            new WeakReference<PositioningManager.OnPositionChangedListener>(positionListener));
                    positioningManager.start(PositioningManager.LocationMethod.GPS_NETWORK);
                    LocationManager lm = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

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
                public void onPositionUpdated(PositioningManager.LocationMethod locationMethod, GeoPosition geoPosition, boolean b) {
                    getNearLocation(geoPosition.getCoordinate());
                    what3Words(geoPosition.getCoordinate());
                }

                @Override
                public void onPositionFixChanged(PositioningManager.LocationMethod locationMethod, PositioningManager.LocationStatus locationStatus) {

                }
            };

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
        if (placeLink != null && !prefUtil.getBoolean(getActivity(),prefUtil.IS_MOCK_ENABLE)) {
            tvLocationName.setText(placeLink.getTitle());
            tvLocationAddress.setText(placeLink.getVicinity().split("<br/>")[0]);
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
        if (resultCode == Activity.RESULT_OK && requestCode == 101) {
            toggleTabs();
            tvLocationName.setText(data.getStringExtra("title"));
            tvLocationAddress.setText(data.getStringExtra("vicinity").split("<br/>")[0]);
            ivSwitchCurrentLoc.setVisibility(View.VISIBLE);
            prefUtil.commitBoolean(getActivity(), prefUtil.IS_MOCK_ENABLE, true);
            Log.d("geo", data.getStringExtra("geo"));
        }
    }
}
