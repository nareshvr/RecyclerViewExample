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
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import ducere.lechal.pod.R;
import ducere.lechal.pod.constants.SharedPrefUtil;

/**
 * Created by VR Naresh on 13-05-2016.
 */
public class DialogHeight extends DialogFragment {
    EditText edtSecond;
    Spinner spHeight;
    LinearLayout llFeet;
    EditText edtCentimeters;
    EditText edtFirst;
    double d;
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_height, null);
        builder.setView(view);

        spHeight = (Spinner) view.findViewById(R.id.spHeight);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.height_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spHeight.setAdapter(adapter);

        edtCentimeters = (EditText)view.findViewById(R.id.edtCentimeters);
        llFeet = (LinearLayout)view.findViewById(R.id.llFeet);

        spHeight.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SharedPrefUtil.setHeightUnits(getContext(), position);
                if(position==0){
                    llFeet.setVisibility(View.VISIBLE);
                    edtCentimeters.setVisibility(View.GONE);
                    String value = edtCentimeters.getText().toString();
                    double d1 = convertCentimeterToFeetandInches(value);
                    double val=d1;
                    try {
                        String[] arr = String.valueOf(val).split("\\.");
                        int[] intArr = new int[2];
                        intArr[0] = Integer.parseInt(arr[0]); // 1
                        intArr[1] = Integer.parseInt(arr[1]); // 9
                        edtFirst.setText(intArr[0] + "");
                        edtSecond.setText(intArr[1] + "");
                        Toast.makeText(getContext(), "Feet" + d1, Toast.LENGTH_LONG).show();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }else if(position==1){
                    llFeet.setVisibility(View.GONE);
                    edtCentimeters.setVisibility(View.VISIBLE);
                    String first = edtFirst.getText().toString();
                    String second = edtSecond.getText().toString();
                    d = convertFeetToCentimeter(first,second);
                    d = Double.parseDouble(String.format("%.4g%n", d));
                    edtCentimeters.setText(d+"");
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        if (SharedPrefUtil.getHeightUnits(getContext()) == 0) {
            spHeight.setSelection(0);
            llFeet.setVisibility(View.VISIBLE);
            edtCentimeters.setVisibility(View.GONE);
        } else {
            spHeight.setSelection(1);
            llFeet.setVisibility(View.GONE);
            edtCentimeters.setVisibility(View.VISIBLE);
        }

        edtFirst = (EditText) view.findViewById(R.id.edtFirst);
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
                if (s.toString().trim().length() == 1) {
                    edtSecond.requestFocus();
                }
            }

        });
        edtSecond.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (s.toString().length() == 1) {
                    spHeight.setFocusable(true);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().trim().length() == 1) {
                    spHeight.requestFocus();
                }

            }
        });
        final AlertDialog alertDialog = builder.create();
        return alertDialog;
    }

    private double convertCentimeterToFeetandInches(String feet) {
        double heightInFeet = 0;
        try {
            if (feet != null && feet.trim().length() != 0) {
                heightInFeet = Double.parseDouble(feet);
            }
        } catch (NumberFormatException nfe) {

        }
        return (heightInFeet / 30.48);
    }

    public static double convertFeetToCentimeter(String feet, String inches) {
        double heightInFeet = 0;
        double heightInInches = 0;
        try {
            if (feet != null && feet.trim().length() != 0) {
                heightInFeet = Double.parseDouble(feet);
            }
            if (inches != null && inches.trim().length() != 0) {
                heightInInches = Double.parseDouble(inches);
            }
        } catch (NumberFormatException nfe) {

        }
        return (heightInFeet * 30.48) + (heightInInches * 3.048);
    }
}
