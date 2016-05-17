package ducere.lechal.pod.dialoges;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import ducere.lechal.pod.R;

/**
 * Created by VR Naresh on 13-05-2016.
 */
public class DialogHeight extends DialogFragment {
    EditText edtSecond;
    Spinner spHeight;

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_height, null);
        builder.setView(view);
        // Create the AlertDialog object and return it
        spHeight = (Spinner) view.findViewById(R.id.spHeight);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.height_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spHeight.setAdapter(adapter);
        EditText edtFirst = (EditText) view.findViewById(R.id.edtFirst);
        edtSecond = (EditText) view.findViewById(R.id.edtSecond);
        edtFirst.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.toString().trim().length() == 1){
                    edtSecond.requestFocus();
                }
            }

        });
        edtSecond.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (s.toString().length()==1){
                    spHeight.setFocusable(true);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.toString().trim().length() == 1){
                    spHeight.requestFocus();
                }

            }
        });
        final AlertDialog alertDialog = builder.create();
        return alertDialog;
    }
}
