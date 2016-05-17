package ducere.lechal.pod.dialoges;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import ducere.lechal.pod.R;
import ducere.lechal.pod.constants.SharedPrefUtil;

/**
 * Created by VR Naresh on 16-05-2016.
 */
public class DialogWeight extends DialogFragment {
    EditText edtSecond;
    Spinner spWeight;

    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_weight, null);
        builder.setView(view);

        spWeight = (Spinner) view.findViewById(R.id.spWeight);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.weight_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spWeight.setAdapter(adapter);
        spWeight.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SharedPrefUtil.setWeightUnits(getContext(),position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        if(SharedPrefUtil.getWeightUnits(getContext())==0) {
            spWeight.setSelection(0);
        }else {
            spWeight.setSelection(1);
        }
        EditText edtWeight = (EditText) view.findViewById(R.id.edtWeight);

        return builder.create();
    }
}
