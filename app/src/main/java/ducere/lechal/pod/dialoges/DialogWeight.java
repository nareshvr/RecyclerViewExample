package ducere.lechal.pod.dialoges;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import ducere.lechal.pod.R;

/**
 * Created by VR Naresh on 16-05-2016.
 */
public class DialogWeight extends DialogFragment {
    EditText edtSecond;
    Spinner spHeight;

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_weight, null);
        builder.setView(view);
        // Create the AlertDialog object and return it
        spHeight = (Spinner) view.findViewById(R.id.spWeight);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.weight_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spHeight.setAdapter(adapter);
        EditText edtWeight = (EditText) view.findViewById(R.id.edtWeight);


        return builder.create();
    }
}
