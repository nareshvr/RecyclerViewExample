package ducere.lechal.pod.dialoges;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Html;

import ducere.lechal.pod.constants.SharedPrefUtil;

/**
 * Created by VR Naresh on 13-05-2016.
 */
public class DialogDayNight extends DialogFragment {
    final CharSequence[] items = {" Day view ", " Night view ", " Auto-switch "};
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Creating and Building the Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        int dayNight = SharedPrefUtil.getDayNight(getContext());
        builder.setTitle(Html.fromHtml("<font color='#F05854'>Day/Night view</font>"));
        builder.setSingleChoiceItems(items, dayNight, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                SharedPrefUtil.setDayNightType(getContext(),item);
                switch (item) {
                    case 0:
                        // Your code when first option seletced
                        break;
                    case 1:
                        // Your code when 2nd  option seletced
                        break;
                    case 2:
                        // Your code when 3rd option seletced
                        break;
                }

            }
        });

        return builder.create();
    }
}
