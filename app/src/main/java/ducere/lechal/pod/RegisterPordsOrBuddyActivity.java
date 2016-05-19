package ducere.lechal.pod;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;

/**
 * Created by VR Naresh on 19-05-2016.
 */
public class RegisterPordsOrBuddyActivity extends AppCompatActivity implements View.OnClickListener {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initViews();
    }

    private void initViews() {
        CardView llRegisterProduct = (CardView)findViewById(R.id.llRegisterProduct);
        llRegisterProduct.setOnClickListener(this);

        CardView llRegisterBuddy = (CardView)findViewById(R.id.llRegisterBuddy);
        llRegisterBuddy.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.llRegisterProduct:
                startActivity(new Intent(RegisterPordsOrBuddyActivity.this,RegisterProductActivity.class));
                break;
            case R.id.llRegisterBuddy:
                startActivity(new Intent(RegisterPordsOrBuddyActivity.this,RegisterBuddyActivity.class));
                break;
        }
    }
}
