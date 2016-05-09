package ducere.lechal.pod;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.poliveira.apps.parallaxlistview.ParallaxScrollView;

import java.io.BufferedReader;
import java.io.File;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import ducere.lechal.pod.adapters.StartSessionDialog;
import ducere.lechal.pod.ble.ActionsToService;
import ducere.lechal.pod.ble.ServiceBroadcastActions;
import ducere.lechal.pod.podsdata.FitnessData;

/**
 * Created by VR Naresh on 03-05-2016.
 */
public class FitnessFragment extends Fragment implements View.OnClickListener {
    private View view;
    ImageView imgMenuPop;
    private final int value = 000000;
    private Flipmeter flipMeter = null;
    String str = "01801";
    int seekBarValue = 50;
    TextView txtDate;
    int mYear, mDay;
    String mMonth;
    Calendar datecal;
    int decrementdate = 1;
    int increment = 1;
    long steps;

    public FitnessFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_fitness, container, false);
        ParallaxScrollView mScrollView = (ParallaxScrollView) view.findViewById(R.id.view1);
        mScrollView.setParallaxView(getActivity().getLayoutInflater().inflate(R.layout.fitnes_header, mScrollView, false));


        flipMeter = (Flipmeter) view.findViewById(R.id.Flipmeter);
        ProgressBar seekBarFitness = (ProgressBar) view.findViewById(R.id.seekBarFitness);
        seekBarFitness.setProgress(seekBarValue);
        flipMeter.setValue(Integer.parseInt(str), true);
        txtDate = (TextView) view.findViewById(R.id.txtDate);
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ENGLISH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        updateDisplay();
        Calendar cal = Calendar.getInstance();
        System.out.println("Current time => " + cal.getTime());

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        final Date date = cal.getTime();
        final String formattedDate = df.format(cal.getTime());
        datecal = Calendar.getInstance();
        ImageView imgLeftArrow = (ImageView) view.findViewById(R.id.imgLeftArrow);
        imgLeftArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                incrementDateByOne(date);
                mYear = datecal.get(Calendar.YEAR);
                mMonth = datecal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ENGLISH);
                mDay = datecal.get(Calendar.DAY_OF_MONTH);
                txtDate.setText(new StringBuilder().append(mDay).append(" ").append(mMonth).append(",").append(mYear).append(" "));
                decrementdate++;
            }
        });
        ImageView imgrightArrow = (ImageView) view.findViewById(R.id.imgrightArrow);
        imgrightArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActionsToService.FITNESS_TODAY_DATA);
                LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intent);
                datecal = Calendar.getInstance();
                incrementDateByOne(date);
                mYear = datecal.get(Calendar.YEAR);
                mMonth = datecal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ENGLISH);
                mDay = datecal.get(Calendar.DAY_OF_MONTH);
                txtDate.setText(new StringBuilder().append(mDay).append(" ").append(mMonth).append(",").append(mYear).append(" "));
                decrementdate--;
            }
        });

        imgMenuPop = (ImageView) view.findViewById(R.id.imgMenupop);
        imgMenuPop.setOnClickListener(this);
        LinearLayout llStartSession = (LinearLayout) view.findViewById(R.id.llStartSession);
        llStartSession.setOnClickListener(this);

        return view;
    }

   /* @Override
    public void onResume() {
        super.onResume();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

            }
        }, 5000);
    }*/

    @Override
    public void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mMessageReceiver, new IntentFilter(ServiceBroadcastActions.FITNESS_TODAY_DATA));
    }

    @Override
    public void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mMessageReceiver);
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (ServiceBroadcastActions.FITNESS_TODAY_DATA.equals(action)) {
                FitnessData serializable = (FitnessData) intent.getSerializableExtra(ServiceBroadcastActions.FITNESS_TODAY_DATA);
                if (serializable == null) {
                    return;
                }
                steps = serializable.getSteps();
                Log.i("FitnessData", "Today Steps::" + steps);
                long cal = serializable.getCal();

            }

        }
    };

    public Date decrementDateByOne(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE, -decrementdate);
        Date previousDate = c.getTime();
        return previousDate;
    }

    public Date incrementDateByOne(Date date) {
        datecal.setTime(date);
        datecal.add(Calendar.DATE, -decrementdate);
        Date nextDate = datecal.getTime();
        return nextDate;
    }

    private void updateDisplay() {
        txtDate.setText(
                new StringBuilder()
                        // Month is 0 based so add 1

                        .append(mDay).append(" ")
                        .append(mMonth).append(",")
                        .append(mYear).append(" "));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgMenupop:
                popMenuDialog();
                break;
            case R.id.llStartSession:
                DialogFragment activityFragment = new StartSessionDialog();
                activityFragment.show(getFragmentManager(), "Position");
                break;
        }
    }

    private void popMenuDialog() {
        //Creating the instance of PopupMenu
        PopupMenu popup = new PopupMenu(getActivity(), imgMenuPop);
        //Inflating the Popup using xml file
        popup.getMenuInflater().inflate(R.menu.fitness_popup_menu, popup.getMenu());

        //registering popup with OnMenuItemClickListener
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {

                switch (item.getItemId()){
                    case R.id.dailyGoals:
                        startActivity(new Intent(getActivity(),EditDailySession.class));
                        break;
                    case R.id.shareCompletedSteps:
                        showSocailMedia();
                        break;
                    case R.id.viewSavedSession:
                        break;
                }

                return true;
            }
        });

        popup.show();//showing popup menu
    }

    private void showSocailMedia() {
        // specify our test image location
        Uri url = Uri.parse("android.resource://"
                + getActivity().getPackageName() + "/" + R.drawable.ic_menu_camera);

        // set up an intent to share the image
        Intent share_intent = new Intent();
        share_intent.setAction(Intent.ACTION_SEND);
        share_intent.setType("image/png");
        share_intent.putExtra(Intent.EXTRA_STREAM,
                Uri.fromFile(new File(url.toString())));
        share_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        share_intent.putExtra(Intent.EXTRA_SUBJECT,
                "share an image");
        share_intent.putExtra(Intent.EXTRA_TEXT,
                "This is an image to share with you");

        // start the intent
        try {
            startActivity(Intent.createChooser(share_intent,
                    "ShareThroughChooser Test"));
        } catch (android.content.ActivityNotFoundException ex) {
            (new AlertDialog.Builder(getActivity())
                    .setMessage("Share failed")
                    .setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int whichButton) {
                                }
                            }).create()).show();
        }
    }


}