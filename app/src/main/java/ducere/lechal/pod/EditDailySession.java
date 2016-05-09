package ducere.lechal.pod;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import static android.app.PendingIntent.getActivity;

/**
 * Created by VR Naresh on 09-05-2016.
 */
public class EditDailySession extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_daily_goals);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
