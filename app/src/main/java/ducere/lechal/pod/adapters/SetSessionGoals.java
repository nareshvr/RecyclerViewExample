package ducere.lechal.pod.adapters;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import ducere.lechal.pod.R;

/**
 * Created by VR Naresh on 04-05-2016.
 */
public class SetSessionGoals extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        //ContextThemeWrapper ctw = new ContextThemeWrapper( getActivity(), R.style.dialogStyle);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        View view = getActivity().getLayoutInflater().inflate(R.layout.set_session_goal, null);
        builder.setView(view);

        builder.setPositiveButton(R.string.start, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // FIRE ZE MISSILES!
                /*DialogFragment newFragment = new CheckPodPositionDialogFragment();
                newFragment.show(getFragmentManager(), "position");*/
               /* DialogFragment fragment = new SetSessionGoals();
                fragment.show(getFragmentManager(), "position");*/
                startActivity(new Intent(getActivity(), StartSessionActivity.class));
            }
        })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
        // Create the AlertDialog object and return it
        Spinner spinner = (Spinner) view.findViewById(R.id.spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.goals_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        final LinearLayout llMailStone = (LinearLayout) view.findViewById(R.id.llMailStone);
        llMailStone.setVisibility(View.GONE);
        SeekBar seekBarSetGoals = (SeekBar) view.findViewById(R.id.seekBarSetGols);
        Switch swMileStone = (Switch) view.findViewById(R.id.swMileStone);
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
        final TextView txtProgressData = (TextView) view.findViewById(R.id.txtProgressData);

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
}
