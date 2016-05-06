package ducere.lechal.pod.adapters;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.LinearLayout;

import ducere.lechal.pod.ActivityFragment;
import ducere.lechal.pod.R;
import ducere.lechal.pod.sqlite.SaveSessionDialog;


/**
 * Created by VR Naresh on 04-05-2016.
 */
public class StartSessionActivity extends FragmentActivity implements View.OnClickListener{
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.startserrion);
        CardView cwStop = (CardView) findViewById(R.id.cwStop);
        cwStop.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.cwStop:
                DialogFragment dialogFragment = new SaveSessionDialog();
                dialogFragment.show(getSupportFragmentManager(),"Position");
                break;
        }
    }
}
