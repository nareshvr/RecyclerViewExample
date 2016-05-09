package ducere.lechal.pod.adapters;

import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;

import com.bignerdranch.expandablerecyclerview.ViewHolder.ChildViewHolder;

import ducere.lechal.pod.NearbyListActivity;
import ducere.lechal.pod.R;
import ducere.lechal.pod.beans.POIType;
import np.TextView;

/**
 * Created by Siva on 07-05-2016.
 */
public class POIChildViewHolder extends ChildViewHolder {

    private TextView mIngredientTextView;
    LinearLayout llView;

    public POIChildViewHolder(View itemView) {
        super(itemView);
        mIngredientTextView = (TextView)itemView.findViewById(R.id.tvName);
        llView = (LinearLayout)itemView.findViewById(R.id.llView);
    }

    public void bind(final POIType poiType) {
        mIngredientTextView.setText(poiType.getType());
        llView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            v.getContext().startActivity(new Intent(v.getContext(), NearbyListActivity.class).putExtra("poi", poiType.getId())
                    .putExtra("title", poiType.getType()));
            }
        });
    }
}
