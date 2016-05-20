package ducere.lechal.pod;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import ducere.lechal.pod.async.Utility;
import ducere.lechal.pod.constants.SharedPrefUtil;
import ducere.lechal.pod.retrofit.LechalService;
import ducere.lechal.pod.retrofit.RetroClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by VR Naresh on 19-05-2016.
 */
public class RegisterProductActivity extends AppCompatActivity implements View.OnClickListener {
    EditText edtProductPassword, edtLastName, edtEmailId, edtFirstName;
    boolean click, reClick;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_with_product);

        initViews();
    }

    private void initViews() {
        edtFirstName = (EditText) findViewById(R.id.edtFirstName);
        edtLastName = (EditText) findViewById(R.id.edtLastName);
        edtEmailId = (EditText) findViewById(R.id.edtEmailId);
        edtProductPassword = (EditText) findViewById(R.id.edtProductPassword);

        Button btnPassword = (Button) findViewById(R.id.btnPassword);
        btnPassword.setOnClickListener(this);

        ImageView imvClear = (ImageView) findViewById(R.id.imvClear);
        imvClear.setOnClickListener(this);

        ImageView imvSubmit = (ImageView) findViewById(R.id.imvSubmit);
        imvSubmit.setOnClickListener(this);

        TextView txtHyp = (TextView) findViewById(R.id.txtHyp);
        txtHyp.setClickable(true);
        txtHyp.setMovementMethod(LinkMovementMethod.getInstance());
        String text = "<a href='http://www.google.com'> Terms of Use. </a>";
        txtHyp.setText(Html.fromHtml(text));

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnPassword:
                if (click == true) {
                    edtProductPassword.setTransformationMethod(null);
                    click = false;
                } else if (click == false) {
                    edtProductPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    click = true;
                }
                break;
            case R.id.imvClear:
                finish();
                break;
            case R.id.imvSubmit:
                String pwd = edtProductPassword.getText().toString();
                String lastName = edtLastName.getText().toString();
                String email = edtEmailId.getText().toString();
                String firstName = edtFirstName.getText().toString();
                if (!TextUtils.isEmpty(firstName)){
                    if (!TextUtils.isEmpty(lastName)){
                    if (!TextUtils.isEmpty(email)){
                        if (!TextUtils.isEmpty(pwd)){
                            LechalService service = RetroClient.getInstance().getService();
                            Call<ResponseBody> register = service.register(firstName, lastName, email, pwd, service.USER_REGISTER_TYPE);
                            register.enqueue(new Callback<ResponseBody>() {
                                @Override
                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                    if (response.isSuccessful()) {
                                        ResponseBody body = response.body();
                                        try {
                                            String string = Utility.getString(body.byteStream());
                                            JSONObject jsonObject = new JSONObject(string);
                                            Log.e("JSON", "OBJ::" + jsonObject);
                                            String userId = jsonObject.optString("userId", "");
                                            String status = jsonObject.optString("status", "");
                                            String error = jsonObject.optString("error", "");
                                            SharedPrefUtil.setUserId(getApplicationContext(),userId);
                                            if(status.contains(LechalService.STATUS_CODE_SUCCESS)){
                                                finish();
                                            }else {
                                                Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT).show();
                                            }

                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<ResponseBody> call, Throwable t) {

                                }
                            });
                        }else{
                            edtProductPassword.setError("Please enter PWD");
                        }
                    }else {
                        edtEmailId.setError("Please enter mail id");
                    }
                    }else {
                        edtLastName.setError("Enter Last name");
                    }
                }else {
                    edtFirstName.setError("Enter Fist name");
                }

                break;
        }
    }
}
