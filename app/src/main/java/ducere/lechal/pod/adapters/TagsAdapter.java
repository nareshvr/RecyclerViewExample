package ducere.lechal.pod.adapters;

/**
 * Created by Siva on 27-04-2016.
 */
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import ducere.lechal.pod.NavigationActivity;
import ducere.lechal.pod.R;
import ducere.lechal.pod.beans.GeoCoordinate;
import ducere.lechal.pod.beans.Place;
import ducere.lechal.pod.sqlite.PlaceUtility;

public class TagsAdapter extends RecyclerView.Adapter<TagsAdapter.MyViewHolder>  {

    private List<Place> placeList;
    private int expandedPosition = -1,prevPosition=-1;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView title, address, distance;
        LinearLayout llMenu,llDetails,llNavigate,llEdit,llDelete;


        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.tvTitle);
            address = (TextView) view.findViewById(R.id.tvAddress);
            llMenu = (LinearLayout) view.findViewById(R.id.llMenu);
            llDetails = (LinearLayout) view.findViewById(R.id.llDetails);
            llEdit = (LinearLayout) view.findViewById(R.id.llEdit);
            llNavigate = (LinearLayout) view.findViewById(R.id.llNavigate);
            llDelete = (LinearLayout) view.findViewById(R.id.llDelete);

            llDetails.setOnClickListener(this);
           // llDetails.setOnLongClickListener(this);

        }


        @Override
        public void onClick(View v) {
            if(v.getId()==R.id.llDetails){
                if (expandedPosition >= 0) {
                    int prev = expandedPosition;
                    notifyItemChanged(prev);
                }
                // Set the current position to "expanded"
                expandedPosition = getPosition();
                notifyItemChanged(expandedPosition);
            }

           // Toast.makeText(v.getContext(), "Clicked: "+expandedPosition, Toast.LENGTH_SHORT).show();
        }


    }


    public TagsAdapter(List<Place> placeList) {
        this.placeList = placeList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_tag_layout, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        Place place = placeList.get(position);
        if (place.getMockName()==null){
            holder.title.setText(place.getTitle());
        }else {
            holder.title.setText(place.getMockName());
        }
        holder.address.setText(place.getVicinity().replace("<br/>", ", "));

            if (prevPosition == position) {
                holder.llMenu.setVisibility(View.GONE);
                holder.llDetails.setBackgroundColor(0xffffffff);
                prevPosition=-1;
            }else if (position == expandedPosition) {
                holder.llMenu.setVisibility(View.VISIBLE);
                holder.llDetails.setBackgroundColor(0x11000000);
                prevPosition = expandedPosition;
            }  else {
                holder.llMenu.setVisibility(View.GONE);
                holder.llDetails.setBackgroundColor(0xffffffff);
            }

        holder.llNavigate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent(v.getContext(),NavigationActivity.class);
                Place placeLink = placeList.get(position);
                Place place = new Place(placeLink.getTitle(),placeLink.getVicinity(),placeLink.getDistance(),new GeoCoordinate(placeLink.getGeo().getLatitude(),placeLink.getGeo().getLongitude()));
                returnIntent.putExtra("place", place);
                v.getContext().startActivity(returnIntent);
            }
            });
        holder.llEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Place updatePlace = placeList.get(position);
                showServerDialog(v, updatePlace);
            }
        });
        holder.llDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Place updatePlace = placeList.get(position);
                showDeleteTag(v, updatePlace,position);
            }
        });


    }



    @Override
    public int getItemCount() {
        return placeList.size();
    }
    void showDeleteTag(final View v, final Place updatePlace, final int pos){
        final AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
        alert.setTitle("Delete Tag");
        alert.setMessage("Do you want to delete " + updatePlace.getMockName() + "");
        alert.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                PlaceUtility placeUtility = new PlaceUtility(v.getContext());

                if (updatePlace.getType()==3){
                    updatePlace.setType(1);
                    placeUtility.updateTagWillDeleteAndInsert(updatePlace);
                }else {
                    placeUtility.deleteTag(updatePlace.getPlaceId());
                }
                notifyItemRemoved(pos);
                dialog.dismiss();
            }
        });
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.dismiss();
            }
        });
        alert.show();
    }
    void showServerDialog(final View v, final Place updatePlace){
        final AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
        final EditText edittext = new EditText(v.getContext());
        alert.setTitle("Edit tag");
        alert.setMessage("Change name");

        alert.setView(edittext);


        alert.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //What ever you want to do with the value

                String server = edittext.getText().toString();
                PlaceUtility placeUtility =  new PlaceUtility(v.getContext());
                updatePlace.setMockName(server);
                placeUtility.updateTagWillDeleteAndInsert(updatePlace);
                notifyDataSetChanged();
                dialog.dismiss();
                // alert.
            }
        });


        alert.show();
    }

}
