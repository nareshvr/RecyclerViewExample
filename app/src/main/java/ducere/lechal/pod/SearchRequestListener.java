package ducere.lechal.pod;

import android.content.Context;
import android.text.Html;
import android.util.Log;

import com.here.android.mpa.search.DiscoveryLink;
import com.here.android.mpa.search.DiscoveryResult;
import com.here.android.mpa.search.DiscoveryResultPage;
import com.here.android.mpa.search.ErrorCode;
import com.here.android.mpa.search.PlaceLink;
import com.here.android.mpa.search.ResultListener;

import java.util.List;

import ducere.lechal.pod.interfaces.OnUpdateSearchLocation;

/**
 * Created by Siva on 25-04-2016.
 */
class SearchRequestListener implements ResultListener<DiscoveryResultPage> {
    OnUpdateSearchLocation onUpdateSearchLocation;
    PlaceLink placeLink1;
    public SearchRequestListener(OnUpdateSearchLocation onUpdateSearchLocation) {
        this.onUpdateSearchLocation = onUpdateSearchLocation;
    }

    @Override
    public void onCompleted(DiscoveryResultPage  data, ErrorCode error) {
        if (error != ErrorCode.NONE) {
            // Handle error
            onUpdateSearchLocation.onUpdateSearchLocation(placeLink1);
            onUpdateSearchLocation.onUpdateSearchFullLocation("Address Not Available!!");
            onUpdateSearchLocation.onUpdateSearchLocationAddress("");
        } else {
            // Store the last DiscoveryResultPage for later processing
            List<DiscoveryResult> items = data.getItems();
            // Iterate through the found place items.
            for (DiscoveryResult item : items) {
                // A Item can either be a PlaceLink (meta information
                // about a Place) or a DiscoveryLink (which is a reference
                // to another refined search that is related to the
                // original search; for example, a search for
                // "Leisure & Outdoor").
                if (item.getResultType() == DiscoveryResult.ResultType.PLACE) {
                    PlaceLink placeLink = (PlaceLink) item;
                    // PlaceLink should be presented to the user, so the link can be
                    // selected in order to retrieve additional details about a place
                    // of interest.
                    Log.d("location name", placeLink.getTitle()) ;
                    onUpdateSearchLocation.onUpdateSearchLocation(placeLink);
                    onUpdateSearchLocation.onUpdateSearchLocationAddress(Html.fromHtml(placeLink.getVicinity())+"");
                    onUpdateSearchLocation.onUpdateSearchFullLocation(placeLink.getTitle()+"\n"+ Html.fromHtml(placeLink.getVicinity()));


                } else if (item.getResultType() == DiscoveryResult.ResultType.DISCOVERY) {
                    DiscoveryLink discoveryLink = (DiscoveryLink) item;
                    // DiscoveryLink can also be presented to the user.
                    // When a DiscoveryLink is selected, another search request should be
                    // performed to retrieve results for a specific category.

                }
            }
        }
    }
}
