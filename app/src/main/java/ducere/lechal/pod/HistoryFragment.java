package ducere.lechal.pod;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ca.barrenechea.widget.recyclerview.decoration.DividerDecoration;
import ca.barrenechea.widget.recyclerview.decoration.StickyHeaderDecoration;
import ducere.lechal.pod.R;
import ducere.lechal.pod.adapters.StickyTestAdapter;
import ducere.lechal.pod.beans.GeoCoordinate;
import ducere.lechal.pod.beans.Place;
import ducere.lechal.pod.sqlite.PlaceUtility;
import np.TextView;


public class HistoryFragment extends Fragment implements RecyclerView.OnItemTouchListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private StickyHeaderDecoration decor;
    View view;
    private RecyclerView mList;
    List<Place> placeList =  new ArrayList<Place>();
    public HistoryFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static HistoryFragment newInstance(String param1, String param2) {
        HistoryFragment fragment = new HistoryFragment();
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
                view =  inflater.inflate(R.layout.fragment_history, container, false);
                mList = (RecyclerView) view.findViewById(R.id.list);
                mList.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(SearchActivity.etSearch.getWindowToken(), 0);
                        return false;
                    }
                });
                final DividerDecoration divider = new DividerDecoration.Builder(this.getActivity())

                        .build();

                mList.setHasFixedSize(true);
                mList.setLayoutManager(new LinearLayoutManager(this.getActivity()));
               /* mList.addItemDecoration(divider, 0);
                mList.addItemDecoration(divider,1);*/

                PlaceUtility placeUtility = new PlaceUtility(getActivity());
                // placeList = placeUtility.getTags();
                Place placeHome = new Place();
                placeHome.setMockName("Home");
                placeHome.setTitle("Home");
                placeHome.setVicinity("");

                Place placeOffice = new Place();
                placeOffice.setMockName("Work");
                placeOffice.setTitle("Work");
                placeOffice.setVicinity("");
                placeList.add(placeHome);
                placeList.add(placeOffice);
                placeList.addAll( placeUtility.getHistory());

                setAdapterAndDecor(mList);

            }catch (InflateException e) {

            }


        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }

    private void setAdapterAndDecor(RecyclerView list) {

        final StickyTestAdapter adapter = new StickyTestAdapter(this.getActivity(),placeList);
        decor = new StickyHeaderDecoration(adapter);
        setHasOptionsMenu(true);

        list.setAdapter(adapter);
        list.addItemDecoration(decor);
        list.addOnItemTouchListener(this);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
       /* if (item.getItemId() == R.id.action_clear_cache) {
            decor.clearHeaderCache();
            return true;
        }
*/
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        // really bad click detection just for demonstration purposes
        // it will not allow the list to scroll if the swipe motion starts
        // on top of a header
        View v = rv.findChildViewUnder(e.getX(), e.getY());
        return v == null;
//        return rv.findChildViewUnder(e.getX(), e.getY()) != null;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        // only use the "UP" motion event, discard all others
        if (e.getAction() != MotionEvent.ACTION_UP) {
            return;
        }

        // find the header that was clicked
        View view = decor.findHeaderViewUnder(e.getX(), e.getY());

        if (view instanceof TextView) {
            Toast.makeText(this.getActivity(), ((TextView) view).getText() + " clicked", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        // do nothing
    }


}
