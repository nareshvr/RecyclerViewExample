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
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;


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
import java.util.Collections;
import java.util.List;

import ducere.lechal.pod.R;
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
    ImageView btFab;
    RelativeLayout rootLayout;
    View centerItem;
    private boolean isNormalAdapter = false;
    private RecyclerView mRecyclerView;
    final List<PlaceLink> placeLinksList = new ArrayList<PlaceLink>();

    boolean isMapEngineInitialize = false;
    PositioningManager positioningManager= null;
    ProgressDialog progressDialog;
    List<String> poiTypes = new ArrayList<String>();
    int selected=0;
    boolean flag=false;
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

                mRecyclerView = (RecyclerView)view. findViewById(R.id.recycler);
                poiTypes.add("eat-play");
                poiTypes.add("business-services");
                poiTypes.add("hospital-health-care-facility");
                poiTypes.add("petrol-station");
                poiTypes.add("administrative-areas-buildings");
                poiTypes.add("hospital-health-care-facility");
                poiTypes.add("going-out");
                poiTypes.add("atm-bank-exchange");

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

    public void searchPoi(String poi){
        LocationManager lm = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);
        if(lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

                GeoCoordinate geo;

                    geo = new GeoCoordinate(SharedPrefUtil.getDouble(getActivity(),SharedPrefUtil.CURRENT_LAT),SharedPrefUtil.getDouble(getActivity(),SharedPrefUtil.CURRENT_LNG));

                AroundRequest request = new AroundRequest().setCategoryFilter(new CategoryFilter().add(poi)).setSearchArea(geo,100000);


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
            public void onBindViewHolderImpl(RecyclerView.ViewHolder viewHolder, ParallaxRecyclerAdapter<PlaceLink> adapter, int i) {

                if (viewHolder instanceof ViewHolder) {
                    ((ViewHolder) viewHolder).title.setText(adapter.getData().get(i).getTitle());
                    ((ViewHolder) viewHolder).address.setText(adapter.getData().get(i).getVicinity().replace("<br/>", ", "));
                    ((ViewHolder) viewHolder).distance.setText(adapter.getData().get(i).getDistance() / 1000.0 + "km");
                } else if (viewHolder instanceof HeaderHolder) {
                    ((HeaderHolder) viewHolder).header.setText(adapter.getData().get(i).getTitle());
                }

            }


            @Override
            public RecyclerView.ViewHolder onCreateViewHolderImpl(ViewGroup viewGroup, final ParallaxRecyclerAdapter<PlaceLink> adapter, int i) {


                if(i == VIEW_TYPES.HEADER ){
                    return new HeaderHolder(getActivity().getLayoutInflater().inflate(R.layout.header_history, viewGroup, false));

                }else {
                    return new ViewHolder(getActivity().getLayoutInflater().inflate(R.layout.row_history_layout, viewGroup, false));

                }


            }

            @Override
            public int getItemCountImpl(ParallaxRecyclerAdapter<PlaceLink> adapter) {
                return placeLinksList.size();
            }
            @Override
            public int getItemViewType(int position) {
                Log.d("ItemView", position+"");

                if(position==0)
                    return  VIEW_TYPES.HEADER ;


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
        menuLayout = (FrameLayout)header. findViewById(R.id.menu_layout);
        arcLayout = (ArcLayout)header. findViewById(R.id.arc_layout);
        rootLayout = (RelativeLayout)header.findViewById(R.id.rlRoot);
        centerItem = (View)header.findViewById(R.id.center_item);
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
        btFab.setOnClickListener(new View.OnClickListener() {
                                     @Override
                                     public void onClick(View v) {
                                         if (v.isSelected()) {
                                             hideMenu();
                                         } else {
                                             showMenu();
                                         }
                                         v.setSelected(!v.isSelected());
                                     }
                                 }

        );



        adapter.setParallaxHeader(header, recyclerView);

        adapter.setData(placeLinksList);
        recyclerView.setAdapter(adapter);
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        public android.widget.TextView title,address,distance;
        public ImageView ivTag;


        public ViewHolder(View itemView) {
            super(itemView);
            title = (android.widget.TextView)itemView.findViewById(R.id.tvTitle);
            address = (android.widget.TextView)itemView.findViewById(R.id.tvAddress);
            distance = (android.widget.TextView)itemView.findViewById(R.id.tvDistacne);
            ivTag = (ImageView)itemView.findViewById(R.id.ivTag);
        }
    }
    static class HeaderHolder extends RecyclerView.ViewHolder {
        public android.widget.TextView header;

        public HeaderHolder(View itemView) {
            super(itemView);

            header = (android.widget.TextView) itemView;
        }
    }
    public void setDefault(){
        arcLayout.getChildAt(0).setBackgroundResource(R.drawable.poi_eat_gray);
        arcLayout.getChildAt(1).setBackgroundResource(R.drawable.poi_services_gray);
        arcLayout.getChildAt(2).setBackgroundResource(R.drawable.poi_hospitals_gray);
        arcLayout.getChildAt(3).setBackgroundResource(R.drawable.poi_petrol_gray);
        arcLayout.getChildAt(4).setBackgroundResource(R.drawable.poi_admin_gray);
        arcLayout.getChildAt(5).setBackgroundResource(R.drawable.poi_hotels_gray);
        arcLayout.getChildAt(6).setBackgroundResource(R.drawable.poi_going_out_gray);
        arcLayout.getChildAt(7).setBackgroundResource(R.drawable.poi_atms_gray);
    }
    public void setClicked(int i){
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
}




