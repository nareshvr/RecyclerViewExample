package ducere.lechal.pod;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * Created by VR Naresh on 14-05-2016.
 */
public class DownloadMapActivity extends AppCompatActivity implements View.OnClickListener{
    ImageView imvMapPopupMenu;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_map);
         imvMapPopupMenu = (ImageView)findViewById(R.id.imvMapPopupMenu);
         imvMapPopupMenu.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imvMapPopupMenu:
                //Creating the instance of PopupMenu
                PopupMenu popup = new PopupMenu(DownloadMapActivity.this, imvMapPopupMenu);
                popup.getMenuInflater().inflate(R.menu.map_popup_menu, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        Toast.makeText(DownloadMapActivity.this,"You Clicked : " + item.getTitle(),Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(DownloadMapActivity.this,DownloadMapListActivity.class));
                        return true;
                    }
                });
                popup.show();//showing popup menu
                break;
        }
        }
    }

