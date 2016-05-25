package ducere.lechal.pod;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Chronometer;
import android.widget.ImageView;

import ducere.lechal.pod.ble.ServiceBroadcastActions;
import ducere.lechal.pod.customViews.CircularSeekBar;
import ducere.lechal.pod.podsdata.FitnessData;
import ducere.lechal.pod.podsdata.Session;
import ducere.lechal.pod.dialoges.SaveSessionDialog;
import np.TextView;


/**
 * Created by VR Naresh on 04-05-2016.
 */
public class StartSessionActivity extends AppCompatActivity implements View.OnClickListener {

    Chronometer chronometer;
    TextView tvHour, tvMin, tvSec, tvSteps, tvCal;
    android.support.design.widget.FloatingActionButton fabPause;
    Session session;
    CircularSeekBar circularSeekBar;
    ImageView imgBatteryView;
    int remainingBattery;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_session);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Running session");

        initView();

    }

    private void initView() {
        session = getIntent().getParcelableExtra("session");
        CardView cwStop = (CardView) findViewById(R.id.cwStop);
        tvHour = (TextView) findViewById(R.id.txtHours);
        tvMin = (TextView) findViewById(R.id.txtMin);
        tvSec = (TextView) findViewById(R.id.txtSec);
        tvCal = (TextView) findViewById(R.id.tvCal);
        tvSteps = (TextView) findViewById(R.id.tvSteps);
        circularSeekBar = (CircularSeekBar) findViewById(R.id.progress);
        circularSeekBar.setMax(session.getGoalValue());
        fabPause = (android.support.design.widget.FloatingActionButton) findViewById(R.id.fabPause);

        chronometer = (Chronometer) findViewById(R.id.chronometer);
        chronometer.setFormat("00:%s");
        chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.start();
        cwStop.setOnClickListener(this);
        ImageView imgBatteryStatus = (ImageView) findViewById(R.id.imgBatteryStatus);
        imgBatteryStatus.setOnClickListener(this);
        chronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            public void onChronometerTick(Chronometer cArg) {
                long elapsedMillis = SystemClock.elapsedRealtime() - chronometer.getBase();
                if (elapsedMillis > 3600000L) {
                    chronometer.setFormat("0%s");
                } else {
                    chronometer.setFormat("00:%s");
                }
                String[] time = chronometer.getText().toString().split(":");

                tvHour.setText(time[0]);
                tvMin.setText(time[1]);
                tvSec.setText(time[2]);
            }
        });
        fabPause.setOnClickListener(this);
        imgBatteryView = (ImageView) findViewById(R.id.imgBatteryView);
        imgBatteryView.setVisibility(View.GONE);

    }

    @Override
    public void onStart() {
        super.onStart();
        IntentFilter intentFilter = new IntentFilter(ServiceBroadcastActions.BATTERY);
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, intentFilter);
        IntentFilter filterToday = new IntentFilter(ServiceBroadcastActions.FITNESS_DATA);
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, filterToday);
    }

    @Override
    public void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case ServiceBroadcastActions.BATTERY:
                    int remainingBattery = intent.getIntExtra(ServiceBroadcastActions.BATTERY, 0);
                    Log.d("Battery", "Status::" + remainingBattery);
                    remainingBattery = intent.getIntExtra(ServiceBroadcastActions.BATTERY, 0);

                    /*if (remainingBattery == -1) {
                        batteryProgress.setProgress(0);
                        batteryText.setText("");
                    } else {
                        batteryProgress.setProgress(remainingBattery);
                        batteryText.setText(remainingBattery + "%");
                    }*/

                    break;
                case ServiceBroadcastActions.FITNESS_DATA:
                    FitnessData serializableFit = (FitnessData) intent.getSerializableExtra(ServiceBroadcastActions.FITNESS_DATA);
                    if (serializableFit == null) {
                        return;
                    }
                    tvSteps.setText(serializableFit.getSteps() + " steps taken");
                    tvCal.setText(serializableFit.getCal() + "cal left");
                    circularSeekBar.setProgress((int) serializableFit.getCal());
                    break;
            }
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cwStop:
                DialogFragment dialogFragment = new SaveSessionDialog();
                dialogFragment.show(getSupportFragmentManager(), "Position");
                break;
            case R.id.fabPause:
                chronometer.stop();
            case R.id.imgBatteryStatus:
                Log.d("Battery", "Status::" + remainingBattery);
                if (remainingBattery <= 10) {
                    imgBatteryView.setVisibility(View.VISIBLE);
                    imgBatteryView.setBackgroundDrawable(getResources().getDrawable(R.mipmap.battery_10));
                } else if (remainingBattery <= 20) {
                    imgBatteryView.setVisibility(View.VISIBLE);
                    imgBatteryView.setBackgroundDrawable(getResources().getDrawable(R.mipmap.battery_20));
                } else if (remainingBattery <= 30) {
                    imgBatteryView.setVisibility(View.VISIBLE);
                    imgBatteryView.setBackgroundDrawable(getResources().getDrawable(R.mipmap.battery_30));
                } else if (remainingBattery <= 40) {
                    imgBatteryView.setVisibility(View.VISIBLE);
                    imgBatteryView.setBackgroundDrawable(getResources().getDrawable(R.mipmap.battery_40));
                } else if (remainingBattery <= 50) {
                    imgBatteryView.setVisibility(View.VISIBLE);
                    imgBatteryView.setBackgroundDrawable(getResources().getDrawable(R.mipmap.battery_50));
                } else if (remainingBattery <= 60) {
                    imgBatteryView.setVisibility(View.VISIBLE);
                    imgBatteryView.setBackgroundDrawable(getResources().getDrawable(R.mipmap.battery_60));
                } else if (remainingBattery <= 70) {
                    imgBatteryView.setVisibility(View.VISIBLE);
                    imgBatteryView.setBackgroundDrawable(getResources().getDrawable(R.mipmap.battery_70));
                } else if (remainingBattery <= 80) {
                    imgBatteryView.setVisibility(View.VISIBLE);
                    imgBatteryView.setBackgroundDrawable(getResources().getDrawable(R.mipmap.battery_80));
                } else if (remainingBattery <= 90) {
                    imgBatteryView.setVisibility(View.VISIBLE);
                    imgBatteryView.setBackgroundDrawable(getResources().getDrawable(R.mipmap.battery_90));
                } else if (remainingBattery <= 100) {
                    imgBatteryView.setVisibility(View.VISIBLE);
                    imgBatteryView.setBackgroundDrawable(getResources().getDrawable(R.mipmap.icon_battery_full));
                }
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }
}
