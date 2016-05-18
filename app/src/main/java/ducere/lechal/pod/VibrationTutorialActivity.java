package ducere.lechal.pod;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import ducere.lechal.pod.constants.Constants;
import ducere.lechal.pod.constants.Vibrations;

public class VibrationTutorialActivity extends AppCompatActivity implements View.OnClickListener, Vibrations {


    Context context = this;
    SharedPreferences defaultSharedPreferences;
    CardView llTurn, llKeepStraight, llSharpTurn, llDestination, llReeRoute, llLookAtYourPhone, llDestinationReached, llTutorials;
    CardView llSlightTurn, llUTurn;

    private CardView leftPodView;
    private CardView rightPodView;

    private CardView vib1000;
    private CardView vib500;
    private CardView vib200;
    private CardView vib150;
    private CardView vibNow;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vibration);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Vibration Tutorial");

        setup();
    }

    private void setup() {
        defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        llSlightTurn = (CardView) findViewById(R.id.llSlightTurn);
        llSlightTurn.setOnClickListener(this);

        llUTurn = (CardView) findViewById(R.id.llUTurn);
        llUTurn.setOnClickListener(this);

        llTurn = (CardView) findViewById(R.id.llTurn);
        llTurn.setOnClickListener(this);
        llKeepStraight = (CardView) findViewById(R.id.llKeepStraight);
        llKeepStraight.setOnClickListener(this);

        llSharpTurn = (CardView) findViewById(R.id.llSharpTurn);
        llSharpTurn.setOnClickListener(this);

        llDestination = (CardView) findViewById(R.id.llDestination);
        llDestination.setOnClickListener(this);

        llReeRoute = (CardView) findViewById(R.id.llree_route);
        llReeRoute.setOnClickListener(this);

        llLookAtYourPhone = (CardView) findViewById(R.id.lllook_at_your_phone);
        llLookAtYourPhone.setOnClickListener(this);

        llDestinationReached = (CardView) findViewById(R.id.llDestinationReached);
        llDestinationReached.setOnClickListener(this);


        rightPodView = (CardView) findViewById(R.id.pod_right);
        leftPodView = (CardView) findViewById(R.id.pod_left);
        leftPodView.setOnClickListener(this);
        rightPodView.setOnClickListener(this);

        vib1000 = (CardView) findViewById(R.id.vib_1000);
        vib1000.setOnClickListener(this);
        vib500 = (CardView) findViewById(R.id.vib_500);
        vib500.setOnClickListener(this);
        vib200 = (CardView) findViewById(R.id.vib_200);
        vib200.setOnClickListener(this);
        vib150 = (CardView) findViewById(R.id.vib_150);
        vib150.setOnClickListener(this);
        vibNow = (CardView) findViewById(R.id.vib_now);
        vibNow.setOnClickListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            finish();

        }
        return super.onOptionsItemSelected(item);
    }

    int idx = 0;
    int idx3 = 0;

    @Override
    public void onClick(View v) {
        int blackColor = ContextCompat.getColor(this, R.color.button_black);
        int whiteColor = ContextCompat.getColor(this, android.R.color.white);
        switch (v.getId()) {
            case R.id.pod_left:
                idx = 0;
                leftPodView.setCardBackgroundColor(blackColor);
                rightPodView.setCardBackgroundColor(whiteColor);
                ((TextView) findViewById(R.id.pod_left_text)).setTextColor(whiteColor);
                ((TextView) findViewById(R.id.pod_right_text)).setTextColor(blackColor);
                break;
            case R.id.pod_right:
                idx = 1;
                rightPodView.setCardBackgroundColor(blackColor);
                leftPodView.setCardBackgroundColor(whiteColor);
                ((TextView) findViewById(R.id.pod_right_text)).setTextColor(whiteColor);
                ((TextView) findViewById(R.id.pod_left_text)).setTextColor(blackColor);
                break;
            case R.id.vib_1000:
                resetVibButtons();
                idx3 = 0;
                vib1000.setCardBackgroundColor(blackColor);
                ((TextView) findViewById(R.id.vib_1000_text)).setTextColor(whiteColor);
                break;
            case R.id.vib_500:
                resetVibButtons();
                vib500.setCardBackgroundColor(blackColor);
                idx3 = 1;
                ((TextView) findViewById(R.id.vib_500_text)).setTextColor(whiteColor);
                break;
            case R.id.vib_200:
                resetVibButtons();
                vib200.setCardBackgroundColor(blackColor);
                idx3 = 2;
                ((TextView) findViewById(R.id.vib_200_text)).setTextColor(whiteColor);
                break;
            case R.id.vib_150:
                resetVibButtons();
                vib150.setCardBackgroundColor(blackColor);
                idx3 = 3;
                ((TextView) findViewById(R.id.vib_150_text)).setTextColor(whiteColor);
                break;
            case R.id.vib_now:
                resetVibButtons();
                vibNow.setCardBackgroundColor(blackColor);
                idx3 = 4;
                ((TextView) findViewById(R.id.vib_now_text)).setTextColor(whiteColor);
                break;
            case R.id.llSlightTurn:
                if (idx == 0) {
                    //btnSlightTurn.setBackgroundResource(R.drawable.demo_slight_turn_1);

                    new CountDownTimer(1000, 1000) {

                        @Override
                        public void onTick(long l) {

                        }

                        @Override
                        public void onFinish() {
                            //btnSlightTurn.setBackgroundResource(R.drawable.demo_slight_turn_0);

                        }
                    }.start();
                    if (idx3 == 0) {
                        Constants.sendVibrate(context, VB, IN_1000_MTS.slightTurn, "00");
                    } else if (idx3 == 1) {
                        Constants.sendVibrate(context, VB, IN_500_MTS.slightTurn, "00");
                    } else if (idx3 == 2) {
                        Constants.sendVibrate(context, VB, IN_200_MTS.slightTurn, "00");
                    } else {
                        Constants.sendVibrate(context, VB, NOW.slightTurn, "00");
                    }

                } else {
                    // btnSlightTurn.setBackgroundResource(R.drawable.demo_slight_right_1);

                    new CountDownTimer(1000, 1000) {

                        @Override
                        public void onTick(long l) {

                        }

                        @Override
                        public void onFinish() {
                            //btnSlightTurn.setBackgroundResource(R.drawable.demo_slight_right_0);

                        }
                    }.start();
                    if (idx3 == 0) {
                        Constants.sendVibrate(context, VB, "00", IN_1000_MTS.slightTurn);
                    } else if (idx3 == 1) {
                        Constants.sendVibrate(context, VB, "00", IN_500_MTS.slightTurn);
                    } else if (idx3 == 2) {
                        Constants.sendVibrate(context, VB, "00", IN_200_MTS.slightTurn);
                    } else {
                        Constants.sendVibrate(context, VB, "00", NOW.slightTurn);
                    }

                }

                break;
            case R.id.llUTurn:
                if (idx == 1) {
                    // btnUTurn.setBackgroundResource(R.drawable.demo_uturn_right_1);

                    new CountDownTimer(1000, 1000) {

                        @Override
                        public void onTick(long l) {

                        }

                        @Override
                        public void onFinish() {
                            // btnUTurn.setBackgroundResource(R.drawable.demo_uturn_right_0);

                        }
                    }.start();
                    if (idx3 == 0) {
                        Constants.sendVibrate(context, VB, "00", IN_1000_MTS.uTurn);
                    } else if (idx3 == 1) {
                        Constants.sendVibrate(context, VB, "00", IN_500_MTS.uTurn);
                    } else if (idx3 == 2) {
                        Constants.sendVibrate(context, VB, "00", IN_200_MTS.uTurn);
                    } else {
                        Constants.sendVibrate(context, VB, "00", NOW.uTurn);
                    }

                } else {
                    //btnUTurn.setBackgroundResource(R.drawable.demo_uturn_1);

                    new CountDownTimer(1000, 1000) {

                        @Override
                        public void onTick(long l) {

                        }

                        @Override
                        public void onFinish() {
                            //btnUTurn.setBackgroundResource(R.drawable.demo_uturn_0);

                        }
                    }.start();
                    if (idx3 == 0) {
                        Constants.sendVibrate(context, VB, IN_1000_MTS.uTurn, "00");
                    } else if (idx3 == 1) {
                        Constants.sendVibrate(context, VB, IN_500_MTS.uTurn, "00");
                    } else if (idx3 == 2) {
                        Constants.sendVibrate(context, VB, IN_200_MTS.uTurn, "00");
                    } else {
                        Constants.sendVibrate(context, VB, NOW.uTurn, "00");
                    }

                }


                break;
            case R.id.llTurn:

                if (idx == 0) {
                    //btnTurn.setBackgroundResource(R.drawable.demo_turn_1);

                    new CountDownTimer(1000, 1000) {

                        @Override
                        public void onTick(long l) {

                        }

                        @Override
                        public void onFinish() {
                            //btnTurn.setBackgroundResource(R.drawable.demo_turn_0);

                        }
                    }.start();

                    if (idx3 == 0) {
                        Constants.sendVibrate(context, VB, IN_1000_MTS.normalTurn, "00");
                    } else if (idx3 == 1) {
                        Constants.sendVibrate(context, VB, IN_500_MTS.normalTurn, "00");
                    } else if (idx3 == 2) {
                        Constants.sendVibrate(context, VB, IN_200_MTS.normalTurn, "00");
                    } else {
                        Constants.sendVibrate(context, VB, NOW.normalTurn, "00");
                    }

                } else {
                    //btnTurn.setBackgroundResource(R.drawable.demo_turn_right_1);

                    new CountDownTimer(1000, 1000) {

                        @Override
                        public void onTick(long l) {

                        }

                        @Override
                        public void onFinish() {
                            // btnTurn.setBackgroundResource(R.drawable.demo_turn_right_0);

                        }
                    }.start();

                    if (idx3 == 0) {
                        Constants.sendVibrate(context, VB, "00", IN_1000_MTS.normalTurn);
                    } else if (idx3 == 1) {
                        Constants.sendVibrate(context, VB, "00", IN_500_MTS.normalTurn);
                    } else if (idx3 == 2) {
                        Constants.sendVibrate(context, VB, "00", IN_200_MTS.normalTurn);
                    } else {
                        Constants.sendVibrate(context, VB, "00", NOW.normalTurn);
                    }

                }
                break;
            case R.id.llKeepStraight:
                Constants.sendVibrate(context, VB, straight, straight);
                break;
            case R.id.llSharpTurn:

                if (idx == 0) {
                    //btnSharpTurn.setBackgroundResource(R.drawable.demo_sharp_turn_1);

                    new CountDownTimer(1000, 1000) {

                        @Override
                        public void onTick(long l) {

                        }

                        @Override
                        public void onFinish() {
                            //btnSharpTurn.setBackgroundResource(R.drawable.demo_sharp_turn);

                        }
                    }.start();
                    if (idx3 == 0) {
                        Constants.sendVibrate(context, VB, IN_1000_MTS.sharpTurn, "00");
                    } else if (idx3 == 1) {
                        Constants.sendVibrate(context, VB, IN_500_MTS.sharpTurn, "00");
                    } else if (idx3 == 2) {
                        Constants.sendVibrate(context, VB, IN_200_MTS.sharpTurn, "00");
                    } else {
                        Constants.sendVibrate(context, VB, NOW.sharpTurn, "00");
                    }

                } else {

                    new CountDownTimer(1000, 1000) {

                        @Override
                        public void onTick(long l) {

                        }

                        @Override
                        public void onFinish() {
                            //btnSharpTurn.setBackgroundResource(R.drawable.demo_sharp_right_0);

                        }
                    }.start();
                    if (idx3 == 0) {
                        Constants.sendVibrate(context, VB, "00", IN_1000_MTS.sharpTurn);
                    } else if (idx3 == 1) {
                        Constants.sendVibrate(context, VB, "00", IN_500_MTS.sharpTurn);
                    } else if (idx3 == 2) {
                        Constants.sendVibrate(context, VB, "00", IN_200_MTS.sharpTurn);
                    } else {
                        Constants.sendVibrate(context, VB, "00", NOW.sharpTurn);
                    }
                }

                break;
            case R.id.llDestination:
                //btnDestination.setBackgroundResource(R.drawable.demo_destination_1);
                new CountDownTimer(1000, 1000) {
                    @Override
                    public void onTick(long l) {
                    }

                    @Override
                    public void onFinish() {
                        //btnDestination.setBackgroundResource(R.drawable.demo_destination_0);
                    }
                }.start();

                if (idx3 == 0) {
                    Constants.sendVibrate(context, VB, IN_1000_MTS.destination, IN_1000_MTS.destination);
                } else if (idx3 == 1) {
                    Constants.sendVibrate(context, VB, IN_500_MTS.destination, IN_500_MTS.destination);
                } else if (idx3 == 2) {
                    Constants.sendVibrate(context, VB, IN_200_MTS.destination, IN_200_MTS.destination);
                } else {
                    Constants.sendVibrate(context, VB, NOW.destination, NOW.destination);
                    if (idx == 0) {
                        Constants.sendVibrate(context, VB, suffix, "00");
                    } else {
                        Constants.sendVibrate(context, VB, "00", suffix);
                    }

                }

                break;
            case R.id.llree_route:

                new CountDownTimer(1000, 1000) {

                    @Override
                    public void onTick(long l) {

                    }

                    @Override
                    public void onFinish() {
                        //  btnReeRoute.setBackgroundResource(R.drawable.demo_re_route_0);

                    }
                }.start();
                //BLEMS.sendVibM1("VB", vib.reRoute_left + "00");
                Constants.sendVibrate(context, VB, reRoute_left, "00");
                new CountDownTimer(300, 300) {

                    @Override
                    public void onTick(long l) {

                    }

                    @Override
                    public void onFinish() {
                        Constants.sendVibrate(context, VB, "00", reRoute_right);
                    }
                }.start();
                break;
            case R.id.lllook_at_your_phone:
                //btnLookAtYourPhone.setBackgroundResource(R.drawable.demo_look_atphone_1);

                new CountDownTimer(1000, 1000) {

                    @Override
                    public void onTick(long l) {

                    }

                    @Override
                    public void onFinish() {
                        //btnLookAtYourPhone.setBackgroundResource(R.drawable.demo_look_atphone);

                    }
                }.start();
                Constants.sendVibrate(context, VB, lowBattery, "00");
                new CountDownTimer(2000, 2000) {

                    @Override
                    public void onTick(long millisUntilFinished) {

                    }

                    @Override
                    public void onFinish() {
                        Constants.sendVibrate(context, VB, "00", lowBattery);
                    }
                }.start();
                break;
            case R.id.llDestinationReached:

                new CountDownTimer(1000, 1000) {

                    @Override
                    public void onTick(long l) {

                    }

                    @Override
                    public void onFinish() {
                        // btnDestinationReached.setBackgroundResource(R.drawable.demo_destination_reached_0);

                    }
                }.start();

                Constants.sendVibrate(context, VB, NOW.destination, NOW.destination);
                if (idx == 0) {
                    Constants.sendVibrate(context, VB, suffix, "00");
                } else {
                    Constants.sendVibrate(context, VB, "00", suffix);
                }

                break;


        }

    }

    private void resetVibButtons() {
        int color = ContextCompat.getColor(this, android.R.color.white);
        vib1000.setCardBackgroundColor(color);
        vib500.setCardBackgroundColor(color);
        vib200.setCardBackgroundColor(color);
        vib150.setCardBackgroundColor(color);
        vibNow.setCardBackgroundColor(color);
        color = ContextCompat.getColor(this, R.color.button_black);
        ((TextView) findViewById(R.id.vib_1000_text)).setTextColor(color);
        ((TextView) findViewById(R.id.vib_500_text)).setTextColor(color);
        ((TextView) findViewById(R.id.vib_200_text)).setTextColor(color);
        ((TextView) findViewById(R.id.vib_150_text)).setTextColor(color);
        ((TextView) findViewById(R.id.vib_now_text)).setTextColor(color);
    }
}
