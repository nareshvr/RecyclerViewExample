package ducere.lechal.pod;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.location.LocationManager;
import android.os.Build;
import android.provider.Settings;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.here.android.mpa.common.MapEngine;
import com.here.android.mpa.common.OnEngineInitListener;
import com.here.android.mpa.common.PositioningManager;
import com.here.android.mpa.search.DiscoveryResult;
import com.here.android.mpa.search.DiscoveryResultPage;
import com.here.android.mpa.search.ErrorCode;
import com.here.android.mpa.search.PlaceLink;
import com.here.android.mpa.search.ResultListener;
import com.here.android.mpa.search.SearchRequest;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ducere.lechal.pod.beans.GeoCoordinate;
import ducere.lechal.pod.beans.Place;
import ducere.lechal.pod.constants.SharedPrefUtil;
import ducere.lechal.pod.sqlite.PlaceUtility;

public class SearchPlaceActivity extends AppCompatActivity implements ActionBar.TabListener {

    public static EditText etSearch;
    LinearLayout llSearchResults;
    boolean isMapEngineInitialize = false;
    PositioningManager positioningManager= null;
    List<DiscoveryResult> items;
    ScrollView scrollView;
    int from=0;
    ImageView ivW3w,ivBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        if (Build.VERSION.SDK_INT >= 21) {
            //getWindow().setNavigationBarColor(getResources().getColor(R.color.colorPrimary));
            getWindow().setStatusBarColor(getResources().getColor(R.color.black));

        }
        initMapEngine();
        from= getIntent().getExtras().getInt("from");

        etSearch = (EditText) findViewById(R.id.etSearch);
        llSearchResults = (LinearLayout) findViewById(R.id.llSearchResult);
        ivW3w = (ImageView)findViewById(R.id.ivW3w);
        ivBack = (ImageView)findViewById(R.id.ivBack);
        scrollView = (ScrollView)findViewById(R.id.scrollView);
        scrollView.setVisibility(View.VISIBLE);

        // Initilization




        scrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(etSearch.getWindowToken(), 0);

                return false;
            }
        });
        ivW3w.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ivW3w.getDrawable() == getResources().getDrawable(R.drawable.ic_w3w_unfilled)) {
                    ivW3w.setImageResource(R.drawable.ic_w3w_filled);

                } else {
                    ivW3w.setImageResource(R.drawable.ic_w3w_unfilled);
                }
            }
        });
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() == 0) {

                    scrollView.setVisibility(View.GONE);
                } else {
                    scrollView.setVisibility(View.VISIBLE);

                    LocationManager lm = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
                    if (isMapEngineInitialize) {
                        if (lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                            if (s.toString().contains("http://places.lechal.com")){
                                return;
                            }
                            if (positioningManager.hasValidPosition()) {
                                try {

                                    SearchRequest request;
                                    com.here.android.mpa.common.GeoCoordinate geo;
                                    if (SharedPrefUtil.getBoolean(SearchPlaceActivity.this, SharedPrefUtil.IS_MOCK_ENABLE)){
                                        geo = new com.here.android.mpa.common.GeoCoordinate(SharedPrefUtil.getDouble(SearchPlaceActivity.this, SharedPrefUtil.MOCK_LAT), SharedPrefUtil.getDouble(SearchPlaceActivity.this, SharedPrefUtil.MOCK_LNG));

                                    }else{
                                        geo = positioningManager.getPosition().getCoordinate();
                                    }
                                    request =
                                            new SearchRequest(etSearch.getText().toString()).setSearchCenter(geo);

                                    // }
                                    // limit number of items in each result page to 10
                                    request.setCollectionSize(30);
                                    ErrorCode error = request.execute(new SearchPlaceListener());
                                    if (error != ErrorCode.NONE) {
                                        // Handle request error
                                        Toast.makeText(getApplicationContext(), error.toString() + "", Toast.LENGTH_SHORT).show();
                                    }
                                } catch (IllegalArgumentException ex) {
                                    // Handle invalid create search request parameters
                                }

                            } else if (positioningManager.getLastKnownPosition().isValid()) {
                                try {

                                    SearchRequest request =
                                            new SearchRequest(etSearch.getText().toString()).setSearchCenter(positioningManager.getPosition().getCoordinate());
                                    // limit number of items in each result page to 10
                                    request.setCollectionSize(30);
                                    ErrorCode error = request.execute(new SearchPlaceListener());
                                    if (error != ErrorCode.NONE) {
                                        // Handle request error
                                        Toast.makeText(getApplicationContext(), error.toString() + "", Toast.LENGTH_SHORT).show();
                                    }
                                } catch (IllegalArgumentException ex) {
                                    // Handle invalid create search request parameters
                                }

                            } else {
                                Toast.makeText(getApplicationContext(), "Waiting for Gps Fix", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            showSettingsAlert();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Waiting for map to initialize", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }

    public void initMapEngine() {
        MapEngine mapEngine = MapEngine.getInstance(getApplicationContext());
        mapEngine.init(getApplicationContext(), new OnEngineInitListener() {
            @Override
            public void onEngineInitializationCompleted(Error error) {
                // TODO Auto-generated method stub
                if (error == OnEngineInitListener.Error.NONE) {
                    isMapEngineInitialize = true;
                    positioningManager = PositioningManager.getInstance();
                    positioningManager.start(PositioningManager.LocationMethod.GPS_NETWORK);

                } else {
                    // handle factory initialization failure
                    Toast.makeText(getApplicationContext(), "cannot initialize map engine : " + error.toString() + "", Toast.LENGTH_SHORT).show();
                    isMapEngineInitialize = false;
                }
            }
        });
    }


    /**
     * Function to show settings alert dialog
     * On pressing Settings button will lauch Settings Options
     * */
    public void showSettingsAlert(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(SearchPlaceActivity.this);

        // Setting Dialog Title
        alertDialog.setTitle("GPS is settings");

        // Setting Dialog Message
        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");

        // On pressing Settings button
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        });

        // on pressing cancel button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();

            }
        });

        // Showing Alert Message
        alertDialog.show();
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {

    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {

    }

    // Example Search request listener
    class SearchPlaceListener implements ResultListener<DiscoveryResultPage> {
        @Override
        public void onCompleted(DiscoveryResultPage results, ErrorCode error) {
            if (error != ErrorCode.NONE) {
                // Handle error

                Toast.makeText(getApplicationContext(), error.toString() + "error", Toast.LENGTH_SHORT).show();
            } else if(results!=null) {
                /**  Process result data
                 The results is a DiscoveryResultPage which represents a
                 paginated collection of items. **/

                items = results.getItems();

                llSearchResults.removeAllViews();
                // Iterate through the found place items.
                /* A Item can either be a PlaceLink (meta information
                 about a Place) or a DiscoveryLink (which is a reference
                 to another refined search that is related to the
                 original search; for example, a search for
                 "Leisure & Outdoor"). **/
                for (DiscoveryResult item : items) {
                    if (item.getResultType() == DiscoveryResult.ResultType.PLACE) {
                        final PlaceLink placeLink = (PlaceLink) item;
                        //  if (placeLink.getDistance() < 100000 ) {

                        LayoutInflater inflater = (LayoutInflater) getApplicationContext()
                                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        View rowView = inflater.inflate(R.layout.search_item_layout, llSearchResults, false);

                        final TextView tvName = (TextView) rowView.findViewById(R.id.tvName);
                        final TextView tvAddress = (TextView) rowView.findViewById(R.id.tvAddress);
                        final TextView tvDistance = (TextView) rowView.findViewById(R.id.tvDistance);
                        final ImageView ivTag = (ImageView)rowView.findViewById(R.id.ivTag);

                        tvName.setText(placeLink.getTitle() + "");
                        tvAddress.setVisibility(View.VISIBLE);
                        tvAddress.setText(placeLink.getVicinity().replace("<br/>", ", ") + "");
                        tvDistance.setText(placeLink.getDistance()/1000.0+" km");


                        rowView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (from == 0) {//for home

                                    Place place = new Place(placeLink.getTitle(),placeLink.getVicinity(),placeLink.getDistance(),new GeoCoordinate(placeLink.getPosition().getLatitude(),placeLink.getPosition().getLongitude()));
                                    place.setPlaceId(placeLink.getId());
                                    place.setTitle("Home");
                                    place.setMockName("Home");
                                    place.setType(4);
                                    new PlaceUtility(SearchPlaceActivity.this).deleteHome();
                                    new PlaceUtility(SearchPlaceActivity.this).updateTagWillDeleteAndInsert(place);
                                    finish();
                                } else {//for Work
                                    Place place = new Place(placeLink.getTitle(),placeLink.getVicinity(),placeLink.getDistance(),new GeoCoordinate(placeLink.getPosition().getLatitude(),placeLink.getPosition().getLongitude()));
                                    place.setPlaceId(placeLink.getId());
                                    place.setTitle("Work");
                                    place.setMockName("Work");
                                    place.setType(5);
                                    new PlaceUtility(SearchPlaceActivity.this).deleteWork();
                                    new PlaceUtility(SearchPlaceActivity.this).updateTagWillDeleteAndInsert(place);
                                    finish();

                                }
                            }
                        });


                        llSearchResults.addView(rowView);
                        /*}else{

                        }*/

                    } else if (item.getResultType() == DiscoveryResult.ResultType.DISCOVERY) {
                        PlaceLink placeLink = (PlaceLink) item;

                    }
                }


            }
        }
    }


}
