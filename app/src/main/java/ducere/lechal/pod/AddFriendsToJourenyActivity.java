package ducere.lechal.pod;

import android.Manifest;
import android.app.SearchManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import ducere.lechal.pod.beans.GroupJourney;
import ducere.lechal.pod.beans.Place;
import ducere.lechal.pod.constants.SharedPrefUtil;
import ducere.lechal.pod.retrofit.LechalService;
import ducere.lechal.pod.retrofit.RetroClient;
import ducere.lechal.pod.server_models.ContactsCount;
import ducere.lechal.pod.server_models.Friends;
import ducere.lechal.pod.server_models.GroupCreateResponse;
import ducere.lechal.pod.server_models.User;
import ducere.lechal.pod.sqlite.PlaceUtility;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddFriendsToJourenyActivity extends AppCompatActivity {

    LechalService lechalService;
    List<String> names = new ArrayList<>();
    List<String> selectedNames = new ArrayList<String>();
    List<String> selectedFriends = new ArrayList<String>();
    HashMap<String,Friends> friendsMap =  new HashMap<>();
    Friends[] friends;
    LinearLayout llContacts;
    Place place;
    TextView tvLocation,tvAddress;
    CardView cardRequest;
    GroupJourney groupJourney;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friends_to_joureny);
        lechalService = RetroClient.getInstance().getService();
        llContacts = (LinearLayout) findViewById(R.id.llContacts);
        cardRequest = (CardView)findViewById(R.id.llRequest) ;
        place = (Place) getIntent().getSerializableExtra("place");
        tvLocation = (np.TextView)findViewById(R.id.tvSearchLocation);
        tvAddress = (np.TextView)findViewById(R.id.tvSearchAddress);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS},111);

        }else{
            sendContacts();

        }
        tvLocation.setText(place.getTitle());
        tvAddress.setText(place.getVicinity().replace("<br/>",", "));
        cardRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final GroupJourney groupJourney = new GroupJourney();
                groupJourney.setGroupId("");
                groupJourney.setTitle("Plan to "+place.getTitle());
                groupJourney.setCreatedUser(SharedPrefUtil.getUserId(getApplicationContext()));
                groupJourney.setDescription("");
                groupJourney.setDestName(place.getTitle());
                groupJourney.setDestLocation(place.getGeo().getLatitude()+","+place.getGeo().getLongitude());
                groupJourney.setType("102");
                groupJourney.setJourneyType(802);
                Object[] objectList = selectedFriends.toArray();
                String[] stringArray =  Arrays.copyOf(objectList,objectList.length,String[].class);
                groupJourney.setInvitedUsers(stringArray);
                final Call<GroupCreateResponse> group = lechalService.createGroupJourney(groupJourney);
                group.enqueue(new Callback<GroupCreateResponse>() {
                    @Override
                    public void onResponse(Call<GroupCreateResponse> call, Response<GroupCreateResponse> response) {
                        if (response.body()==null){
                            if (response.body().getStatus()==150){
                                groupJourney.setGroupId(response.body().getGroupId());
                                PlaceUtility placeUtility = new PlaceUtility(getApplicationContext());
                                placeUtility.putGroupJourney(groupJourney);
                            }
                        }

                    }

                    @Override
                    public void onFailure(Call<GroupCreateResponse> call, Throwable t) {

                    }
                });

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.friends_search, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView mSearchView = (SearchView) MenuItemCompat.getActionView(searchItem);

        final SearchView.SearchAutoComplete searchAutoComplete = (SearchView.SearchAutoComplete)     mSearchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchAutoComplete.setTextColor(Color.WHITE);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
               R.layout.search_item, names);
        searchAutoComplete.setAdapter(adapter);

        SearchManager searchManager =
                (SearchManager) getSystemService(this.SEARCH_SERVICE);
        mSearchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));
        searchAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                // TODO Auto-generated method stub

                selectedNames.add((String)parent.getItemAtPosition(position));
                selectedFriends.add(friendsMap.get((String)parent.getItemAtPosition(position)).getUserId());
                showSelectedContacts();

            }
        });
        return true;

    }
    void showSelectedContacts(){
        llContacts.removeAllViews();
        for (int i=0;i<selectedNames.size();i++){
            LayoutInflater inflater = (LayoutInflater) getApplicationContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View itemView = inflater.inflate(R.layout.friend_item, null, false);
            np.TextView tvName = (np.TextView)itemView.findViewById(R.id.tvName);
            ImageView ivCancel = (ImageView)itemView.findViewById(R.id.ivCancel);
            final ImageView ivProfile = (ImageView)itemView.findViewById(R.id.ivProfile);
            tvName.setText(selectedNames.get(i));
            llContacts.addView(itemView);
            final int finalI = i;
            ivCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectedNames.remove(finalI);
                    selectedFriends.remove(finalI);
                    showSelectedContacts();
                }
            });
            Call<ResponseBody> call = lechalService.fetchProfileImage(friendsMap.get(selectedNames.get(i)).getImg());
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        if (response.body() != null) {
                            // display the image data in a ImageView or save it
                            Bitmap bm = BitmapFactory.decodeStream(response.body().byteStream());
                            ivProfile.setImageBitmap(bm);
                        } else {
                            // TODO
                        }
                    } else {
                        // TODO
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    // TODO
                }
            });
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (requestCode == 111
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            sendContacts();
        }

    }
    private void sendContacts() {

        ArrayList<String> result = new ArrayList<String>();
        JSONObject jObjMain = new JSONObject();

        JSONArray mJSONArray = new JSONArray(result);

        Cursor cursor = getApplicationContext().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
        int count=0;
        while(cursor.moveToNext())
        {
            JSONObject jObj = new JSONObject();
            int phone_idx = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
            int name_idx = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
            String phone = cursor.getString(phone_idx);
            String name = cursor.getString(name_idx);
            count++;
            try {
                jObj.put("name", name);
                jObj.put("phone", phone);
                mJSONArray.put(jObj);
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            result.add(name);
        }

        try {
            jObjMain.put("Contacts", mJSONArray);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Log.d("contacts", jObjMain.toString());
       final Call<ContactsCount> contactsCount = lechalService.sendContacts(SharedPrefUtil.getUserId(getApplicationContext()), jObjMain.toString(),"105");
        contactsCount.enqueue(new Callback<ContactsCount>() {
            @Override
            public void onResponse(Call<ContactsCount> call, Response<ContactsCount> response) {
                if (response.isSuccessful()&&response.body()!=null){

                    if (response.body().getCount()>0){
                        Call<Friends[]> friends = lechalService.getFriends(SharedPrefUtil.getUserId(getApplicationContext()), "105");
                        friends.enqueue(friendsCallback);
                    }
                }
            }

            @Override
            public void onFailure(Call<ContactsCount> call, Throwable t) {

            }
        });


    }
    Callback<Friends[]> friendsCallback = new Callback<Friends[]>() {

        @Override
        public void onResponse(Call<Friends[]> call, Response<Friends[]> response) {

            friends = response.body();

            for (int i=0;i<response.body().length;i++){
                names.add(response.body()[i].getName());
                friendsMap.put(response.body()[i].getName(),response.body()[i]);
            }
        }

        @Override
        public void onFailure(Call<Friends[]> call, Throwable t) {

        }
    };
}
