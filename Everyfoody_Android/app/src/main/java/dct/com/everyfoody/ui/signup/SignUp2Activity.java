package dct.com.everyfoody.ui.signup;

import android.content.Intent;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dct.com.everyfoody.R;
import dct.com.everyfoody.base.BaseModel;
import dct.com.everyfoody.base.WhiteThemeActivity;
import dct.com.everyfoody.base.util.LogUtil;
import dct.com.everyfoody.base.util.ToastMaker;
import dct.com.everyfoody.global.ApplicationController;
import dct.com.everyfoody.model.TempLogin;
import dct.com.everyfoody.model.UserInfo;
import dct.com.everyfoody.request.NetworkService;
import dct.com.everyfoody.ui.home.user.MainActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static dct.com.everyfoody.ui.login.LoginActivity.NETWORK_SUCCESS;
import static dct.com.everyfoody.ui.signup.SignUpMainActivity.SIGNUP_OWNER;


public class SignUp2Activity extends WhiteThemeActivity {
    @BindView(R.id.signup_name_edit)
    EditText nameEdit;
    @BindView(R.id.signup_phone_edit)
    EditText phoneEdit;
    @BindView(R.id.signup_status_text)
    TextView signupText;

    private NetworkService networkService;
    private TempLogin tempLogin;
    private int signupFlag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up2);
        ButterKnife.bind(this);
        networkService = ApplicationController.getInstance().getNetworkService();
        getData();

        phoneEdit.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
    }

    private void getData() {
        Intent getData = getIntent();
        signupFlag = getData.getExtras().getInt("ownerORuser");
        if (signupFlag == SIGNUP_OWNER)
            signupText.setText("사업자 회원가입");

        tempLogin = new Gson().fromJson(getData.getExtras().getString("tempLoginInfo"), TempLogin.class);
    }

    @OnClick(R.id.signup_btn)
    public void onClickSignUp(View view) {
        Pattern name = Pattern.compile("^([가-힣]{2,4})$");
        Matcher mname = name.matcher(nameEdit.getText().toString());


        if (mname.find()) {
            if (signupFlag != SIGNUP_OWNER)
                userSignUp();
            else
                ownerSignUp();
        } else {
            ToastMaker.makeShortToast(getApplicationContext(), "이름의 형식을 맞춰주세요!");
            nameEdit.requestFocus();
        }
    }

    private void userSignUp() {
        UserInfo userInfo = new UserInfo();
        if (!TextUtils.isEmpty(tempLogin.getImageUrl())) {
            userInfo.setImageURL(tempLogin.getImageUrl());
        } else {
            userInfo.setImageURL("");
        }

        userInfo.setCategory(tempLogin.getCategory());
        userInfo.setUid(tempLogin.getUid());
        userInfo.setEmail(tempLogin.getEmail());
        userInfo.setPhone(phoneEdit.getText().toString());
        userInfo.setName(nameEdit.getText().toString());


        Log.d("dfdf", tempLogin.getEmail() + "  " + tempLogin.getUid() + "  " + userInfo.getImageURL() + "  " + tempLogin.getCategory() + "  " + phoneEdit.getText().toString() + "  " + nameEdit.getText().toString());
        Call<BaseModel> signUpCall = networkService.userSignUp(userInfo);

        signUpCall.enqueue(new Callback<BaseModel>() {
            @Override
            public void onResponse(Call<BaseModel> call, Response<BaseModel> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals(NETWORK_SUCCESS)) {
                        ToastMaker.makeShortToast(getApplicationContext(), "다시 로그인해주세요.");
                        Intent loginIntent = new Intent(getApplicationContext(), MainActivity.class);
                        loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(loginIntent);
                        finish();
                    }
                }
            }

            @Override
            public void onFailure(Call<BaseModel> call, Throwable t) {
                LogUtil.d(getApplicationContext(), t.toString());
            }
        });
    }

    private void ownerSignUp() {
        UserInfo userInfo = new UserInfo();
        if (!TextUtils.isEmpty(tempLogin.getImageUrl())) {
            userInfo.setImageURL(tempLogin.getImageUrl());
        } else {
            userInfo.setImageURL("");
        }
        userInfo.setCategory(tempLogin.getCategory());
        userInfo.setUid(tempLogin.getUid());
        userInfo.setEmail(tempLogin.getEmail());
        userInfo.setPhone(phoneEdit.getText().toString());
        userInfo.setName(nameEdit.getText().toString());

        Call<BaseModel> signUpCall = networkService.ownerSignUp(userInfo);

        signUpCall.enqueue(new Callback<BaseModel>() {
            @Override
            public void onResponse(Call<BaseModel> call, Response<BaseModel> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals(NETWORK_SUCCESS)) {
                        ToastMaker.makeShortToast(getApplicationContext(), "다시 로그인해주세요.");
                        Intent loginIntent = new Intent(getApplicationContext(), MainActivity.class);
                        loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(loginIntent);
                    }
                }
            }

            @Override
            public void onFailure(Call<BaseModel> call, Throwable t) {
                LogUtil.d(getApplicationContext(), t.toString());
            }
        });
    }
}
