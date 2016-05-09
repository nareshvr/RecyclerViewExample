package ducere.lechal.pod.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bignerdranch.expandablerecyclerview.Adapter.ExpandableRecyclerAdapter;
import com.bignerdranch.expandablerecyclerview.Model.ParentListItem;

import java.util.List;

import ducere.lechal.pod.R;
import ducere.lechal.pod.beans.POIType;
import np.TextView;

/**
 * Created by Siva on 07-05-2016.
 */
public class POIAdapter extends ExpandableRecyclerAdapter<POIHeaderViewHolder, POIChildViewHolder> {

    private LayoutInflater mInflator;

    /**
     * Primary constructor. Sets up {@link #mParentItemList} and {@link #mItemList}.
     * <p/>
     * Changes to {@link #mParentItemList} should be made through add/remove methods in
     * {@link ExpandableRecyclerAdapter}
     *
     * @param parentItemList List of all {@link ParentListItem} objects to be
     *                       displayed in the RecyclerView that this
     *                       adapter is linked to
     */
    public POIAdapter(Context context, @NonNull List<? extends ParentListItem> parentItemList) {
        super(parentItemList);
        mInflator = LayoutInflater.from(context);
    }


    // onCreate ...
    @Override
    public POIHeaderViewHolder onCreateParentViewHolder(ViewGroup parentViewGroup) {
        View viewHead = mInflator.inflate(R.layout.row_head_nearby, parentViewGroup, false);
        return new POIHeaderViewHolder(viewHead);
    }

    @Override
    public POIChildViewHolder onCreateChildViewHolder(ViewGroup childViewGroup) {
        View viewChild = mInflator.inflate(R.layout.row_child_near, childViewGroup, false);

        return new POIChildViewHolder(viewChild);
    }

    // onBind ...
    @Override
    public void onBindParentViewHolder(POIHeaderViewHolder recipeViewHolder, int position, ParentListItem parentListItem) {
        POI poi = (POI) parentListItem;
        recipeViewHolder.bind(poi);
    }

    @Override
    public void onBindChildViewHolder(POIChildViewHolder ingredientViewHolder, int position, Object childListItem) {
        POIType ingredient = (POIType) childListItem;

        ingredientViewHolder.bind(ingredient);

    }
}
