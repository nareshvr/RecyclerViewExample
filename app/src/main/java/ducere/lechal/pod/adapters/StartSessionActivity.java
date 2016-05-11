package ducere.lechal.pod.adapters;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.github.clans.fab.FloatingActionButton;

import ducere.lechal.pod.ActivityFragment;
import ducere.lechal.pod.R;
import ducere.lechal.pod.ble.ServiceBroadcastActions;
import ducere.lechal.pod.sqlite.SaveSessionDialog;
import np.TextView;


/**
 * Created by VR Naresh on 04-05-2016.
 */
public class StartSessionActivity extends AppCompatActivity implements View.OnClickListener{

    Chronometer chronometer;
    TextView tvHour,tvMin,tvSec;
    android.support.design.widget.FloatingActionButton fabPause;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.startserrion);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        CardView cwStop = (CardView) findViewById(R.id.cwStop);
        tvHour = (TextView)findViewById(R.id.txtHours);
        tvMin = (TextView)findViewById(R.id.txtMin);
        tvSec = (TextView)findViewById(R.id.txtSec);
        fabPause = (android.support.design.widget.FloatingActionButton)findViewById(R.id.fabPause);

        chronometer =  (Chronometer)findViewById(R.id.chronometer);
        chronometer.setFormat("00:%s");
        chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.start();
        cwStop.setOnClickListener(this);
        ImageView imgBatteryStatus  = (ImageView)findViewById(R.id.imgBatteryStatus);
        imgBatteryStatus.setOnClickListener(this);
        chronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            public void onChronometerTick(Chronometer cArg) {
                long elapsedMillis = SystemClock.elapsedRealtime() -chronometer.getBase();
                if(elapsedMillis > 3600000L){
                    chronometer.setFormat("0%s");
                }else{
                    chronometer.setFormat("00:%s");
                }
                String[] time = chronometer.getText().toString().split(":");
                tvHour.setText(time[0]);
                tvMin.setText(time[1]);
                tvSec.setText(time[2]);
            }
        });
        fabPause.setOnClickListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        IntentFilter intentFilter = new IntentFilter(ServiceBroadcastActions.BATTERY);
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, intentFilter);
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
                    Log.d("Battery","Status::"+remainingBattery);
                    /*if (remainingBattery == -1) {
                        batteryProgress.setProgress(0);
                        batteryText.setText("");
                    } else {
                        batteryProgress.setProgress(remainingBattery);
                        batteryText.setText(remainingBattery + "%");
                    }*/

                    break;
            }
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.cwStop:
                DialogFragment dialogFragment = new SaveSessionDialog();
                dialogFragment.show(getSupportFragmentManager(),"Position");
                break;
            case R.id.fabPause:
                chronometer.stop();
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }
}
