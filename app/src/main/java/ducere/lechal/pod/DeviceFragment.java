package ducere.lechal.pod;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.poliveira.apps.parallaxlistview.ParallaxScrollView;
import ducere.lechal.pod.ble.ActionsToService;
import ducere.lechal.pod.ble.ServiceBroadcastActions;
import ducere.lechal.pod.constants.Constants;
import ducere.lechal.pod.constants.SharedPrefUtil;
import ducere.lechal.pod.constants.Vibrations;
import ducere.lechal.pod.customViews.CircleProgressView;
import ducere.lechal.pod.dialoges.DialogCheckPodPosition;

public class DeviceFragment extends Fragment implements View.OnClickListener,Vibrations {


    private TextView batteryText;
    private CircleProgressView batteryProgress;
    SharedPreferences defaultSharedPreferences;
    TextView txtShoeType;
    TextView intensityView;
    View view;
    public DeviceFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_pods, container, false);

        defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

        initViews();

        return view;
    }

    private void initViews() {
        ParallaxScrollView mScrollView = (ParallaxScrollView) view.findViewById(R.id.view);
        mScrollView.setParallaxView(getActivity().getLayoutInflater().inflate(R.layout.pods_header, mScrollView, false));

        batteryText = (TextView) view.findViewById(R.id.battery_text);
        batteryProgress = (CircleProgressView) view.findViewById(R.id.battery_progress);
        batteryProgress.setProgress(0);
        batteryProgress.setPaintColor(Color.WHITE);

        view.findViewById(R.id.cwPodsPosition).setOnClickListener(this);

        LinearLayout llIntensity = (LinearLayout) view.findViewById(R.id.llIntensity);
        llIntensity.setOnClickListener(this);

        LinearLayout llTutorials = (LinearLayout) view.findViewById(R.id.llTutorials);
        llTutorials.setOnClickListener(this);

        txtShoeType = (TextView) view.findViewById(R.id.txtShoeType);

        LinearLayout llFootwearType = (LinearLayout) view.findViewById(R.id.llFootwearType);
        llFootwearType.setOnClickListener(this);

        intensityView = (TextView) view.findViewById(R.id.intensity_val);

        int intensityType = SharedPrefUtil.getIntensityType(getContext());
        intensityView.setText(Constants.INTENSITIES[intensityType]);

    }

    @Override
    public void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(getContext()).sendBroadcast(new Intent(ActionsToService.GET_BATTERY));
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
                    if (remainingBattery == -1) {
                        batteryProgress.setProgress(0);
                        batteryText.setText("");
                    } else {
                        batteryProgress.setProgress(remainingBattery);
                        batteryText.setText(remainingBattery + "%");
                    }
                    break;
            }
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.llIntensity:
                startActivity(new Intent(getActivity(),IntensityActivity1.class));
                //showIntensityPicker();
                break;
            case R.id.cwPodsPosition:
                DialogFragment newFragment = new DialogCheckPodPosition();
                newFragment.show(getFragmentManager(), "position");
                break;
            case R.id.llTutorials:
                startActivity(new Intent(getActivity(), VibrationTutorialActivity.class));
                break;
            case R.id.llFootwearType:
                showFootwearDialog();
                break;
        }
    }

    private void showIntensityPicker() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Vibration Intensity");
        int selected = SharedPrefUtil.getIntensityType(getContext()); // does not select anything

        builder.setSingleChoiceItems(Constants.INTENSITIES, selected, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                SharedPrefUtil.setIntensityType(getContext(), which);
                intensityView.setText(Constants.INTENSITIES[which]);
                switch (which) {
                    case 0:
                        Constants.sendFootwear(getContext(), "CD2");
                        break;
                    case 1:
                        Constants.sendFootwear(getContext(), "CD3");
                        break;
                    case 2:
                        Constants.sendFootwear(getContext(), "CD1");
                        break;
                    case 3:
                        Constants.sendFootwear(getContext(), "CD4");
                        break;
                }
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void showFootwearDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Footwear type");
        final CharSequence[] choiceList = {"Casual shoes", "Sports shoes", "Insole", "Ballerina"};

        int selected = SharedPrefUtil.getFootwearType(getContext()); // does not select anything

        builder.setSingleChoiceItems(choiceList, selected, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                SharedPrefUtil.setFootwearType(getContext(), which);
                switch (which) {
                    case 0:
                        txtShoeType.setText(" Casual Shoe ");
                        Constants.sendFootwear(getContext(), "CD2");
                        break;
                    case 1:
                        txtShoeType.setText(" Sports shoes ");
                        Constants.sendFootwear(getContext(), "CD3");
                        break;
                    case 2:
                        txtShoeType.setText(" Insole ");
                        Constants.sendFootwear(getContext(), "CD1");
                        break;
                    case 3:
                        txtShoeType.setText(" Ballerina ");
                        Constants.sendFootwear(getContext(), "CD4");
                        break;

                }
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }
}
