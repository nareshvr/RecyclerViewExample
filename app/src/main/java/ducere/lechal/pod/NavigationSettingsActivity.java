package ducere.lechal.pod;

import android.content.Intent;
import android.os.Bundle;

import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;

import ducere.lechal.pod.dialoges.DialogClearHistory;
import ducere.lechal.pod.dialoges.DialogDayNight;
import ducere.lechal.pod.dialoges.DialogMapUnitsDialog;
import ducere.lechal.pod.dialoges.DialogRoutePerference;

/**
 * Created by VR Naresh on 12-05-2016.
 */
public class NavigationSettingsActivity extends AppCompatActivity implements View.OnClickListener{

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_settings);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initViews();

    }

    private void initViews() {
        LinearLayout llMapUnits = (LinearLayout)findViewById(R.id.llMapUnits);
        llMapUnits.setOnClickListener(this);

        LinearLayout llDayNight = (LinearLayout)findViewById(R.id.llDayNight);
        llDayNight.setOnClickListener(this);

        LinearLayout llVoiceOptions = (LinearLayout)findViewById(R.id.llVoiceOptions);
        llVoiceOptions.setOnClickListener(this);

        LinearLayout llRoutePerference  = (LinearLayout)findViewById(R.id.llRoutePerference);
        llRoutePerference.setOnClickListener(this);

        LinearLayout llClearHistory = (LinearLayout)findViewById(R.id.llClearHistory);
        llClearHistory.setOnClickListener(this);

        LinearLayout llDownLoadMaps = (LinearLayout)findViewById(R.id.llDownLoadMaps);
        llDownLoadMaps.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.llMapUnits:
                DialogFragment dialogFragment = new DialogMapUnitsDialog();
                dialogFragment.show(getSupportFragmentManager(),"Position");
                break;
            case R.id.llDayNight:
                DialogFragment daynight = new DialogDayNight();
                daynight.show(getSupportFragmentManager(),"Position");
                break;
            case R.id.llVoiceOptions:
                startActivity(new Intent(NavigationSettingsActivity.this,VoiceOptionActivity.class));
                break;
            case R.id.llRoutePerference:
                DialogFragment routepreference = new DialogRoutePerference();
                routepreference.show(getSupportFragmentManager(),"Position");
                break;
            case R.id.llClearHistory:
                DialogFragment clearhistory = new DialogClearHistory();
                clearhistory.show(getSupportFragmentManager(),"Position");
                break;
            case R.id.llDownLoadMaps:
                startActivity(new Intent(NavigationSettingsActivity.this,DownloadMapActivity.class));
                break;

        }
    }
}
