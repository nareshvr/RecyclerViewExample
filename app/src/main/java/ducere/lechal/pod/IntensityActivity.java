package ducere.lechal.pod;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageView;

import ducere.lechal.pod.constants.Constants;
import ducere.lechal.pod.constants.Vibrations;

/**
 * Created by VR Naresh on 18-05-2016.
 */
public class IntensityActivity extends AppCompatActivity implements View.OnClickListener, Vibrations {
    ImageView imvRotate;
    Context context = this;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intensity_tutorials);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Intensity");

        initViews();
    }

    private void initViews() {
        imvRotate = (ImageView) findViewById(R.id.imvRotate);

        CardView intensityVeryLow = (CardView) findViewById(R.id.intensityVeryLow);
        intensityVeryLow.setOnClickListener(this);

        CardView intensityVeryHigh = (CardView) findViewById(R.id.intensityVeryHigh);
        intensityVeryHigh.setOnClickListener(this);

        CardView intensityHigh = (CardView) findViewById(R.id.intensityHigh);
        intensityHigh.setOnClickListener(this);

        CardView intensityMedium = (CardView) findViewById(R.id.intensityMedium);
        intensityMedium.setOnClickListener(this);

        CardView intensityLow = (CardView) findViewById(R.id.intensityLow);
        intensityLow.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.intensityVeryLow:
                animateVeryLow();
                break;
            case R.id.intensityVeryHigh:
                animateVeryHigh();
                break;
            case R.id.intensityHigh:
                animateHigh();
                break;
            case R.id.intensityMedium:
                animateMedium();
                break;
            case R.id.intensityLow:
                animateLow();
                break;


        }
    }

    private void animateLow() {

        imvRotate.setVisibility(ImageView.VISIBLE);
        imvRotate.setBackgroundResource(R.drawable.animation_low);

        AnimationDrawable frameAnimation = (AnimationDrawable) imvRotate.getBackground();
        Constants.sendIntensity(context, INTENSITY.intensity_Low);
        if (frameAnimation.isRunning()) {
            frameAnimation.stop();
        } else {
            frameAnimation.stop();
            frameAnimation.start();
        }
    }

    private void animateMedium() {

        imvRotate.setVisibility(ImageView.VISIBLE);
        imvRotate.setBackgroundResource(R.drawable.animation_medium);

        AnimationDrawable frameAnimation = (AnimationDrawable) imvRotate.getBackground();
        Constants.sendIntensity(context, INTENSITY.intensity_Medium);
        if (frameAnimation.isRunning()) {
            frameAnimation.stop();
        } else {
            frameAnimation.stop();
            frameAnimation.start();
        }
    }


    private void animateHigh() {

        imvRotate.setVisibility(ImageView.VISIBLE);
        imvRotate.setBackgroundResource(R.drawable.animation_high);

        AnimationDrawable frameAnimation = (AnimationDrawable) imvRotate.getBackground();
        Constants.sendIntensity(context, INTENSITY.intensity_High);
        if (frameAnimation.isRunning()) {
            frameAnimation.stop();
        } else {
            frameAnimation.stop();
            frameAnimation.start();
        }
    }

    private void animateVeryHigh() {

        imvRotate.setVisibility(ImageView.VISIBLE);
        imvRotate.setBackgroundResource(R.drawable.animation_very_high);

        AnimationDrawable frameAnimation = (AnimationDrawable) imvRotate.getBackground();
        Constants.sendIntensity(context, INTENSITY.intensity_Very_High);
        if (frameAnimation.isRunning()) {
            frameAnimation.stop();
        } else {
            frameAnimation.stop();
            frameAnimation.start();
        }
    }

    private void animateVeryLow() {

        imvRotate.setVisibility(ImageView.VISIBLE);
        imvRotate.setBackgroundResource(R.drawable.animation_very_low);

        AnimationDrawable frameAnimation = (AnimationDrawable) imvRotate.getBackground();
        Constants.sendIntensity(context, INTENSITY.intensity_Very_Low);
        if (frameAnimation.isRunning()) {
            frameAnimation.stop();
        } else {
            frameAnimation.stop();
            frameAnimation.start();
        }
    }
}
