package ducere.lechal.pod;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by VR Naresh on 19-05-2016.
 */
public class RegisterBuddyActivity extends AppCompatActivity implements View.OnClickListener {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_with_budy);

        initViews();
    }

    private void initViews() {
        EditText edtBuddy = (EditText)findViewById(R.id.edtBuddy);
        edtBuddy.setOnClickListener(this);

        TextView txtHyp =(TextView)findViewById(R.id.txtHyp);
        txtHyp.setClickable(true);
        txtHyp.setMovementMethod(LinkMovementMethod.getInstance());
        String text = "<a href='http://www.lechal.com'>www.lechal.com</a>";
        txtHyp.setText(Html.fromHtml(text));

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.edtBuddy:
                startActivity(new Intent(RegisterBuddyActivity.this,ReRegisterAsBuddyActivity.class));
                break;
        }
    }
}
