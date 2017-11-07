package dct.com.everyfoody.ui.signup;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import butterknife.ButterKnife;
import butterknife.OnClick;
import dct.com.everyfoody.R;
import dct.com.everyfoody.base.WhiteThemeActivity;

public class SignUpMainActivity extends WhiteThemeActivity {

    private String uid, email, imageUrl;
    private int category;
    private String tempLogin;

    public static final int SIGNUP_USER = 51;
    public static final int SIGNUP_OWNER = 52;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_main);
        ButterKnife.bind(this);
        getData();
    }

    @OnClick(R.id.user_signup)
    public void onClickUserSignup(View view){
        Intent userIntent = new Intent(this, SignUp2Activity.class);
        userIntent.putExtra("tempLoginInfo", tempLogin);
        userIntent.putExtra("ownerORuser", SIGNUP_USER);
        startActivity(userIntent);
    }

    @OnClick(R.id.owner_signup)
    public void onClickOwnerSignup(View view){
        Intent ownerIntent = new Intent(this, SignUp2Activity.class);
        ownerIntent.putExtra("tempLoginInfo", tempLogin);
        ownerIntent.putExtra("ownerORuser", SIGNUP_OWNER);
        startActivity(ownerIntent);
    }

    private void getData(){
        Intent getData = getIntent();
        tempLogin = getData.getExtras().getString("tempLoginInfo");

    }

}
