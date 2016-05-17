package ducere.lechal.pod;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;

import ducere.lechal.pod.dialoges.DialogClearHistory;
import ducere.lechal.pod.dialoges.DialogGender;
import ducere.lechal.pod.dialoges.DialogHeight;
import ducere.lechal.pod.dialoges.DialogWeight;

/**
 * Created by VR Naresh on 13-05-2016.
 */
public class FitnessSettingsActivity extends AppCompatActivity implements View.OnClickListener{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fitness_settings);

        initViews();
    }

    private void initViews() {
        LinearLayout llGender = (LinearLayout)findViewById(R.id.llGender);
        llGender.setOnClickListener(this);

        LinearLayout llHeight = (LinearLayout)findViewById(R.id.llHeight);
        llHeight.setOnClickListener(this);

        LinearLayout llWeight  = (LinearLayout)findViewById(R.id.llWeight);
        llWeight.setOnClickListener(this);

        LinearLayout llClearHistory  = (LinearLayout)findViewById(R.id.llClearHistory);
        llClearHistory.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.llGender:
                DialogFragment gender = new DialogGender();
                gender.show(getSupportFragmentManager(),"position");
                break;
            case R.id.llHeight:
                DialogFragment height = new DialogHeight();
                height.show(getSupportFragmentManager(),"position");
                break;
            case R.id.llWeight:
                DialogFragment weight = new DialogWeight();
                weight.show(getSupportFragmentManager(),"position");
                break;
            case R.id.llClearHistory:
                DialogFragment clear = new DialogClearHistory();
                clear.show(getSupportFragmentManager(),"position");
                break;
        }
    }
}
