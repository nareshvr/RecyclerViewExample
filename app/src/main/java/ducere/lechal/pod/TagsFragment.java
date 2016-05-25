package ducere.lechal.pod;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import ca.barrenechea.widget.recyclerview.decoration.DividerDecoration;
import ca.barrenechea.widget.recyclerview.decoration.StickyHeaderDecoration;
import ducere.lechal.pod.R;
import ducere.lechal.pod.adapters.StickyTestAdapter;
import ducere.lechal.pod.adapters.TagsAdapter;
import ducere.lechal.pod.beans.GeoCoordinate;
import ducere.lechal.pod.beans.Place;
import ducere.lechal.pod.sqlite.PlaceUtility;
import np.TextView;


public class TagsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    PlaceUtility placeUtility;
    private StickyHeaderDecoration decor;
    View view;
    private RecyclerView recyclerView;
    List<Place> placeList =  new ArrayList<Place>();
    LinearLayout llNoTags;
    public TagsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TagsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TagsFragment newInstance(String param1, String param2) {
        TagsFragment fragment = new TagsFragment();
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
                view =  inflater.inflate(R.layout.fragment_tags, container, false);
                recyclerView = (RecyclerView) view.findViewById(R.id.list);
                llNoTags = (LinearLayout)view.findViewById(R.id.llNoTags);
                recyclerView.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(SearchActivity.etSearch.getWindowToken(), 0);
                        return false;
                    }
                });

            }catch (InflateException e) {

            }

        return view;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        final DividerDecoration divider = new DividerDecoration.Builder(this.getActivity())

                .build();
        placeUtility = new PlaceUtility(getActivity());
       // placeList = placeUtility.getTags();
        List<Place> placeHome = placeUtility.getHome();
        if(placeHome.size()>0){
            placeList.add(placeHome.get(0));
        }
        List<Place> placeWork = placeUtility.getWork();
        if(placeWork.size()>0){
            placeList.add(placeWork.get(0));
        }


        placeList.addAll( placeUtility.getTags());

       TagsAdapter mAdapter = new TagsAdapter(placeList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(divider);
        recyclerView.setAdapter(mAdapter);

        if (placeList.size()==0)
            llNoTags.setVisibility(View.VISIBLE);


    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);


    }



}
