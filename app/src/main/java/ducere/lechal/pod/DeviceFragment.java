package ducere.lechal.pod;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.github.seanzor.prefhelper.SharedPrefHelper;
import com.poliveira.apps.parallaxlistview.ParallaxScrollView;
import ducere.lechal.pod.ble.ServiceBroadcastActions;
import ducere.lechal.pod.constants.Constants;
import ducere.lechal.pod.constants.Vibrations;
import ducere.lechal.pod.customViews.CircleProgressView;


public class DeviceFragment extends Fragment implements View.OnClickListener{

    private TextView batteryText;
    private CircleProgressView batteryProgress;
    SharedPrefHelper mPref;
    SharedPreferences defaultSharedPreferences;
    Vibrations vib;
    TextView txtShoeType;
    View view;
    Context context = getContext();
    AlertDialog levelDialog;
    public DeviceFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       // if(view!=null) {
            view = inflater.inflate(R.layout.fragment_pods, container, false);
            vib = new Vibrations();
            defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
            mPref = new SharedPrefHelper(getResources(), defaultSharedPreferences);
            ParallaxScrollView mScrollView = (ParallaxScrollView) view.findViewById(R.id.view);
            mScrollView.setParallaxView(getActivity().getLayoutInflater().inflate(R.layout.pods_header, mScrollView, false));
            batteryText = (TextView) view.findViewById(R.id.battery_text);
            batteryProgress = (CircleProgressView) view.findViewById(R.id.battery_progress);
            batteryProgress.setProgress(0);
            batteryProgress.setPaintColor(Color.WHITE);
            CardView cwPodsPosition = (CardView) view.findViewById(R.id.cwPodsPosition);
            cwPodsPosition.setOnClickListener(this);
            LinearLayout llIntensity = (LinearLayout) view.findViewById(R.id.llIntensity);
            llIntensity.setOnClickListener(this);
            LinearLayout llTutorials = (LinearLayout) view.findViewById(R.id.llTutorials);
            llTutorials.setOnClickListener(this);
            txtShoeType = (TextView) view.findViewById(R.id.txtShoeType);
            LinearLayout llFootwearType = (LinearLayout) view.findViewById(R.id.llFootwearType);
            llFootwearType.setOnClickListener(this);
        //}
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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.llIntensity:
                //startActivity(new Intent(getActivity(),IntensityActity.class));
                showIntensityDialog();
                break;
            case R.id.cwPodsPosition:
                checkPodPositionDialog();
                break;
            case R.id.llTutorials:
                startActivity(new Intent(getActivity(),VibrationTutorialActivity.class));
                break;
            case R.id.llFootwearType:
                showFootwearDialog();
                break;

        }
    }

    private void showIntensityDialog() {
        // Strings to Show In Dialog with Radio Buttons
        final CharSequence[] items = {" Very High "," High "," Medium "," Low "," Very Low "};

        // Creating and Building the Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Vibration Intensity Levels");
        builder.setSingleChoiceItems(items, Math.abs(mPref.getInt(R.string.vibintensity,5)-5), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                switch(item)
                {
                    case 0:
                        // Your code when first option seletced
                        mPref.commitInt(R.string.vibintensity,5);
                        //tvVibINtensity.setText(" Very High ");
                        // BLEMS.sendVibM1("VI", "5");
                        Constants.sendIntensity(context,"VI5");
                        break;
                    case 1:
                        // Your code when 2nd  option seletced
                        mPref.commitInt(R.string.vibintensity,4);
                        //tvVibINtensity.setText(" High ");
                        //BLEMS.sendVibM1("VI", "4");
                        Constants.sendIntensity(context,"VI4");

                        break;
                    case 2:
                        // Your code when 3rd option seletced
                        mPref.commitInt(R.string.vibintensity,3);
                        // tvVibINtensity.setText(" Medium ");
                        //BLEMS.sendVibM1("VI","3");
                        Constants.sendIntensity(context,"VI3");
                        break;
                    case 3:
                        // Your code when 3rd option seletced
                        mPref.commitInt(R.string.vibintensity,2);
                        //tvVibINtensity.setText(" Low ");
                        // BLEMS.sendVibM1("VI","2");
                        Constants.sendIntensity(context,"VI2");
                        break;
                    case 4:
                        // Your code when 3rd option seletced
                        mPref.commitInt(R.string.vibintensity,1);
                        //tvVibINtensity.setText(" Very Low ");
                        //BLEMS.sendVibM1("VI","1");
                        Constants.sendIntensity(context,"VI1");
                        break;

                }
                levelDialog.dismiss();
            }
        });

        levelDialog = builder.create();
        levelDialog.show();

    }

    private void checkPodPositionDialog() {
        // custom dialog
        final Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.check_pods_position);
        // set the custom dialog components - text, image and button
        final ImageView leftPod = (ImageView) dialog.findViewById(R.id.leftPod);
        leftPod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPref.getBoolean(R.string.swap, false)) {
                    //BLEMS.sendVibM1("VB", "0001");
                    Constants.sendVibrationLeft(getContext(),"VB0001");
                } else {
                    Constants.sendVibrationRight(getContext(),"VB0100");
                }
                new CountDownTimer(1000, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                    }
                    @Override
                    public void onFinish() {
                        //leftPod.setBackgroundResource(R.drawable.pod_position_left0);
                    }
                }.start();
            }
        });

        final ImageView rightPod = (ImageView)dialog.findViewById(R.id.rightPod);
        rightPod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mPref.getBoolean(R.string.swap,false)){
                    Constants.sendVibrationLeft(getContext(),"VB0100");

                }else{
                    Constants.sendVibrationRight(getContext(),"VB0001");
                }
                // rightPod.setBackgroundResource(R.drawable.pod_position_right);
                new CountDownTimer(1000, 1000) {

                    @Override
                    public void onTick(long millisUntilFinished) {

                    }

                    @Override
                    public void onFinish() {
                        //rightPod.setBackgroundResource(R.drawable.pod_position_right0);
                    }
                }.start();
            }
        });

        ImageView swapPods = (ImageView)dialog.findViewById(R.id.podSwap);
        swapPods.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mPref.getBoolean(R.string.swap, false)) {
                    mPref.commitBoolean(R.string.swap, false);
                    // BLEMS.sendVibM1("VB", "0100");

                } else {
                    mPref.commitBoolean(R.string.swap, true);
                    //BLEMS.sendVibM1("VB", "0001");

                }
                //swapPods.setBackgroundResource(R.drawable.pod_position_swap);
                new CountDownTimer(1000, 1000) {

                    @Override
                    public void onTick(long millisUntilFinished) {

                    }

                    @Override
                    public void onFinish() {
                        //swapPods.setBackgroundResource(R.drawable.pod_position_swap0);
                    }
                }.start();
                Toast.makeText(getActivity(),"Swapped.",Toast.LENGTH_SHORT).show();
            }
        });
        TextView txtok = (TextView)dialog.findViewById(R.id.txtOK);
        txtok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });

        dialog.show();
    }

    private void showFootwearDialog() {


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Footwear type");

        final CharSequence[] choiceList = {"Casual shoes", "Sports shoes" , "Insole" , "Ballerina" };

        int selected = -1; // does not select anything
        builder.setSingleChoiceItems(choiceList, selected, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getContext(), "Select "+choiceList[which], Toast.LENGTH_SHORT).show();
                switch (which){
                    case 0:
                        mPref.commitInt(R.string.shoe_type,0);
                        txtShoeType.setText(" Casual Shoe ");
                        // BLEMS.writeData("CD2");
                        Constants.sendFootwear(getContext(),"CD2");
                        break;
                    case 1:
                        mPref.commitInt(R.string.shoe_type,1);
                        txtShoeType.setText(" Sports shoes ");
                        // BLEMS.writeData("CD2");
                        Constants.sendFootwear(getContext(),"CD3");
                        break;
                    case 2:
                        mPref.commitInt(R.string.shoe_type,2);
                        txtShoeType.setText(" Insole ");
                        // BLEMS.writeData("CD2");
                        Constants.sendFootwear(getContext(),"CD1");
                        break;
                    case 3:
                        mPref.commitInt(R.string.shoe_type,3);
                        txtShoeType.setText(" Ballerina ");
                        // BLEMS.writeData("CD2");
                        Constants.sendFootwear(getContext(),"CD4");
                        break;

                }
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }
}
