package ducere.lechal.pod;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import com.github.seanzor.prefhelper.SharedPrefHelper;

import ducere.lechal.pod.constants.Constants;


/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class IntensityActity extends AppCompatActivity implements View.OnClickListener{
    SharedPrefHelper mPref;
    SharedPreferences defaultSharedPreferences;
    Context context = this;
    AlertDialog levelDialog;
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intensity_actity);
        defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        // Create the helper which will wrap the SharedPreferences we just created
        mPref = new SharedPrefHelper(getResources(), defaultSharedPreferences);
        LinearLayout llIntensity  = (LinearLayout)findViewById(R.id.llIntensity);
        llIntensity.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.llIntensity:

                // Strings to Show In Dialog with Radio Buttons
                final CharSequence[] items = {" Very High "," High "," Medium "," Low "," Very Low "};

                // Creating and Building the Dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
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

                break;
        }
    }
}
