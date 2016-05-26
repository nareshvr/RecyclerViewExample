package ducere.lechal.pod;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;

/**
 * Created by VR Naresh on 24-05-2016.
 */
public class DownlaodMapNew extends AppCompatActivity implements View.OnClickListener {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_map);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.BLACK);
        setSupportActionBar(toolbar);

        //toolbar.setTitle("Download maps");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Download maps");

        CardView cvDownloadMaps = (CardView)findViewById(R.id.cvDownloadMaps);
        cvDownloadMaps.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.cvDownloadMaps:
                startActivity(new Intent(DownlaodMapNew.this, DownloadMapActivity.class));
                break;
        }
    }
}
