package ducere.lechal.pod.dialoges;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import android.text.Html;
import android.view.View;

import ducere.lechal.pod.constants.SharedPrefUtil;

/**
 * Created by VR Naresh on 13-05-2016.
 */
public class DialogMapUnitsDialog extends DialogFragment {

    final CharSequence[] items = {" Metric ", " Imperial US (miles/feet) ", " Imperial UK (miles/yards)"};
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(Html.fromHtml("<font color='#F05854'>Map units</font>"));
        int mapUnits = SharedPrefUtil.getMapUnits(getContext());
        builder.setSingleChoiceItems(items, mapUnits, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                SharedPrefUtil.setMapUnits(getContext(),item);
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
