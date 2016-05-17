package ducere.lechal.pod;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;

import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;

public class ProfileAchievements extends AppCompatActivity implements View.OnClickListener{
    CollapsingToolbarLayout collapsingToolbarLayout;
    ImageView imvProfileMenu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_contacts);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle("Juliette Bing");
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));

        imvProfileMenu = (ImageView)findViewById(R.id.imvProfileMenu);
        imvProfileMenu.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imvProfileMenu:
                profilePopMenu();
                break;
        }
    }
    private void profilePopMenu() {
        //Creating the instance of PopupMenu
        PopupMenu popup = new PopupMenu(ProfileAchievements.this, imvProfileMenu);
        //Inflating the Popup using xml file
        popup.getMenuInflater().inflate(R.menu.profile_popup_menu, popup.getMenu());

        //registering popup with OnMenuItemClickListener
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {

                switch (item.getItemId()){
                    case R.id.editProfile:
                        startActivity(new Intent(ProfileAchievements.this,EditProfileActivity.class));
                        break;
                    case R.id.clearAchievements:
                        //showSocailMedia();
                        break;
                    case R.id.allowGlobalTracking:
                        break;
                }

                return true;
            }
        });

        popup.show();//showing popup menu
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
