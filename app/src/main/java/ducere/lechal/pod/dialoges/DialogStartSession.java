package ducere.lechal.pod.dialoges;


import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ImageView;
import ducere.lechal.pod.R;
import ducere.lechal.pod.adapters.SetSessionGoals;

public class DialogStartSession extends DialogFragment {
    int mode=0;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        View view = getActivity().getLayoutInflater().inflate(R.layout.start_session, null);
        builder.setView(view);

        builder.setPositiveButton(R.string.next, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                DialogFragment fragment = new SetSessionGoals().newInstance(mode);
                fragment.show(getFragmentManager(),"position");
            }
        })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
        final ImageView imgWalk = (ImageView)view.findViewById(R.id.imgWalk);
        final ImageView imgRun = (ImageView)view.findViewById(R.id.imgRun);
        final ImageView imgCycle = (ImageView)view.findViewById(R.id.imgCycle);
        imgWalk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgWalk.setBackgroundDrawable(getResources().getDrawable(R.mipmap.img_walk_red));
                imgRun.setBackgroundDrawable(getResources().getDrawable(R.mipmap.img_run_white));
                imgCycle.setBackgroundDrawable(getResources().getDrawable(R.mipmap.img_cycle_white));
                mode=1;
            }
        });
        imgRun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgRun.setBackgroundDrawable(getResources().getDrawable(R.mipmap.img_run_red));
                imgWalk.setBackgroundDrawable(getResources().getDrawable(R.mipmap.img_walk_white));
                imgCycle.setBackgroundDrawable(getResources().getDrawable(R.mipmap.img_cycle_white));
                mode=2;
            }
        });
        imgCycle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgCycle.setBackgroundDrawable(getResources().getDrawable(R.mipmap.img_cycle_red));
                imgRun.setBackgroundDrawable(getResources().getDrawable(R.mipmap.img_run_white));
                imgWalk.setBackgroundDrawable(getResources().getDrawable(R.mipmap.img_walk_white));
                mode=3;
            }
        });
        // Create the AlertDialog object and return it
        final AlertDialog alertDialog = builder.create();
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(Color.DKGRAY);
                alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(Color.RED);
            }
        });
        return alertDialog;
    }
}