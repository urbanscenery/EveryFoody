package dct.com.everyfoody.ui.home.owner.nonauth;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dct.com.everyfoody.R;
import dct.com.everyfoody.base.WhiteThemeActivity;
import dct.com.everyfoody.base.util.LogUtil;
import dct.com.everyfoody.base.util.SharedPreferencesService;
import dct.com.everyfoody.global.ApplicationController;
import dct.com.everyfoody.model.Login;
import dct.com.everyfoody.model.UserInfo;
import dct.com.everyfoody.model.UserStatus;
import dct.com.everyfoody.request.NetworkService;
import dct.com.everyfoody.ui.home.user.MainActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static dct.com.everyfoody.ui.login.LoginActivity.AUTH_TOKEN;
import static dct.com.everyfoody.ui.login.LoginActivity.DIVICE_TOKEN;
import static dct.com.everyfoody.ui.login.LoginActivity.LOGIN_KAKAO;
import static dct.com.everyfoody.ui.login.LoginActivity.LOGIN_RESULT;
import static dct.com.everyfoody.ui.login.LoginActivity.NETWORK_SUCCESS;
import static dct.com.everyfoody.ui.login.LoginActivity.RESULT_OWNER;
import static dct.com.everyfoody.ui.login.LoginActivity.USER_NAME;
import static dct.com.everyfoody.ui.login.LoginActivity.USER_STATUS;

public class NonAuthOwnerActivity extends WhiteThemeActivity {
    @BindView(R.id.truck_register_image)
    ImageView registerImage;
    @BindView(R.id.truck_register_text)
    TextView registerText;
    @BindView(R.id.non_auth_toolbar)
    Toolbar nonAuthToolbar;
    @BindView(R.id.non_auth_title)
    TextView titleText;
    @BindView(R.id.test_login)
    Button testLogin;

    private final int WAIT_REG = 61;
    private NetworkService networkService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_non_auth_owner);
        ButterKnife.bind(this);
        SharedPreferencesService.getInstance().load(this);
        networkService = ApplicationController.getInstance().getNetworkService();
        setToolbar();
        checkStatus();
        updateStatus();

    }

    private void updateStatus() {
        Call<UserStatus> statusCall = networkService.getUserStatus(SharedPreferencesService.getInstance().getPrefStringData(AUTH_TOKEN));

        statusCall.enqueue(new Callback<UserStatus>() {
            @Override
            public void onResponse(Call<UserStatus> call, Response<UserStatus> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals(NETWORK_SUCCESS) && response.body().getUserStatus() == RESULT_OWNER) {
                        SharedPreferencesService.getInstance().setPrefData(USER_STATUS, response.body().getUserStatus());
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    }
                }
            }

            @Override
            public void onFailure(Call<UserStatus> call, Throwable t) {
                LogUtil.d(getApplicationContext(), t.toString());
            }
        });
    }

    private void setToolbar() {
        titleText.setText("안녕하세요 " + SharedPreferencesService.getInstance().getPrefStringData(USER_NAME) + "님,\n오늘도 에브리푸디와 함께 해요!");
        final QDialog qDialog = new QDialog(this);
        nonAuthToolbar.setTitle("");
        setSupportActionBar(nonAuthToolbar);
        nonAuthToolbar.setNavigationIcon(getResources().getDrawable(R.drawable.question_mark));
        nonAuthToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                qDialog.setCanceledOnTouchOutside(true);
                qDialog.show();
            }
        });
    }

    private void checkStatus() {
        if (SharedPreferencesService.getInstance().getPrefIntegerData("waiting_status") == WAIT_REG) {
            registerText.setText("등록 대기중");
            Glide.with(this).load(R.drawable.waiting).into(registerImage);
            testLogin.setVisibility(View.VISIBLE);
        }
    }

    @OnClick(R.id.truck_register_btn)
    public void onClickTruckRegister(View view) {
        Intent registerIntent = new Intent(getApplicationContext(), TruckRegisterActivity.class);
        startActivityForResult(registerIntent, WAIT_REG);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == WAIT_REG) {
                SharedPreferencesService.getInstance().setPrefData("waiting_status", WAIT_REG);
                checkStatus();
            }
        }
    }

    @OnClick(R.id.temp_logout)
    public void tempLogout(View view) {
        SharedPreferencesService.getInstance().removeData(AUTH_TOKEN, USER_STATUS);
        Intent defaultHome = new Intent(NonAuthOwnerActivity.this, MainActivity.class);
        startActivity(defaultHome);
    }

    @OnClick(R.id.non_auth_logout)
    public void onClickLogout(View view) {
        logout();
    }

    @OnClick(R.id.test_login)
    public void onClickTestLogin(View view) {
        login();
    }

    private void logout() {

        SharedPreferencesService.getInstance().removeData(AUTH_TOKEN, USER_STATUS);
        Intent logoutIntent = new Intent(getApplicationContext(), MainActivity.class);
        logoutIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        logoutIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(logoutIntent);
    }

    private void login() {
        UserInfo userInfo = new UserInfo();
        userInfo.setEmail("everyfoody@test.com");
        userInfo.setUid("12345");
        userInfo.setCategory(LOGIN_KAKAO);
        userInfo.setDeviceToken(SharedPreferencesService.getInstance().getPrefStringData(DIVICE_TOKEN));


        Call<Login> loginCall2 = networkService.userLogin(userInfo);

        loginCall2.enqueue(new Callback<Login>()

        {
            @Override
            public void onResponse(Call<Login> call, Response<Login> response) {
                if (response.isSuccessful()) {
                    SharedPreferencesService.getInstance().setPrefData(AUTH_TOKEN, response.body().getResultData().getToken());
                    SharedPreferencesService.getInstance().setPrefData(USER_NAME, response.body().getResultData().getName());
                    int userStatus = response.body().getResultData().getCategory();

                    Intent loginResult = new Intent(getApplicationContext(), MainActivity.class);
                    SharedPreferencesService.getInstance().setPrefData(USER_STATUS, userStatus);
                    loginResult.putExtra(LOGIN_RESULT, userStatus);
                    setResult(RESULT_OK, loginResult);
                    finish();
                }
            }
        @Override
        public void onFailure (Call < Login > call, Throwable t){
            LogUtil.d(getApplicationContext(), t.toString());
        }
    });
}


}
