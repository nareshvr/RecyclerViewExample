package ducere.lechal.pod.adapters;

import com.bignerdranch.expandablerecyclerview.Model.ParentListItem;

import java.util.List;

import ducere.lechal.pod.beans.POIType;

/**
 * Created by Siva on 07-05-2016.
 */
public class POI implements ParentListItem {

    private String mName;
    private List<POIType> poiTypeList;

    public POI(String name, List<POIType> poiTypeList) {
        mName = name;
        this.poiTypeList = poiTypeList;
    }

    public String getName() {
        return mName;
    }

    @Override
    public List<?> getChildItemList() {
        return poiTypeList;
    }

    @Override
    public boolean isInitiallyExpanded() {
        return false;
    }

}
