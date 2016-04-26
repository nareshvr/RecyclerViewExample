package ducere.lechal.pod.interfaces;

import com.here.android.mpa.search.PlaceLink;

/**
 * Created by Siva on 25-04-2016.
 */
public interface OnUpdateSearchLocation {
    public void onUpdateSearchLocation(PlaceLink placeLink);
    public void onUpdateSearchFullLocation(String loc);
    public void onUpdateSearchLocationAddress(String loc);
}
