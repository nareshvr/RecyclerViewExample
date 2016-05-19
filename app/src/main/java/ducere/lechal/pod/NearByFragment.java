package ducere.lechal.pod;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;


import com.bignerdranch.expandablerecyclerview.Adapter.ExpandableRecyclerAdapter;
import com.here.android.mpa.common.GeoCoordinate;
import com.here.android.mpa.common.MapEngine;
import com.here.android.mpa.common.OnEngineInitListener;
import com.here.android.mpa.common.PositioningManager;
import com.here.android.mpa.routing.RouteOptions;
import com.here.android.mpa.search.AroundRequest;
import com.here.android.mpa.search.CategoryFilter;
import com.here.android.mpa.search.DiscoveryRequest;
import com.here.android.mpa.search.DiscoveryResult;
import com.here.android.mpa.search.DiscoveryResultPage;
import com.here.android.mpa.search.ErrorCode;
import com.here.android.mpa.search.PlaceLink;
import com.here.android.mpa.search.ResultListener;
import com.here.android.mpa.search.SearchRequest;
import com.ogaclejapan.arclayout.ArcLayout;
import com.poliveira.parallaxrecyclerview.HeaderLayoutManagerFixed;
import com.poliveira.parallaxrecyclerview.ParallaxRecyclerAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import ca.barrenechea.widget.recyclerview.decoration.DividerDecoration;
import ducere.lechal.pod.R;
import ducere.lechal.pod.adapters.POI;
import ducere.lechal.pod.adapters.POIAdapter;
import ducere.lechal.pod.beans.POIType;
import ducere.lechal.pod.beans.Place;
import ducere.lechal.pod.constants.Convert;
import ducere.lechal.pod.constants.SharedPrefUtil;
import np.TextView;


public class NearByFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    View view;
    FrameLayout menuLayout;
    ArcLayout arcLayout;
    ImageView btFab, ivList;
    RelativeLayout rootLayout;
    View centerItem;
    private boolean isNormalAdapter = false;
    private RecyclerView mRecyclerView;
    final List<PlaceLink> placeLinksList = new ArrayList<PlaceLink>();

    boolean isMapEngineInitialize = false;
    PositioningManager positioningManager = null;
    ProgressDialog progressDialog;
    List<String> poiTypes = new ArrayList<String>();
    int selected = 0;
    boolean flag = false,listFlag=false;

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

        if (view != null) {

            ViewGroup parent = (ViewGroup) view.getParent();

            if (parent != null)
                parent.removeView(view);
        } else
            try {
                // Inflate the layout for this fragment
                view = inflater.inflate(R.layout.fragment_near_by, container, false);
                ivList = (ImageView) view.findViewById(R.id.poiList);

                mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler);
                mRecyclerView.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(SearchActivity.etSearch.getWindowToken(), 0);
                        return false;
                    }
                });


                poiTypes.add("eat-play");
                poiTypes.add("business-services");
                poiTypes.add("hospital-health-care-facility");
                poiTypes.add("petrol-station");
                poiTypes.add("administrative-areas-buildings");
                poiTypes.add("hospital-health-care-facility");
                poiTypes.add("going-out");
                poiTypes.add("atm-bank-exchange");

                ivList.setOnClickListener(new View.OnClickListener() {
                                              @Override
                                              public void onClick(View v) {
                                                  if (listFlag) {
                                                      ivList.setBackgroundResource(R.drawable.poi_list);
                                                      createAdapter(mRecyclerView);
                                                      listFlag = false;
                                                  } else {
                                                      setupList();
                                                      listFlag = true;
                                                      ivList.setBackgroundResource(R.drawable.poi_circle);
                                                  }
                                              }
                                          }

                );

                initMapEngine();


            } catch (InflateException e) {

            }

        return view;
    }

    public void initMapEngine() {
        MapEngine mapEngine = MapEngine.getInstance(getActivity().getApplicationContext());
        mapEngine.init(getActivity(), new OnEngineInitListener() {
            @Override
            public void onEngineInitializationCompleted(Error error) {
                // TODO Auto-generated method stub
                if (error == OnEngineInitListener.Error.NONE) {
                    isMapEngineInitialize = true;
                    positioningManager = positioningManager.getInstance();
                    positioningManager.start(PositioningManager.LocationMethod.GPS_NETWORK);
                    //   progressDialog.show();
                    searchPoi("eat-drink");
                } else {
                    // handle factory initialization failure
                    Toast.makeText(getActivity(), "cannot initialize map engine : " + error.toString() + "", Toast.LENGTH_SHORT).show();
                    isMapEngineInitialize = false;
                }
            }
        });
    }

    public void searchPoi(String poi) {
        LocationManager lm = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        if (lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

            GeoCoordinate geo;

            if (SharedPrefUtil.getBoolean(getActivity(),SharedPrefUtil.IS_MOCK_ENABLE)){
                geo = new GeoCoordinate(SharedPrefUtil.getDouble(getActivity(), SharedPrefUtil.MOCK_LAT), SharedPrefUtil.getDouble(getActivity(), SharedPrefUtil.MOCK_LNG));

            }else{
                geo = new GeoCoordinate(SharedPrefUtil.getDouble(getActivity(), SharedPrefUtil.CURRENT_LAT), SharedPrefUtil.getDouble(getActivity(), SharedPrefUtil.CURRENT_LNG));

            }

            AroundRequest request = new AroundRequest().setCategoryFilter(new CategoryFilter().add(poi)).setSearchArea(geo, 100000);


               /* DiscoveryRequest request =
                        new SearchRequest(poi).setSearchCenter(positioningManager.getPosition().getCoordinate());*/
                /*// limit number of items in each result page to 10
                request.setCollectionSize(20);
                ErrorCode error = request.execute(new SearchRequestListener());*/

            request.setCollectionSize(50);
            ErrorCode error = request.execute(new SearchRequestListener());

            if (error != ErrorCode.NONE) {
                // Handle request error
                // progressDialog.cancel();
                Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_SHORT).show();

            }
            //listView.expandGroupWithAnimation(groupPositions);


        }

    }

    class SearchRequestListener implements ResultListener<DiscoveryResultPage> {
        @Override
        public void onCompleted(DiscoveryResultPage results, ErrorCode error) {
            if (error != ErrorCode.NONE) {
                // Handle error
                // progressDialog.cancel();
                Toast.makeText(getActivity(), error.toString() + "error", Toast.LENGTH_SHORT).show();
            } else {

                /**  Process result data
                 The results is a DiscoveryResultPage which represents a
                 paginated collection of items. **/
                // Iterate through the found place items.
                List<DiscoveryResult> items = results.getItems();
                placeLinksList.clear();

                for (DiscoveryResult item : items) {
                        /* A Item can either be a PlaceLink (meta information
						 about a Place) or a DiscoveryLink (which is a reference
						 to another refined search that is related to the
						 original search; for example, a search for
						 "Leisure & Outdoor"). **/
                    if (item.getResultType() == DiscoveryResult.ResultType.PLACE) {


                        placeLinksList.add((PlaceLink) item);


                    } else if (item.getResultType() == DiscoveryResult.ResultType.DISCOVERY) {

                    }
                }

                createAdapter(mRecyclerView);

                // progressDialog.cancel();

            }
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();

    }


    @SuppressWarnings("NewApi")
    private void showMenu() {
        menuLayout.setVisibility(View.VISIBLE);

        List<Animator> animList = new ArrayList<>();

        for (int i = 0, len = arcLayout.getChildCount(); i < len; i++) {
            animList.add(createShowItemAnimator(arcLayout.getChildAt(i)));
        }

        AnimatorSet animSet = new AnimatorSet();
        animSet.setDuration(400);

        animSet.setInterpolator(new OvershootInterpolator());
        animSet.playTogether(animList);
        animSet.start();
    }

    @SuppressWarnings("NewApi")
    private void hideMenu() {

        List<Animator> animList = new ArrayList<>();

        for (int i = arcLayout.getChildCount() - 1; i >= 0; i--) {
            animList.add(createHideItemAnimator(arcLayout.getChildAt(i)));
        }

        AnimatorSet animSet = new AnimatorSet();
        animSet.setDuration(400);
        animSet.setInterpolator(new AnticipateInterpolator());
        animSet.playTogether(animList);
        animSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                menuLayout.setVisibility(View.INVISIBLE);
            }
        });
        animSet.start();

    }

    private Animator createShowItemAnimator(View item) {

        float dx = btFab.getX() - item.getX();
        float dy = btFab.getY() - item.getY();

        item.setRotation(0f);
        item.setTranslationX(dx);
        item.setTranslationY(dy);

        Animator anim = ObjectAnimator.ofPropertyValuesHolder(
                item,
                AnimatorUtils.rotation(0f, 720f),
                AnimatorUtils.translationX(dx, 0f),
                AnimatorUtils.translationY(dy, 0f)
        );

        return anim;
    }

    private Animator createHideItemAnimator(final View item) {
        float dx = btFab.getX() - item.getX();
        float dy = btFab.getY() - item.getY();

        Animator anim = ObjectAnimator.ofPropertyValuesHolder(
                item,
                AnimatorUtils.rotation(720f, 0f),
                AnimatorUtils.translationX(0f, dx),
                AnimatorUtils.translationY(0f, dy)
        );

        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                item.setTranslationX(0f);
                item.setTranslationY(0f);
            }
        });

        return anim;
    }


    private void createAdapter(RecyclerView recyclerView) {
        flag = false;

        final ParallaxRecyclerAdapter<PlaceLink> adapter = new ParallaxRecyclerAdapter<PlaceLink>(placeLinksList) {
            private static final int TYPE_HEADER = 0;
            private static final int TYPE_ITEM = 1;

            @Override
            public void onBindViewHolderImpl(RecyclerView.ViewHolder viewHolder, final ParallaxRecyclerAdapter<PlaceLink> adapter, final int i) {

                if (viewHolder instanceof ViewHolder) {
                    ((ViewHolder) viewHolder).title.setText(adapter.getData().get(i).getTitle());
                    ((ViewHolder) viewHolder).address.setText(adapter.getData().get(i).getVicinity().replace("<br/>", ", "));
                    ((ViewHolder) viewHolder).distance.setText(Convert.metersToKms(adapter.getData().get(i).getDistance()));
                    ((ViewHolder) viewHolder).llRow.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            PlaceLink placeLink = adapter.getData().get(i);
                            Place place = new Place(placeLink.getTitle(),placeLink.getVicinity(),placeLink.getDistance(),new ducere.lechal.pod.beans.GeoCoordinate(placeLink.getPosition().getLatitude(),placeLink.getPosition().getLongitude()));
                            place.setPlaceId(placeLink.getId());
                            startActivity(new Intent(getActivity(),NavigationActivity.class).putExtra("place",place));
                        }
                    });


                } else if (viewHolder instanceof HeaderHolder) {
                    ((HeaderHolder) viewHolder).header.setText(adapter.getData().get(i).getTitle());
                }

            }


            @Override
            public RecyclerView.ViewHolder onCreateViewHolderImpl(ViewGroup viewGroup, final ParallaxRecyclerAdapter<PlaceLink> adapter, int i) {


                if (i == VIEW_TYPES.HEADER) {
                    return new HeaderHolder(getActivity().getLayoutInflater().inflate(R.layout.header_history, viewGroup, false));

                } else {
                    return new ViewHolder(getActivity().getLayoutInflater().inflate(R.layout.row_history_layout, viewGroup, false));

                }


            }

            @Override
            public int getItemCountImpl(ParallaxRecyclerAdapter<PlaceLink> adapter) {
                return placeLinksList.size();
            }

            @Override
            public int getItemViewType(int position) {
                Log.d("ItemView", position + "");

                if (position == 0)
                    return VIEW_TYPES.HEADER;


                return VIEW_TYPES.NORMAL;
            }

        };

        adapter.setOnClickEvent(new ParallaxRecyclerAdapter.OnClickEvent() {
            @Override
            public void onClick(View v, int position) {
                //   Toast.makeText(getActivity(), "You clicked '" + position + "'", Toast.LENGTH_SHORT).show();
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        View header = getActivity().getLayoutInflater().inflate(R.layout.nearby_header, recyclerView, false);

        //Arc Animation
        menuLayout = (FrameLayout) header.findViewById(R.id.menu_layout);
        arcLayout = (ArcLayout) header.findViewById(R.id.arc_layout);
        rootLayout = (RelativeLayout) header.findViewById(R.id.rlRoot);
        centerItem = (View) header.findViewById(R.id.center_item);
        for (int i = 0, size = arcLayout.getChildCount(); i < size; i++) {
            final int finalI = i;
            arcLayout.getChildAt(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selected = finalI;
                    searchPoi(poiTypes.get(finalI));
                }
            });
        }

        setDefault();
        setClicked(selected);

        btFab = (ImageView) header.findViewById(R.id.fab);
        showMenu();


        adapter.setParallaxHeader(header, recyclerView);

        adapter.setData(placeLinksList);
        recyclerView.setAdapter(adapter);
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        public android.widget.TextView title, address, distance;
        public ImageView ivTag;
        LinearLayout llRow;


        public ViewHolder(View itemView) {
            super(itemView);
            title = (android.widget.TextView) itemView.findViewById(R.id.tvTitle);
            address = (android.widget.TextView) itemView.findViewById(R.id.tvAddress);
            distance = (android.widget.TextView) itemView.findViewById(R.id.tvDistacne);
            ivTag = (ImageView) itemView.findViewById(R.id.ivTag);
            llRow = (LinearLayout)itemView.findViewById(R.id.llRow);
        }
    }

    static class HeaderHolder extends RecyclerView.ViewHolder {
        public android.widget.TextView header;

        public HeaderHolder(View itemView) {
            super(itemView);

            header = (android.widget.TextView) itemView;
        }
    }

    public void setDefault() {
        arcLayout.getChildAt(0).setBackgroundResource(R.drawable.poi_eat_gray);
        arcLayout.getChildAt(1).setBackgroundResource(R.drawable.poi_services_gray);
        arcLayout.getChildAt(2).setBackgroundResource(R.drawable.poi_hospitals_gray);
        arcLayout.getChildAt(3).setBackgroundResource(R.drawable.poi_petrol_gray);
        arcLayout.getChildAt(4).setBackgroundResource(R.drawable.poi_admin_gray);
        arcLayout.getChildAt(5).setBackgroundResource(R.drawable.poi_hotels_gray);
        arcLayout.getChildAt(6).setBackgroundResource(R.drawable.poi_going_out_gray);
        arcLayout.getChildAt(7).setBackgroundResource(R.drawable.poi_atms_gray);
    }

    public void setClicked(int i) {
        switch (i) {
            case 0:
                arcLayout.getChildAt(0).setBackgroundResource(R.drawable.poi_eat);
                break;
            case 1:
                arcLayout.getChildAt(1).setBackgroundResource(R.drawable.poi_service);
                break;
            case 2:
                arcLayout.getChildAt(2).setBackgroundResource(R.drawable.poi_hospital);
                break;
            case 3:
                arcLayout.getChildAt(3).setBackgroundResource(R.drawable.poi_petrol);
                break;
            case 4:
                arcLayout.getChildAt(4).setBackgroundResource(R.drawable.poi_admin);
                break;
            case 5:
                arcLayout.getChildAt(5).setBackgroundResource(R.drawable.poi_hotels);
                break;
            case 6:
                arcLayout.getChildAt(6).setBackgroundResource(R.drawable.poi_going_out);
                break;
            case 7:
                arcLayout.getChildAt(7).setBackgroundResource(R.drawable.poi_atms);
                break;


        }
    }
    void setupList(){
        POIType allAccom = new POIType("All", "accommodation");
        POIType hotel = new POIType("Hotel", "hotel");
        POIType motel = new POIType("Motel", "motel");
        POIType hostel = new POIType("Hostel", "hostel");
        POIType camping = new POIType("Camping", "camping");

        POIType allAdmin = new POIType("All", "administrative-areas-buildings");
        POIType admin = new POIType("Administrative Region", "administrative-region");
        POIType cityTown = new POIType("City, Town or Village", "city-town-village");
        POIType outDoor = new POIType("Outdoor Area/Complex", "outdoor-area-complex");
        POIType building = new POIType("Building", "building");

        POIType allServices = new POIType("All", "business-services");
        POIType atm = new POIType("ATM/Bank/Exchange", "atm-bank-exchange");
        POIType police = new POIType("Police/Emergency", "police-emergency");
        POIType postOffice = new POIType("Post Office", "post-office");
        POIType tourist = new POIType("Tourist Information", "tourist-information");
        POIType petrol = new POIType("Petrol Station", "petrol-station");
        POIType carRental = new POIType("Car Rental", "car-rental");
        POIType carDealer = new POIType("Car Dealer/Repair", "car-dealer-repair");
        POIType travel = new POIType("Travel Agency", "travel-agency");
        POIType communication = new POIType("Communications/Media", "communication-media");
        POIType business = new POIType("Business/Industry", "business-industry");
        POIType service = new POIType("Service", "service");

        POIType allEat = new POIType("All", "eat-drink");
        POIType restaurant = new POIType("Restaurant", "restaurant");
        POIType snacks = new POIType("Snacks/Fast food", "snacks-fast-food");
        POIType bar = new POIType("Bar/Pub", "bar-pub");
        POIType coffee = new POIType("Coffee/Tea", "coffee-tea");

        POIType allFacilities = new POIType("All", "facilities");
        POIType hospital = new POIType("Hospital or Healthcare Facility", "hospital-health-care-facility");
        POIType educational = new POIType("Educational Facility", "education-facility");
        POIType govt = new POIType("Government or Community Facility", "government-community-facility");
        POIType library = new POIType("Library", "library");
        POIType expo = new POIType("Expo & Convention Facility", "fair-convention-facility");
        POIType parking = new POIType("Parking Facility", "parking-facility");
        POIType publicB = new POIType("Public Bathroom/Rest Area", "toilet-rest-area");
        POIType sports = new POIType("Sport Facility/Venue", "sports-facility-venue");
        POIType facility = new POIType("Facility", "facility");
        POIType religious = new POIType("Religious Place", "religious-place");

        POIType allGoing = new POIType("All", "going-out");
        POIType dance = new POIType("Dance or Nightclub", "dance-night-club");
        POIType snacksFast = new POIType("Snacks/Fast food", "cinema");
        POIType theater = new POIType("Theater, Music & Culture", "theatre-music-culture");
        POIType casino = new POIType("Casino", "casino");

        POIType allLeisure = new POIType("All", "leisure-outdoor");
        POIType recreation = new POIType("Recreation", "recreation");
        POIType theme = new POIType("Theme Park", "amusement-holiday-park");

        POIType allNatural = new POIType("All", "natural-geographical");
        POIType body = new POIType("Body of Water", "body-of-water");
        POIType mountain = new POIType("Mountain or Hill", "mountain-hill");
        POIType underWater = new POIType("Underwater Feature", "undersea-feature");
        POIType forest = new POIType("Forest, Heath or Other Vegetation", "forest-heath-vegetation");

        POIType allShopping = new POIType("All", "shopping");
        POIType kiosk = new POIType("Kiosk/24-7/Convenience Store", "kiosk-convenience-store");
        POIType shopping = new POIType("Shopping Center", "mall");
        POIType department = new POIType("Department Store", "department-store");
        POIType food = new POIType("Food & Drink", "food-drink");
        POIType book = new POIType("Book Shop", "bookshop");
        POIType pharmacy = new POIType("Pharmacy", "pharmacy");
        POIType electronics = new POIType("Electronics", "electronics-shop");
        POIType diy = new POIType("DIY/Garden center", "hardware-house-garden-shop");
        POIType clothing = new POIType("Clothing & Accessories", "clothing-accessories-shop");
        POIType outdoor = new POIType("Outdoor Sports", "sport-outdoor-shop");
        POIType store = new POIType("Store", "shop");

        POIType allSights = new POIType("All", "sights-museums");
        POIType landmark = new POIType("Landmark/Attraction", "landmark-attraction");
        POIType museum = new POIType("Museum", "museum");

        POIType allTransport = new POIType("All", "transport");
        POIType airport = new POIType("Airport", "airport");
        POIType rail = new POIType("Railway Station", "railway-station");
        POIType transit = new POIType("Public Transit", "public-transport");
        POIType ferry = new POIType("Ferry Terminal", "ferry-terminal");
        POIType taxi = new POIType("Taxi Stand", "taxi-stand");


        POI accommodationList = new POI("Accommodation", Arrays.asList(allAccom, hotel, motel, hostel, camping));
        POI adminList = new POI("Administrative Areas/Buildings", Arrays.asList(allAdmin, admin, cityTown, outDoor, building));
        POI businessList = new POI("Business & Services", Arrays.asList(allServices, atm, police, postOffice, tourist, petrol, carRental, carDealer, travel, communication, business, service));
        POI eatList = new POI("Eat & Drink", Arrays.asList(allEat, restaurant, snacks, bar, coffee));
        POI facilitiesList = new POI("Facilities", Arrays.asList(allFacilities, hospital, educational, govt, library, expo, parking, publicB, sports, facility, religious));
        POI goingList = new POI("Going Out", Arrays.asList(allGoing, dance, snacksFast, theater, casino));
        POI leisureList = new POI("Leisure & Outdoor", Arrays.asList(allLeisure, recreation, theme));
        POI naturalList = new POI("Natural or Geographical", Arrays.asList(allNatural, body, mountain, underWater, forest));
        POI shoppingList = new POI("Shopping", Arrays.asList(allShopping, kiosk, shopping, department, food, book, pharmacy, electronics, diy, clothing, outdoor, store));
        POI sightsList = new POI("Sights & Museums", Arrays.asList(allSights, landmark, museum));
        POI transList = new POI("Transport", Arrays.asList(allTransport, airport, rail, transit, ferry, taxi));

        List<POI> poi = Arrays.asList(accommodationList, adminList,
                businessList, eatList, facilitiesList, goingList, leisureList,
                naturalList, shoppingList, sightsList, transList);

        final POIAdapter adapter = new POIAdapter(getActivity(), poi);
        adapter.setExpandCollapseListener(new ExpandableRecyclerAdapter.ExpandCollapseListener() {
            private int lastExpanded = -1;
            @Override
            public void onListItemExpanded(int position) {
                if (lastExpanded != -1)
                    adapter.collapseParent(lastExpanded); // collapse previous expanded item.
                lastExpanded = position; // set last expanded parent position.
            }

            @Override
            public void onListItemCollapsed(int position) {
                if (position == lastExpanded) {
                    lastExpanded = -1;
                }
            }
        });
        mRecyclerView.setAdapter(adapter);
    }
}




