package ducere.lechal.pod;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import ducere.lechal.pod.constants.SharedPrefUtil;

import static android.app.PendingIntent.getActivity;

/**
 * Created by VR Naresh on 09-05-2016.
 */
public class EditDailySession extends AppCompatActivity implements View.OnClickListener {
    EditText edtSteps;
TextView tvCal;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_daily_goals);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        edtSteps=(EditText)findViewById(R.id.edtSteps);
        tvCal = (TextView)findViewById(R.id.tvCal);
        int position = edtSteps.length();
        Editable etext = edtSteps.getText();
        Selection.setSelection(etext, position);

        edtSteps.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                try {

                    String steps = edtSteps.getText().toString();
                    int step1 = Integer.parseInt(steps);
                    int i = returnCalories(step1);
                    tvCal.setText(i + "");
                }catch (Exception e){
                    Log.e("Exception","Steps to cal "+e.toString());
                }
            }
        });
        int sessionGoals = SharedPrefUtil.getSessionGoals(getApplicationContext());
        edtSteps.setText(sessionGoals+"");
        CardView cwSaveGoals = (CardView)findViewById(R.id.cwSaveGoals);
        cwSaveGoals.setOnClickListener(this);
    }

    public int returnCalories(int steps){

        double calPerMile =  0.57 * Float.parseFloat( "170") ;
        // double calPerSteps = calPerMile/(0.415 * Integer.parseInt(mPref.getString(R.string.height_cm, "170")));
        double  strideLenFoot = (0.415*0.393701*Integer.parseInt( "170")) / 12; //0.415* ht in inches divide by 12 gives feet per stride
        double stepsPerMile = 5280/(strideLenFoot); // Divide 5,280 by your average stride length in feet.
        double calPerSteps = calPerMile/(stepsPerMile);
        return (int)Math.round((calPerSteps*steps));

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.cwSaveGoals:
                String saveSteps = edtSteps.getText().toString();
                SharedPrefUtil.setSessionGoals(getApplicationContext(),Integer.parseInt(saveSteps));
                finish();
                break;
        }
    }
}
