package ducere.lechal.pod.adapters;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.LocationManager;
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
import android.view.Window;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.here.android.mpa.common.GeoPosition;
import com.here.android.mpa.common.Image;
import com.here.android.mpa.common.MapEngine;
import com.here.android.mpa.common.OnEngineInitListener;
import com.here.android.mpa.common.PositioningManager;
import com.here.android.mpa.mapping.Map;
import com.here.android.mpa.mapping.MapContainer;
import com.here.android.mpa.mapping.MapFragment;
import com.here.android.mpa.mapping.MapMarker;
import com.here.android.mpa.mapping.MapTransitLayer;

import java.io.IOException;
import java.lang.ref.WeakReference;

import ducere.lechal.pod.ActivityFragment;
import ducere.lechal.pod.LechalApplication;
import ducere.lechal.pod.R;
import ducere.lechal.pod.ble.ActionsToService;
import ducere.lechal.pod.ble.ServiceBroadcastActions;
import ducere.lechal.pod.constants.Convert;
import ducere.lechal.pod.customViews.CircularSeekBar;
import ducere.lechal.pod.customViews.PausableChronometer;
import ducere.lechal.pod.podsdata.FitnessData;
import ducere.lechal.pod.podsdata.Session;
import ducere.lechal.pod.sqlite.SaveSessionDialog;
import ducere.lechal.pod.utilities.NavigationFeedback;
import np.TextView;


/**
 * Created by VR Naresh on 04-05-2016.
 */
public class StartSessionActivity extends AppCompatActivity implements View.OnClickListener{

    PausableChronometer chronometer;
    TextView tvHour,tvMin,tvSec,tvSteps,tvCal,tvPercentage;
    android.support.design.widget.FloatingActionButton fabPause;
    Session session;
    ImageView ivMode;
    ImageView imgBatteryStatus,ivMap;
    CardView cwStop;
    RelativeLayout rlTop;
    FitnessData fitnessDataInitial=null;
    float steps=0,cal=0,dist=0;
    CircularSeekBar circularSeekBar;
    int CALORIES=1,STEPS=2,DISTANCE=3,TIME=4;
    int WALK=1,RUN=2,CYCLE=3;
    boolean pause=false;
    int batteryStatus=0;
    public boolean isMapEngineInitialize = false;
    // map embedded in the map fragment
    public static Map map = null;
    // map fragment embedded in this activity
    private MapFragment mapFragment = null;

    PositioningManager positioningManager;
    public static MapContainer mapcontainer = null;
    public static MapMarker mapmarker = null;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.startserrion);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Running session");

        session = (Session) getIntent().getSerializableExtra("session");
        cwStop = (CardView) findViewById(R.id.cwStop);
        tvHour = (TextView)findViewById(R.id.txtHours);
        tvMin = (TextView)findViewById(R.id.txtMin);
        tvSec = (TextView)findViewById(R.id.txtSec);
        tvCal = (TextView)findViewById(R.id.tvCal);
        tvSteps = (TextView)findViewById(R.id.tvSteps);
        rlTop = (RelativeLayout)findViewById(R.id.rlTop);
        tvPercentage = (TextView)findViewById(R.id.tvPercentage);
        circularSeekBar = (CircularSeekBar)findViewById(R.id.progress);
        circularSeekBar.setMax(session.getGoalValue());
        ivMode = (ImageView)findViewById(R.id.ivMode);
        fabPause = (android.support.design.widget.FloatingActionButton)findViewById(R.id.fabPause);
        imgBatteryStatus  = (ImageView)findViewById(R.id.imgBatteryStatus);
        ivMap  = (ImageView)findViewById(R.id.imgMap);
        chronometer =  (PausableChronometer)findViewById(R.id.chronometer);

        chronometer.setFormat("00:%s");
        chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.start();

        imgBatteryStatus.setOnClickListener(this);
        ivMap.setOnClickListener(this);
        cwStop.setOnClickListener(this);
        if (session.getActivityType()==WALK){
            ivMode.setBackgroundResource(R.mipmap.ic_walk_red_circle);
        }else if (session.getActivityType()==RUN){
            ivMode.setBackgroundResource(R.mipmap.ic_run_red_circle);
        }else if (session.getActivityType()==CYCLE){
            ivMode.setBackgroundResource(R.mipmap.ic_cycle_red_circle);
        }

        initMapEngine();
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
                    batteryStatus = intent.getIntExtra(ServiceBroadcastActions.BATTERY, 0);
                    Log.d("Battery","Status::"+batteryStatus);
                    setBatteryStatus(batteryStatus);
                    break;
                case ServiceBroadcastActions.FITNESS_DATA:
                    FitnessData serializableFit = (FitnessData) intent.getSerializableExtra(ServiceBroadcastActions.FITNESS_DATA);
                    if (serializableFit == null) {
                        return;
                    }
                    if (fitnessDataInitial==null)
                        fitnessDataInitial = serializableFit;
                    else
                        updateViews(serializableFit);
                    break;
            }
        }
    };
    void updateViews( FitnessData serializableFit){
       steps = serializableFit.getSteps()-fitnessDataInitial.getSteps();
        cal = serializableFit.getCal()-fitnessDataInitial.getCal();
        dist = serializableFit.getDistance()-fitnessDataInitial.getDistance();
        float max = circularSeekBar.getMax();
        if(session.getGoalType()==CALORIES){
            tvSteps.setText(steps + " steps taken");
            tvCal.setText(session.getCalories()-cal + "cal left");
            circularSeekBar.setProgress((int) cal);
            int percentage = (int)( cal/max)*100;
            tvPercentage.setText(percentage + "%");
        }else if(session.getGoalType()==STEPS){
            tvSteps.setText(cal + " cal burnt");
            tvCal.setText(session.getSteps()-steps + " steps left");
            circularSeekBar.setProgress((int) steps);
            int percentage = (int)(steps/max)*100;
            tvPercentage.setText(percentage + "%");
        }else if(session.getGoalType()==DISTANCE){
            tvSteps.setText(cal + " cal burnt");
            tvCal.setText(Convert.metersToKms(session.getDistance()-dist )+ " left");
            circularSeekBar.setProgress((int) dist);
            int percentage = (int)( dist/max)*100;
            tvPercentage.setText(percentage+"%");
        }else if(session.getGoalType()==TIME){
            tvSteps.setText(serializableFit.getSteps() + " steps taken");
            tvCal.setText(session.getCalories()-serializableFit.getCal() + "cal left");
            circularSeekBar.setProgress((int) serializableFit.getCal());
            int percentage = (int)( cal/max)*100;
            tvPercentage.setText(percentage+"%");
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.cwStop:
                chronometer.stop();
                session.setCalories((int) cal);
                session.setDistance(Math.round(dist));
                session.setSteps(Math.round(steps));
                session.setEndTime(System.currentTimeMillis());
                session.setSync(false);
                session.setStatus(0);
                DialogFragment dialogFragment = new SaveSessionDialog();
                dialogFragment.show(getSupportFragmentManager(),"Position");
                break;
            case R.id.fabPause:
                if (pause==false){
                    pause=true;
                    chronometer.stop();
                    fabPause.setImageResource(R.drawable.icon_play);
                }else{
                    pause=false;
                    chronometer.start();
                    fabPause.setImageResource(R.mipmap.icon_pause);
                }
                break;
            case R.id.imgBatteryStatus:
               showBatteryDialog(batteryStatus);
                break;
            case R.id.imgMap:
                if (rlTop.getVisibility()==View.VISIBLE){
                    rlTop.setVisibility(View.GONE);
                }else{
                    rlTop.setVisibility(View.VISIBLE);
                }
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            showEndDialog();
        }

        return super.onOptionsItemSelected(item);
    }
    void setBatteryStatus(int status){

        if (status <= 10) {
            imgBatteryStatus.setBackgroundResource(R.drawable.battery_10);
        } else if (status <= 20) {
            imgBatteryStatus.setBackgroundResource(R.drawable.battery_20);
        } else if (status <= 30) {
            imgBatteryStatus.setBackgroundResource(R.drawable.battery_30);
        } else if (status <= 40) {
            imgBatteryStatus.setBackgroundResource(R.drawable.battery_40);
        } else if (status <= 50) {
            imgBatteryStatus.setBackgroundResource(R.drawable.battery_50);
        } else if (status <= 60) {
            imgBatteryStatus.setBackgroundResource(R.drawable.battery_60);
        } else if (status <= 70) {
            imgBatteryStatus.setBackgroundResource(R.drawable.battery_70);
        } else if (status <= 80) {
            imgBatteryStatus.setBackgroundResource(R.drawable.battery_80);
        } else if (status <= 90) {
            imgBatteryStatus.setBackgroundResource(R.drawable.battery_90);
        } else if (status >= 90) {
            imgBatteryStatus.setBackgroundResource(R.drawable.icon_battery_full);
        }

    }
    void showBatteryDialog(int status){
        final Dialog dialog = new Dialog(StartSessionActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.battery_alert);

        TextView tvPercentage = (TextView) dialog.findViewById(R.id.tvPercentage);
        CircularSeekBar progress = (CircularSeekBar)dialog.findViewById(R.id.progress);
        progress.setMax(100);
        progress.setProgress(status);
        tvPercentage.setText(status+"%");

        dialog.show();
    }
    public void initMapEngine() {
        final MapEngine mapEngine = MapEngine.getInstance(getApplicationContext());
        mapEngine.init(this, new OnEngineInitListener() {
            @Override
            public void onEngineInitializationCompleted(Error error) {
                // TODO Auto-generated method stub
                if (error == OnEngineInitListener.Error.NONE) {
                    isMapEngineInitialize = true;
                    initMap();
                    //MapEngine.setOnline(false);
                } else {
                    // handle factory initialization failure
                    Toast.makeText(StartSessionActivity.this, "cannot initialize map engine : " + error.toString() + "", Toast.LENGTH_SHORT).show();
                    isMapEngineInitialize = false;
                }
            }
        });
    }

    public void initMap() {
        // SearchActivity for the map fragment to finish setup by calling init().
        mapFragment = (com.here.android.mpa.mapping.MapFragment) (getFragmentManager().findFragmentById(
                R.id.mapfragment));

        mapFragment.init(new OnEngineInitListener() {
            @Override
            public void onEngineInitializationCompleted(
                    OnEngineInitListener.Error error) {
                if (error == OnEngineInitListener.Error.NONE) {
                    map = mapFragment.getMap();

                    // Set the zoom level to the average between min and max
                    map.setZoomLevel(25);

                    //set the map projection
                    map.setProjectionMode(Map.Projection.GLOBE);


                    positioningManager = PositioningManager.getInstance();
                    positioningManager.addListener(
                            new WeakReference<PositioningManager.OnPositionChangedListener>(positionListener));
                    positioningManager.start(PositioningManager.LocationMethod.GPS_NETWORK);
                    LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                    if (lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                        if (positioningManager.hasValidPosition()) {
                            map.setCenter(positioningManager.getPosition().getCoordinate(),
                                    Map.Animation.LINEAR);

                        } else if (positioningManager.getLastKnownPosition().isValid()) {
                            map.setCenter(positioningManager.getLastKnownPosition().getCoordinate(),
                                    Map.Animation.LINEAR);

                        } else {
                            Toast.makeText(StartSessionActivity.this, "Waiting for Gps Fix", Toast.LENGTH_SHORT).show();
                        }
                    } else {

                    }

                    // Display position indicator
                    map.getPositionIndicator().setVisible(true);

                    Image myImage = new com.here.android.mpa.common.Image();
                    try {
                        myImage.setImageResource(R.drawable.ic_marker_location_lechal);
                    } catch (IOException e) {
                        Log.d("Exception", e.toString() + "");
                    }

                    map.getPositionIndicator().setMarker(myImage);
                    map.getPositionIndicator().setAccuracyIndicatorVisible(true);
                    // Listen for gesture events. For example tapping on buildings


                    map.getMapTransitLayer().setMode(MapTransitLayer.Mode.EVERYTHING);


                } else {
                    //gps.showSettingsAlert();
                    //Log.d("gps", "4");
                    Toast.makeText(StartSessionActivity.this, "Cannot initialize map fragment : " +
                            error.toString(), Toast.LENGTH_SHORT).show();
                    isMapEngineInitialize = false;
                    initMapEngine();
                }
            }

        });
    }
    // Define positioning listener
    private PositioningManager.OnPositionChangedListener positionListener = new
            PositioningManager.OnPositionChangedListener() {

                @Override
                public void onPositionUpdated(PositioningManager.LocationMethod locationMethod, final GeoPosition geoPosition, boolean b) {

                }

                @Override
                public void onPositionFixChanged(PositioningManager.LocationMethod locationMethod, PositioningManager.LocationStatus locationStatus) {

                }
            };
    void showEndDialog() {
        final Dialog dialog = new Dialog(StartSessionActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_layout);

        android.widget.TextView title = (android.widget.TextView) dialog.findViewById(R.id.tvTitle);
        android.widget.TextView description = (android.widget.TextView) dialog.findViewById(R.id.tvDescription);
        android.widget.TextView cancel = (android.widget.TextView) dialog.findViewById(R.id.tvCancel);
        android.widget.TextView ok = (android.widget.TextView) dialog.findViewById(R.id.tvOk);
        title.setText("Runing Session");
        description.setText("Stop current session to exit.");
        ok.setText("Stop");

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chronometer.stop();
                session.setCalories((int) cal);
                session.setDistance(Math.round(dist));
                session.setSteps(Math.round(steps));
                session.setEndTime(System.currentTimeMillis());
                session.setSync(false);
                session.setStatus(0);
                DialogFragment dialogFragment = new SaveSessionDialog().newInstance(session);
                dialogFragment.show(getSupportFragmentManager(),"Position");
                dialog.dismiss();
                //finish();

            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();

    }
}
