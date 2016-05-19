package ducere.lechal.pod;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by VR Naresh on 18-05-2016.
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    Button btnPassword;
    EditText edtPassword;
    boolean click;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initViews();
    }

    private void initViews() {
        btnPassword = (Button) findViewById(R.id.btnPassword);

        btnPassword.setOnClickListener(this);
        edtPassword = (EditText) findViewById(R.id.edtPassword);

        CardView llLogin = (CardView)findViewById(R.id.llLogin);
        llLogin.setOnClickListener(this);

        CardView llRegistarion = (CardView)findViewById(R.id.llRegistarion);
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
                startActivity(new Intent(LoginActivity.this,RegisterPordsOrBuddyActivity.class));
                break;
            case R.id.llRegistarion:
                startActivity(new Intent(LoginActivity.this,RegisterProductActivity.class));
                break;
        }
    }
}
