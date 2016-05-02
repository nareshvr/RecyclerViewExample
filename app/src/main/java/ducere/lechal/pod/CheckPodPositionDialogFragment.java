package ducere.lechal.pod;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Toast;

import ducere.lechal.pod.constants.Constants;
import ducere.lechal.pod.constants.SharedPrefUtil;
import ducere.lechal.pod.constants.Vibrations;

public class CheckPodPositionDialogFragment extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = getActivity().getLayoutInflater().inflate(R.layout.check_pods_position, null);
        builder.setView(view).setTitle("Confirm pod position");
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        view.findViewById(R.id.leftPod).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SharedPrefUtil.isPodSwapped(getContext())) {
                    Constants.sendVibrationRight(getContext());
                } else {
                    Constants.sendVibrationLeft(getContext());
                }
            }
        });

        view.findViewById(R.id.rightPod).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SharedPrefUtil.isPodSwapped(getContext())) {
                    Constants.sendVibrationLeft(getContext());
                } else {
                    Constants.sendVibrationRight(getContext());
                }
            }
        });

        view.findViewById(R.id.podSwap).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPrefUtil.setPodSwapped(getContext(), !SharedPrefUtil.isPodSwapped(getContext()));
                Toast.makeText(getActivity(), "Swapped.", Toast.LENGTH_SHORT).show();
            }
        });
        return builder.create();
    }
}