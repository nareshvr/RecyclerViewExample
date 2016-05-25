package ducere.lechal.pod.dialoges;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Html;

import ducere.lechal.pod.R;
import ducere.lechal.pod.constants.SharedPrefUtil;

/**
 * Created by VR Naresh on 13-05-2016.
 */
public class DialogGender extends DialogFragment {
    final CharSequence[] items = {" Male ", " Female "};
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        getActivity().getLayoutInflater().inflate(R.layout.viewpager_fragment, null);
        builder.setTitle(Html.fromHtml("<font color='#F05854'>Gender</font>"));
        int selected = SharedPrefUtil.getGenderType(getContext());
        builder.setSingleChoiceItems(items, selected, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                SharedPrefUtil.setGenderType(getContext(), item);
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

        return builder.create();
    }
}
