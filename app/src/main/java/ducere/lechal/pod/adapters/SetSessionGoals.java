package ducere.lechal.pod.adapters;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import ducere.lechal.pod.R;
import ducere.lechal.pod.StartSessionActivity;
import ducere.lechal.pod.constants.SessionConstants;
import ducere.lechal.pod.podsdata.Session;

/**
 * Created by VR Naresh on 04-05-2016.
 */
public class SetSessionGoals extends DialogFragment {

    Session session;
    Spinner spinner;
    SeekBar seekBarSetGoals;
    Switch swMileStone;
     TextView txtProgressData;
    EditText etValue;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        //ContextThemeWrapper ctw = new ContextThemeWrapper( getActivity(), R.style.dialogStyle);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());


        View view = getActivity().getLayoutInflater().inflate(R.layout.set_session_goal, null);
        builder.setView(view);
        session = new Session();
        builder.setPositiveButton(R.string.start, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                session.setGoalType(getSelectedId());
                session.setActivityType(getMode(getArguments().getInt("mode")));
                session.setGoalValue(Integer.parseInt(etValue.getText().toString()));
                session.setStartTime(System.currentTimeMillis());
                session.setMilestoneFreq(0);

                startActivity(new Intent(getActivity(), StartSessionActivity.class).putExtra("session",session));
            }
        })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
        // Create the AlertDialog object and return it
        spinner = (Spinner) view.findViewById(R.id.spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.goals_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        final LinearLayout llMailStone = (LinearLayout) view.findViewById(R.id.llMailStone);
        llMailStone.setVisibility(View.GONE);
        seekBarSetGoals = (SeekBar) view.findViewById(R.id.seekBarSetGols);
        swMileStone = (Switch) view.findViewById(R.id.swMileStone);
        etValue = (EditText)view.findViewById(R.id.etValue);
        swMileStone.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    llMailStone.setVisibility(View.VISIBLE);
                } else {
                    llMailStone.setVisibility(View.GONE);
                }
            }
        });
        txtProgressData = (TextView) view.findViewById(R.id.txtProgressData);

        seekBarSetGoals.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progress = 0;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progresValue, boolean fromUser) {
                progress = progresValue;
                //Toast.makeText(getActivity(), "Changing seekbar's progress", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Toast.makeText(getActivity(), "Started tracking seekbar", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                txtProgressData.setText("Milestone feedback: " + "\n" + progress + "/" + seekBar.getMax());
                //Toast.makeText(getActivity(), "Stopped tracking seekbar", Toast.LENGTH_SHORT).show();
            }
        });


        final AlertDialog alertDialog = builder.create();
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(Color.CYAN);
                alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(Color.RED);
            }
        });
        return alertDialog;
    }

    private int getMode(int mode) {

      int act= SessionConstants.ACTIVITY_WALK;
        switch (mode){
            case 0:
                act = SessionConstants.ACTIVITY_WALK;
                break;
            case 1:
                act = SessionConstants.ACTIVITY_RUN;
                break;
            case 2:
                act = SessionConstants.ACTIVITY_CYCLE;
                break;
        }
        return act;
    }

    private int getSelectedId() {
        int target =SessionConstants.TARGET_CALORIES;
        switch (spinner.getSelectedItemPosition()){
            case 0:
                target = SessionConstants.TARGET_CALORIES;
                break;
            case 1:
                target = SessionConstants.TARGET_STEPS;
                break;
            case 2:
                target = SessionConstants.TARGET_DISTANCE;
                break;
            case 3:
                target = SessionConstants.TARGET_TIME;
                break;
        }
        return target;
    }

    public static SetSessionGoals newInstance(int num) {
        SetSessionGoals f = new SetSessionGoals();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putInt("mode", num);
        f.setArguments(args);

        return f;
    }
}
