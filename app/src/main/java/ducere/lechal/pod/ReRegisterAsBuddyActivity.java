package ducere.lechal.pod;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by VR Naresh on 19-05-2016.
 */
public class ReRegisterAsBuddyActivity extends AppCompatActivity implements View.OnClickListener {
    EditText edtRePassword,edtReBuddyPassword;
    private boolean click,reClick;
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_with_budy_continue);

        initViews();
    }

    private void initViews() {
        edtRePassword = (EditText)findViewById(R.id.edtRePassword);
        edtReBuddyPassword = (EditText)findViewById(R.id.edtReBuddyPassword);

        Button btnBuddyPassword  = (Button)findViewById(R.id.btnBuddyPassword);
        btnBuddyPassword.setOnClickListener(this);

        Button btnBuddyRePassword = (Button)findViewById(R.id.btnBuddyRePassword);
        btnBuddyRePassword.setOnClickListener(this);

        TextView txtHyp = (TextView)findViewById(R.id.txtHyp);
        txtHyp.setClickable(true);
        txtHyp.setMovementMethod(LinkMovementMethod.getInstance());
        String text = "<a href='http://www.google.com'> Terms of Use. </a>";
        txtHyp.setText(Html.fromHtml(text));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnBuddyPassword:
                if (click == true) {
                    edtRePassword.setTransformationMethod(null);
                    click = false;
                } else if (click == false) {
                    edtRePassword.setTransformationMethod(new PasswordTransformationMethod());
                    click = true;
                }
                break;
            case R.id.btnBuddyRePassword:
                if (reClick == true) {
                    edtReBuddyPassword.setTransformationMethod(null);
                    click = false;
                } else if (reClick == false) {
                    edtReBuddyPassword.setTransformationMethod(new PasswordTransformationMethod());
                    click = true;
                }
                break;
        }
    }
}
