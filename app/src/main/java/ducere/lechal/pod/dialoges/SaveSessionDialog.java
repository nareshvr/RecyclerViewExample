package ducere.lechal.pod.dialoges;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import ducere.lechal.pod.R;


public class SaveSessionDialog extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = getActivity().getLayoutInflater().inflate(R.layout.save_session_dialog, null);
        builder.setView(view);
        EditText edtSessionTime = (EditText)view.findViewById(R.id.edtSessionTime);
        String title = "";
        if (Integer.parseInt(getHour()) >= 0 && Integer.parseInt(getHour()) < 12) {
            title = "Morning Session";
        } else if (Integer.parseInt(getHour()) >= 12 && Integer.parseInt(getHour()) < 16) {
            title = "Afternoon Session";
        } else if (Integer.parseInt(getHour()) >= 16 && Integer.parseInt(getHour()) < 20) {
            title = "Evening Session";
        } else {
            title = "Night Session";
        }
        edtSessionTime.setText(title);
        builder.setPositiveButton("SAVE", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("DON\'t SAVE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        final AlertDialog alertDialog = builder.create();
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.text_gray));
                alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorPrimaryAlert));
            }
        });
        return alertDialog;
    }

    public String getHour() {

        Date myDate = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("UTC"));
        calendar.setTime(myDate);
        Date time = calendar.getTime();
        SimpleDateFormat outputFmt = new SimpleDateFormat("HH");
        String dateAsString = outputFmt.format(time);

        return dateAsString;

    }
}