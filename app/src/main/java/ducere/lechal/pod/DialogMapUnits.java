package ducere.lechal.pod;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import android.text.Html;
import android.view.View;

/**
 * Created by VR Naresh on 13-05-2016.
 */
public class DialogMapUnits extends DialogFragment {
    AlertDialog levelDialog;
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final CharSequence[] items = {" Metric ", " Imperial US (miles/feet) ", " Imperial UK (miles/yards)"};
        // Creating and Building the Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View view= getActivity().getLayoutInflater().inflate(R.layout.dailog_mapunits,null);
        builder.setView(view);
        builder.setTitle(Html.fromHtml("<font color='#F05854'>Map units</font>"));
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

        final AlertDialog alertDialog = builder.create();
        return alertDialog;
    }
}
