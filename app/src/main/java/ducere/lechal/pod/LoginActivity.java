package ducere.lechal.pod;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import ducere.lechal.pod.async.Utility;
import ducere.lechal.pod.constants.Constants;
import ducere.lechal.pod.constants.SharedPrefUtil;
import ducere.lechal.pod.retrofit.LechalService;
import ducere.lechal.pod.retrofit.RetroClient;
import ducere.lechal.pod.server_models.User;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by VR Naresh on 18-05-2016.
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    Button btnPassword;
    EditText edtPassword;
    EditText edtUserName;
    boolean click;
    Context context = this;
    LechalService service;
    String username = null;
    String pwd = null;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initViews();
    }

    private void initViews() {
        btnPassword = (Button) findViewById(R.id.btnPassword);

        btnPassword.setOnClickListener(this);
        edtPassword = (EditText) findViewById(R.id.edtPassword);

        edtUserName = (EditText) findViewById(R.id.edtUserName);

        CardView llLogin = (CardView) findViewById(R.id.llLogin);
        llLogin.setOnClickListener(this);

        CardView llRegistarion = (CardView) findViewById(R.id.llRegistarion);
        llRegistarion.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnPassword:
                if (click == true) {
                    edtPassword.setTransformationMethod(null);
                    click = false;
                } else if (click == false) {
                    edtPassword.setTransformationMethod(new PasswordTransformationMethod());
                    click = true;
                }
                break;
            case R.id.llLogin:

                username = edtUserName.getText().toString();
                pwd = edtPassword.getText().toString();
                if (!TextUtils.isEmpty(username)) {
                    if (!TextUtils.isEmpty(pwd)) {
                        boolean connected = Utility.isConnected(context);
                        if (connected) {
                            service = RetroClient.getInstance().getService();
                            Call<ResponseBody> loginUser = service.loginUser(username, pwd, service.USER_LOGIN_TYPE);
                            loginUser.enqueue(callback);
                        }
                    } else {
                        edtPassword.setError("Please enter valid pwd");
                    }
                } else {
                    edtUserName.setError("Please enter valid mail id");
                }

                break;
            case R.id.llRegistarion:
                startActivity(new Intent(LoginActivity.this, RegisterProductActivity.class));
                break;
        }
    }

    Callback<ResponseBody> callback = new Callback<ResponseBody>() {
        @Override
        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
            if (response.isSuccessful()) {
                ResponseBody body = response.body();
                try {
                    String string = Utility.getString(body.byteStream());
                    JSONObject jsonObject = new JSONObject(string);
                    String userId = jsonObject.optString("userId", "");
                    if (!TextUtils.isEmpty(userId)) {
                        // valid usr id
                        startActivity(new Intent(LoginActivity.this, RegisterPordsOrBuddyActivity.class));
                        SharedPrefUtil.setUserId(context,userId);

                        //Toast.makeText(getApplication(), "USER ID = " + userId, Toast.LENGTH_LONG).show();
                        Call<User> user = service.getUser(userId, service.USER_GET_TYPE);
                        user.enqueue(new Callback<User>() {
                            @Override
                            public void onResponse(Call<User> call, Response<User> response) {
                                if (response.isSuccessful()) {
                                    User user = response.body();
                                    SharedPrefUtil.setUser(context,user);
                                    Log.e("User", "INFO::" + user.toString());
                                }
                            }

                            @Override
                            public void onFailure(Call<User> call, Throwable t) {

                            }
                        });
                    } else {
                        Toast.makeText(getApplicationContext(), "Login fail", Toast.LENGTH_LONG).show();
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
    };

}
