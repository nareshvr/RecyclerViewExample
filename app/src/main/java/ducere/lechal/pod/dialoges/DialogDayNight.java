package ducere.lechal.pod.dialoges;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Html;

/**
 * Created by VR Naresh on 13-05-2016.
 */
public class DialogDayNight extends DialogFragment {
    AlertDialog levelDialog;
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final CharSequence[] items = {" Day view ", " Night view ", " Auto-switch "};
        // Creating and Building the Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder.setTitle(Html.fromHtml("<font color='#F05854'>Day/Night view</font>"));
        builder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
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
