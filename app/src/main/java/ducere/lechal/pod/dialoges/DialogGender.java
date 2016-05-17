package ducere.lechal.pod.dialoges;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Html;

import ducere.lechal.pod.R;

/**
 * Created by VR Naresh on 13-05-2016.
 */
public class DialogGender extends DialogFragment {
    AlertDialog levelDialog;
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final CharSequence[] items = {" Male ", " Female "};
        // Creating and Building the Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        getActivity().getLayoutInflater().inflate(R.layout.viewpager_fragment, null);
        builder.setTitle(Html.fromHtml("<font color='#F05854'>Gender</font>"));
        builder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                switch (item) {
                    case 0:
                        // Your code when first option seletced
                        break;
                    case 1:
                        // Your code when 2nd  option seletced
                        break;
                }

            }
        });
        final AlertDialog alertDialog = builder.create();
        return alertDialog;
    }
}
