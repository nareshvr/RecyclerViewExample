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
public class RegisterProductActivity extends AppCompatActivity implements View.OnClickListener {
    EditText edtProductPassword,edtReEnterPassword;
    boolean click,reClick;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_with_product);

        initViews();
    }

    private void initViews() {
        edtProductPassword = (EditText)findViewById(R.id.edtProductPassword);
        edtReEnterPassword = (EditText)findViewById(R.id.edtReEnterPassword);

        Button btnPassword  = (Button)findViewById(R.id.btnPassword);
        btnPassword.setOnClickListener(this);

        Button btnProRePassword = (Button)findViewById(R.id.btnProRePassword);
        btnProRePassword.setOnClickListener(this);

        TextView txtHyp = (TextView)findViewById(R.id.txtHyp);
        txtHyp.setClickable(true);
        txtHyp.setMovementMethod(LinkMovementMethod.getInstance());
        String text = "<a href='http://www.google.com'> Terms of Use. </a>";
        txtHyp.setText(Html.fromHtml(text));

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnPassword:
                if (click == true) {
                    edtProductPassword.setTransformationMethod(null);
                    click = false;
                } else if (click == false) {
                    edtProductPassword.setTransformationMethod(new PasswordTransformationMethod());
                    click = true;
                }
                break;
            case R.id.btnProRePassword:
                if (reClick == true) {
                    edtReEnterPassword.setTransformationMethod(null);
                    click = false;
                } else if (reClick == false) {
                    edtReEnterPassword.setTransformationMethod(new PasswordTransformationMethod());
                    click = true;
                }
                break;
        }
    }
}
