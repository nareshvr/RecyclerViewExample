package ducere.lechal.pod;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by VR Naresh on 14-05-2016.
 */
public class DownloadMapActivity extends AppCompatActivity implements View.OnClickListener {
    ImageView imvMapPopupMenu;
TextView tvNorthAmerica,tvEurope,tvAfrica,tvSouthAmerica,tvOceania,tvAsia;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.download_map_new);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.BLACK);
        setSupportActionBar(toolbar);

        //toolbar.setTitle("Download maps");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Download maps");

        initView();

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.map_popup_menu, menu);
        return true;
    }
    private void initView() {
        /*imvMapPopupMenu = (ImageView) findViewById(R.id.imvMapPopupMenu);
        imvMapPopupMenu.setOnClickListener(this);*/

        CardView cvDownloadMaps = (CardView)findViewById(R.id.cvDownloadMaps);
        cvDownloadMaps.setOnClickListener(this);

        tvNorthAmerica = (TextView)findViewById(R.id.tvNorthAmerica);
        tvNorthAmerica.setOnClickListener(this);

        tvEurope = (TextView)findViewById(R.id.tvEurope);
        tvEurope.setOnClickListener(this);

        tvAfrica = (TextView)findViewById(R.id.tvAfrica);
        tvAfrica.setOnClickListener(this);

        tvSouthAmerica = (TextView)findViewById(R.id.tvSouthAmerica);
        tvSouthAmerica.setOnClickListener(this);

        tvOceania=(TextView)findViewById(R.id.tvOceania);
        tvOceania.setOnClickListener(this);

        tvAsia = (TextView)findViewById(R.id.tvAsia);
        tvAsia.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            /*case R.id.imvMapPopupMenu:
                //Creating the instance of PopupMenu
                PopupMenu popup = new PopupMenu(DownloadMapActivity.this, imvMapPopupMenu);
                popup.getMenuInflater().inflate(R.menu.map_popup_menu, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                       // startActivity(new Intent(DownloadMapActivity.this, DownloadMap.class));
                        return true;
                    }
                });
                popup.show();//showing popup menu
                break;*/
            case R.id.cvDownloadMaps:
                startActivity(new Intent(DownloadMapActivity.this, DownloadMapListActivity.class));
                break;
            case R.id.tvNorthAmerica:
                tvNorthAmerica.setBackground(getResources().getDrawable(R.mipmap.txt_active_nm));
                tvEurope.setBackground(getResources().getDrawable(R.mipmap.txt_block_ep));
                tvAfrica.setBackground(getResources().getDrawable(R.mipmap.txt_block_af));
                tvSouthAmerica.setBackground(getResources().getDrawable(R.mipmap.txt_block_sa));
                tvOceania.setBackground(getResources().getDrawable(R.mipmap.txt_block_on));
                tvAsia.setBackground(getResources().getDrawable(R.mipmap.txt_block_aa));
                break;
            case R.id.tvEurope:
                tvEurope.setBackground(getResources().getDrawable(R.mipmap.txt_active_ep));
                tvNorthAmerica.setBackground(getResources().getDrawable(R.mipmap.txt_block_nm));
                tvAfrica.setBackground(getResources().getDrawable(R.mipmap.txt_block_af));
                tvSouthAmerica.setBackground(getResources().getDrawable(R.mipmap.txt_block_sa));
                tvOceania.setBackground(getResources().getDrawable(R.mipmap.txt_block_on));
                tvAsia.setBackground(getResources().getDrawable(R.mipmap.txt_block_aa));
                break;
            case R.id.tvAfrica:
                tvAfrica.setBackground(getResources().getDrawable(R.mipmap.txt_active_af));
                tvEurope.setBackground(getResources().getDrawable(R.mipmap.txt_block_ep));
                tvNorthAmerica.setBackground(getResources().getDrawable(R.mipmap.txt_block_nm));
                tvSouthAmerica.setBackground(getResources().getDrawable(R.mipmap.txt_block_sa));
                tvOceania.setBackground(getResources().getDrawable(R.mipmap.txt_block_on));
                tvAsia.setBackground(getResources().getDrawable(R.mipmap.txt_block_aa));
                break;
            case R.id.tvSouthAmerica:
                tvSouthAmerica.setBackground(getResources().getDrawable(R.mipmap.txt_active_sa));
                tvAfrica.setBackground(getResources().getDrawable(R.mipmap.txt_block_af));
                tvEurope.setBackground(getResources().getDrawable(R.mipmap.txt_block_ep));
                tvNorthAmerica.setBackground(getResources().getDrawable(R.mipmap.txt_block_nm));
                tvAsia.setBackground(getResources().getDrawable(R.mipmap.txt_block_aa));
                tvOceania.setBackground(getResources().getDrawable(R.mipmap.txt_block_on));
                break;
            case R.id.tvOceania:
                tvOceania.setBackground(getResources().getDrawable(R.mipmap.txt_active_on));
                tvSouthAmerica.setBackground(getResources().getDrawable(R.mipmap.txt_block_sa));
                tvAfrica.setBackground(getResources().getDrawable(R.mipmap.txt_block_af));
                tvEurope.setBackground(getResources().getDrawable(R.mipmap.txt_block_ep));
                tvNorthAmerica.setBackground(getResources().getDrawable(R.mipmap.txt_block_nm));
                tvAsia.setBackground(getResources().getDrawable(R.mipmap.txt_block_aa));
                break;
            case R.id.tvAsia:
                tvAsia.setBackground(getResources().getDrawable(R.mipmap.txt_active_aa));
                tvOceania.setBackground(getResources().getDrawable(R.mipmap.txt_block_on));
                tvSouthAmerica.setBackground(getResources().getDrawable(R.mipmap.txt_block_sa));
                tvAfrica.setBackground(getResources().getDrawable(R.mipmap.txt_block_af));
                tvEurope.setBackground(getResources().getDrawable(R.mipmap.txt_block_ep));
                tvNorthAmerica.setBackground(getResources().getDrawable(R.mipmap.txt_block_nm));
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

}

