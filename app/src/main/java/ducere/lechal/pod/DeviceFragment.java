package ducere.lechal.pod;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.poliveira.apps.parallaxlistview.ParallaxScrollView;

import ducere.lechal.pod.ble.ServiceBroadcastActions;
import ducere.lechal.pod.customViews.CircleProgressView;


public class DeviceFragment extends Fragment {

    private TextView batteryText;
    private CircleProgressView batteryProgress;

    public DeviceFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pods, container, false);
        ParallaxScrollView mScrollView = (ParallaxScrollView) view.findViewById(R.id.view);
        mScrollView.setParallaxView(getActivity().getLayoutInflater().inflate(R.layout.pods_header, mScrollView, false));
        batteryText = (TextView) view.findViewById(R.id.battery_text);
        batteryProgress = (CircleProgressView) view.findViewById(R.id.battery_progress);
        batteryProgress.setProgress(0);
        batteryProgress.setPaintColor(Color.WHITE);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        IntentFilter intentFilter = new IntentFilter(ServiceBroadcastActions.BATTERY);

        LocalBroadcastManager.getInstance(getContext()).registerReceiver(broadcastReceiver, intentFilter);
    }

    @Override
    public void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(broadcastReceiver);
    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case ServiceBroadcastActions.BATTERY:
                    int remainingBattery = intent.getIntExtra(ServiceBroadcastActions.BATTERY, 0);
                    batteryProgress.setProgress(remainingBattery);
                    batteryText.setText(remainingBattery + "%");
                    break;
            }
        }
    };
}
