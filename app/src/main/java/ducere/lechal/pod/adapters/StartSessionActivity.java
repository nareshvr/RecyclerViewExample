package ducere.lechal.pod.adapters;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import ducere.lechal.pod.ActivityFragment;
import ducere.lechal.pod.R;
import ducere.lechal.pod.ble.ServiceBroadcastActions;
import ducere.lechal.pod.sqlite.SaveSessionDialog;


/**
 * Created by VR Naresh on 04-05-2016.
 */
public class StartSessionActivity extends FragmentActivity implements View.OnClickListener{
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.startserrion);
        CardView cwStop = (CardView) findViewById(R.id.cwStop);
        cwStop.setOnClickListener(this);
        ImageView imgBatteryStatus  = (ImageView)findViewById(R.id.imgBatteryStatus);
        imgBatteryStatus.setOnClickListener(this);
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
            case R.id.imgBatteryStatus:

                break;
        }
    }
}
