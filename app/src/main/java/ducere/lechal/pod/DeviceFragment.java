package ducere.lechal.pod;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.seanzor.prefhelper.SharedPrefHelper;

import ducere.lechal.pod.constants.Constants;
import ducere.lechal.pod.constants.Vibrations;
import ducere.lechal.pod.customViews.CircleProgressView;
import ducere.lechal.pod.interfaces.OnFragmentInteractionListener;


public class DeviceFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String VB = "VB";
    Vibrations vib;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    View view;
    SharedPrefHelper mPref;
    SharedPreferences defaultSharedPreferences;
    TextView txtShoeType;
    CircleProgressView progressView;
    private OnFragmentInteractionListener mListener;
    Context mContext = getContext();
    LinearLayout llTutorials;
    public DeviceFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DeviceFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DeviceFragment newInstance(String param1, String param2) {
        DeviceFragment fragment = new DeviceFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_device, container, false);
        vib = new Vibrations();
        defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mPref = new SharedPrefHelper(getResources(), defaultSharedPreferences);
        LinearLayout btnCheckPodPosition = (LinearLayout) view.findViewById(R.id.llCheckPodPosition);
        btnCheckPodPosition.setOnClickListener(this);

        llTutorials = (LinearLayout) view.findViewById(R.id.llTutorials);
        llTutorials.setOnClickListener(this);
        LinearLayout llIntensity = (LinearLayout)view.findViewById(R.id.llIntensity);
        llIntensity.setOnClickListener(this);
        LinearLayout llFootwear = (LinearLayout)view.findViewById(R.id.llFootwear);
        llFootwear.setOnClickListener(this);
        txtShoeType = (TextView)view.findViewById(R.id.txtShoeType);
        progressView = (CircleProgressView)view.findViewById(R.id.imgBattareyStatus);
        progressView.setPaintColor(Color.CYAN);
        return view;
    }

    /*// TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }*/

    /*@Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }
*/
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {
       switch (v.getId()){
           case R.id.llCheckPodPosition:
               //@ToDo
               checkPodPositionDialog();
               break;
           case R.id.llTutorials:
               startActivity(new Intent(getActivity(),VibrationTutorialActivity.class));
               break;
           case R.id.llIntensity:
               startActivity(new Intent(getActivity(),IntensityActity.class));
               break;
           case R.id.llFootwear:
              // startActivity(new Intent(getActivity(),FootwearActity.class));
               showFootwearDialog();
               break;
       }
    }

    private void checkPodPositionDialog() {
        // custom dialog
        final Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.check_pods_position);
        // set the custom dialog components - text, image and button
        final ImageView leftPod = (ImageView) dialog.findViewById(R.id.leftPod);
        leftPod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPref.getBoolean(R.string.swap, false)) {
                    //BLEMS.sendVibM1("VB", "0001");
                    Constants.sendVibrationLeft(getContext(),"VB0001");
                } else {
                    Constants.sendVibrationRight(getContext(),"VB0100");
                    //BLEMS.sendVibM1("VB", "0100");
                }
                //BLEMS.sendVibM1("VB", "0100");
                //leftPod.setBackgroundResource(R.drawable.pod_position_left);
                new CountDownTimer(1000, 1000) {

                    @Override
                    public void onTick(long millisUntilFinished) {

                    }

                    @Override
                    public void onFinish() {
                        //leftPod.setBackgroundResource(R.drawable.pod_position_left0);
                    }
                }.start();
            }
        });

        final ImageView rightPod = (ImageView)dialog.findViewById(R.id.rightPod);
        rightPod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mPref.getBoolean(R.string.swap,false)){
                    //BLEMS.sendVibM1("VB", "0100");
                    Constants.sendVibrationLeft(getContext(),"VB0100");

                }else{
                   // BLEMS.sendVibM1("VB", "0001");
                    Constants.sendVibrationRight(getContext(),"VB0001");
                }
               // rightPod.setBackgroundResource(R.drawable.pod_position_right);
                new CountDownTimer(1000, 1000) {

                    @Override
                    public void onTick(long millisUntilFinished) {

                    }

                    @Override
                    public void onFinish() {
                        //rightPod.setBackgroundResource(R.drawable.pod_position_right0);
                    }
                }.start();
            }
        });

        ImageView swapPods = (ImageView)dialog.findViewById(R.id.podSwap);
        swapPods.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mPref.getBoolean(R.string.swap, false)) {
                    mPref.commitBoolean(R.string.swap, false);
                    // BLEMS.sendVibM1("VB", "0100");

                } else {
                    mPref.commitBoolean(R.string.swap, true);
                    //BLEMS.sendVibM1("VB", "0001");

                }
                //swapPods.setBackgroundResource(R.drawable.pod_position_swap);
                new CountDownTimer(1000, 1000) {

                    @Override
                    public void onTick(long millisUntilFinished) {

                    }

                    @Override
                    public void onFinish() {
                        //swapPods.setBackgroundResource(R.drawable.pod_position_swap0);
                    }
                }.start();
                Toast.makeText(getActivity(),"Swapped.",Toast.LENGTH_SHORT).show();
            }
        });
        TextView txtok = (TextView)dialog.findViewById(R.id.txtOK);
        txtok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });

        dialog.show();
    }

    private void showFootwearDialog() {


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Footwear type");

        final CharSequence[] choiceList = {"Casual shoes", "Sports shoes" , "Insole" , "Ballerina" };

        int selected = -1; // does not select anything
        builder.setSingleChoiceItems(choiceList, selected, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getContext(), "Select "+choiceList[which], Toast.LENGTH_SHORT).show();
                        switch (which){
                            case 0:
                                mPref.commitInt(R.string.shoe_type,0);
                                txtShoeType.setText(" Casual Shoe ");
                                // BLEMS.writeData("CD2");
                                Constants.sendFootwear(getContext(),"CD2");
                                break;
                            case 1:
                                mPref.commitInt(R.string.shoe_type,1);
                                txtShoeType.setText(" Sports shoes ");
                                // BLEMS.writeData("CD2");
                                Constants.sendFootwear(getContext(),"CD3");
                                break;
                            case 2:
                                mPref.commitInt(R.string.shoe_type,2);
                                txtShoeType.setText(" Insole ");
                                // BLEMS.writeData("CD2");
                                Constants.sendFootwear(getContext(),"CD1");
                                break;
                            case 3:
                                mPref.commitInt(R.string.shoe_type,3);
                                txtShoeType.setText(" Ballerina ");
                                // BLEMS.writeData("CD2");
                                Constants.sendFootwear(getContext(),"CD4");
                                break;

                        }
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */

}
