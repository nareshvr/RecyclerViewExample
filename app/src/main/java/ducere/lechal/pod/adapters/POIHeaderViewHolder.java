package ducere.lechal.pod.adapters;

import android.os.Build;
import android.view.View;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

import com.bignerdranch.expandablerecyclerview.ViewHolder.ParentViewHolder;

import java.util.List;

import ducere.lechal.pod.R;
import np.TextView;

/**
 * Created by Siva on 07-05-2016.
 */
public class POIHeaderViewHolder extends ParentViewHolder {


    public TextView name;
    ImageView ivArrow;
    private static final float INITIAL_POSITION = 0.0f;
    private static final float ROTATED_POSITION = 180f;

    public POIHeaderViewHolder(View itemView) {
        super(itemView);
        name = (TextView)itemView.findViewById(R.id.tvName);
        ivArrow = (ImageView)itemView.findViewById(R.id.ivArrow);
    }

    public void bind(POI poi) {
      name.setText(poi.getName());
    }


    @Override
    public void setExpanded(boolean expanded) {
        super.setExpanded(expanded);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            if (expanded) {
                ivArrow.setRotation(ROTATED_POSITION);
            } else {
                ivArrow.setRotation(INITIAL_POSITION);
            }
        }
    }

    @Override
    public void onExpansionToggled(boolean expanded) {
        super.onExpansionToggled(expanded);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            RotateAnimation rotateAnimation;
            if (expanded) { // rotate clockwise
                rotateAnimation = new RotateAnimation(ROTATED_POSITION,
                        INITIAL_POSITION,
                        RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                        RotateAnimation.RELATIVE_TO_SELF, 0.5f);
            } else { // rotate counterclockwise
                rotateAnimation = new RotateAnimation(-1 * ROTATED_POSITION,
                        INITIAL_POSITION,
                        RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                        RotateAnimation.RELATIVE_TO_SELF, 0.5f);
            }

            rotateAnimation.setDuration(200);
            rotateAnimation.setFillAfter(true);
            ivArrow.startAnimation(rotateAnimation);
        }
    }
}
