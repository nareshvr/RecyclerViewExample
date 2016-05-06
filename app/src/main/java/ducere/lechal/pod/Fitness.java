package ducere.lechal.pod;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import ducere.lechal.pod.adapters.StartSessionDialog;

/**
 * Created by VR Naresh on 03-05-2016.
 */
public class Fitness extends Fragment implements View.OnClickListener {
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

    public Fitness() {
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
        popup.getMenuInflater().inflate(R.menu.main, popup.getMenu());

        //registering popup with OnMenuItemClickListener
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                Toast.makeText(getActivity(), "You Clicked : " + item.getTitle(), Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        popup.show();//showing popup menu
    }


}