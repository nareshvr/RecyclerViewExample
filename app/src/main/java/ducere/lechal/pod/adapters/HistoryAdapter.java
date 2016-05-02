package ducere.lechal.pod.adapters;

/**
 * Created by Siva on 27-04-2016.
 */
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import ducere.lechal.pod.R;
import ducere.lechal.pod.beans.Place;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.MyViewHolder> {

    private List<Place> placeList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, address, distance;
        public ImageView ivTag;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            address = (TextView) view.findViewById(R.id.tvAddress);
            distance = (TextView) view.findViewById(R.id.tvDistance);
        }
    }


    public HistoryAdapter(List<Place> placeList) {
        this.placeList = placeList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_history_layout, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Place place = placeList.get(position);
        holder.title.setText(place.getTitle());
        holder.address.setText(place.getVicinity().replace("<br/>",", "));
        holder.distance.setText(place.getDistance()/1000.0 +"km");
    }

    @Override
    public int getItemCount() {
        return placeList.size();
    }
}
