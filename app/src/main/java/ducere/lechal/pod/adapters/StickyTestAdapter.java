package ducere.lechal.pod.adapters;

/**
 * Created by Siva on 27-04-2016.
 */
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.here.android.mpa.urbanmobility.Line;

import java.util.ArrayList;
import java.util.List;

import ca.barrenechea.widget.recyclerview.decoration.StickyHeaderAdapter;
import ducere.lechal.pod.NavigationActivity;
import ducere.lechal.pod.R;
import ducere.lechal.pod.beans.GeoCoordinate;
import ducere.lechal.pod.beans.Place;

public class StickyTestAdapter extends RecyclerView.Adapter<StickyTestAdapter.ViewHolder> implements
        StickyHeaderAdapter<StickyTestAdapter.HeaderHolder> {

    private LayoutInflater mInflater;
    List<Place> placeList =  new ArrayList<Place>();

    public StickyTestAdapter(Context context,List<Place> placeList) {
        mInflater = LayoutInflater.from(context);
        this.placeList = placeList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        Log.d("viewGroup",i+"");
        final View view = mInflater.inflate(R.layout.row_history_layout, viewGroup, false);
        final View viewFav = mInflater.inflate(R.layout.row_fav_layout, viewGroup, false);

       /* if (i<2)
            return new ViewHolder(viewFav);
        else*/
            return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int i) {
        viewHolder.title.setText(placeList.get(i).getTitle());
        viewHolder.address.setText(placeList.get(i).getVicinity().replace("<br/>",""));
        viewHolder.distance.setText(placeList.get(i).getDistance()/1000.0+"km");
        if(i<2){
            viewHolder.ivTag.setVisibility(View.GONE);
            viewHolder.address.setVisibility(View.GONE);
            viewHolder.distance.setText(placeList.get(i).getVicinity().replace("<br/>",""));
        }
        viewHolder.llRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(i>1){
                    Intent returnIntent = new Intent(v.getContext(),NavigationActivity.class);
                    Place placeLink = placeList.get(i);
                    Place place = new Place(placeLink.getTitle(),placeLink.getVicinity(),placeLink.getDistance(),new GeoCoordinate(placeLink.getGeo().getLatitude(),placeLink.getGeo().getLongitude()));
                    place.setPlaceId(placeLink.getPlaceId());
                    returnIntent.putExtra("place", place);
                    v.getContext().startActivity(returnIntent);
                }


            }
        });

    }

    @Override
    public int getItemCount() {
        return placeList.size();
    }

    @Override
    public long getHeaderId(int position) {
        if(position<2)
            return position/2;
        else
            return placeList.size()-2;
    }

    @Override
    public HeaderHolder onCreateHeaderViewHolder(ViewGroup parent) {
        final View view = mInflater.inflate(R.layout.header_history, parent, false);
        return new HeaderHolder(view);
    }

    @Override
    public void onBindHeaderViewHolder(HeaderHolder viewholder, int position) {
        if (position<2)
            viewholder.header.setText("FAVOURITES");
        else if (position==2)
            viewholder.header.setText("HISTORY" );
    }


    static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView title,address,distance;
        public ImageView ivTag;
        public LinearLayout llRow;


        public ViewHolder(View itemView) {
            super(itemView);
            title = (TextView)itemView.findViewById(R.id.tvTitle);
            address = (TextView)itemView.findViewById(R.id.tvAddress);
            distance = (TextView)itemView.findViewById(R.id.tvDistacne);
            ivTag = (ImageView)itemView.findViewById(R.id.ivTag);
            llRow = (LinearLayout)itemView.findViewById(R.id.llRow);
        }


    }

    static class HeaderHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView header;

        public HeaderHolder(View itemView) {
            super(itemView);

            header = (TextView) itemView;
        }

        @Override
        public void onClick(View v) {
           // Toast.makeText(v.getContext(),"itemFav",Toast.LENGTH_SHORT).show();
        }
    }
}