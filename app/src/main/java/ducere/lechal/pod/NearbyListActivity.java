package ducere.lechal.pod;

import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.here.android.mpa.common.GeoCoordinate;
import com.here.android.mpa.search.AroundRequest;
import com.here.android.mpa.search.CategoryFilter;
import com.here.android.mpa.search.DiscoveryResult;
import com.here.android.mpa.search.DiscoveryResultPage;
import com.here.android.mpa.search.ErrorCode;
import com.here.android.mpa.search.PlaceLink;
import com.here.android.mpa.search.ResultListener;

import java.util.ArrayList;
import java.util.List;

import ducere.lechal.pod.beans.Place;
import ducere.lechal.pod.constants.Convert;
import ducere.lechal.pod.constants.SharedPrefUtil;
import np.TextView;

public class NearbyListActivity extends AppCompatActivity {

    LinearLayout llList;

    final List<PlaceLink> placeLinksList = new ArrayList<PlaceLink>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearby_list);
        llList = (LinearLayout) findViewById(R.id.llList);
        searchPoi(getIntent().getStringExtra("poi"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getIntent().getStringExtra("title"));

    }

    public void searchPoi(String poi) {
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

            GeoCoordinate geo;

            if (SharedPrefUtil.getBoolean(NearbyListActivity.this,SharedPrefUtil.IS_MOCK_ENABLE)){
                geo = new GeoCoordinate(SharedPrefUtil.getDouble(NearbyListActivity.this, SharedPrefUtil.MOCK_LAT), SharedPrefUtil.getDouble(NearbyListActivity.this, SharedPrefUtil.MOCK_LNG));

            }else{
                geo = new GeoCoordinate(SharedPrefUtil.getDouble(NearbyListActivity.this, SharedPrefUtil.CURRENT_LAT), SharedPrefUtil.getDouble(NearbyListActivity.this, SharedPrefUtil.CURRENT_LNG));

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
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();

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
                Toast.makeText(getApplicationContext(), error.toString() + "error", Toast.LENGTH_SHORT).show();
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
                        final PlaceLink place = (PlaceLink) item;
                        LayoutInflater inflater = (LayoutInflater) getApplicationContext()
                                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        View itemView = inflater.inflate(R.layout.row_history_layout, llList, false);
                        TextView title = (TextView) itemView.findViewById(R.id.tvTitle);
                        TextView address = (TextView) itemView.findViewById(R.id.tvAddress);
                        TextView distance = (TextView) itemView.findViewById(R.id.tvDistacne);
                        ImageView ivTag = (ImageView) itemView.findViewById(R.id.ivTag);
                        title.setText(place.getTitle());
                        address.setText(place.getVicinity().replace("<br/>", ", "));
                        distance.setText(Convert.metersToKms(place.getDistance()));
                        llList.addView(itemView);
                        itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                PlaceLink placeLink = place;
                                Place place = new Place(placeLink.getTitle(),placeLink.getVicinity(),placeLink.getDistance(),new ducere.lechal.pod.beans.GeoCoordinate(placeLink.getPosition().getLatitude(),placeLink.getPosition().getLongitude()));
                                place.setPlaceId(placeLink.getId());
                                startActivity(new Intent(NearbyListActivity.this,NavigationActivity.class).putExtra("place",place));
                                finish();
                            }
                        });


                    } else if (item.getResultType() == DiscoveryResult.ResultType.DISCOVERY) {
    

                    }
                }


            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }
}
