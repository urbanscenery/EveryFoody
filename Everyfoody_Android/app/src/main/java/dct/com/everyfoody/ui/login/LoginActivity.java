package dct.com.everyfoody.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.gson.Gson;
import com.kakao.auth.ErrorCode;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.LoginButton;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeResponseCallback;
import com.kakao.usermgmt.response.model.UserProfile;
import com.kakao.util.exception.KakaoException;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dct.com.everyfoody.R;
import dct.com.everyfoody.base.WhiteThemeActivity;
import dct.com.everyfoody.base.util.LogUtil;
import dct.com.everyfoody.base.util.SharedPreferencesService;
import dct.com.everyfoody.global.ApplicationController;
import dct.com.everyfoody.model.CheckId;
import dct.com.everyfoody.model.Login;
import dct.com.everyfoody.model.TempLogin;
import dct.com.everyfoody.model.UserInfo;
import dct.com.everyfoody.request.NetworkService;
import dct.com.everyfoody.ui.signup.SignUpMainActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends WhiteThemeActivity {
    @BindView(R.id.kakao_signup)
    LoginButton kakaoBtn;

    public final static String NETWORK_SUCCESS = "success";
    public final static String LOGIN_RESULT = "login-result-key";     //로그인 결과를 Intent로 보낼 때, IntExtra의 key값
    public final static String AUTH_TOKEN = "auth_token";
    public final static String USER_STATUS = "user_status";
    public final static String USER_NAME = "user_name";
    public final static String DIVICE_TOKEN = "fcm_token";
    public final static int RESULT_GUEST = 401;     //value값1
    public final static int RESULT_OWNER = 402;     //value값2
    public final static int RESULT_NON_AUTH_OWNER = 403;  // 인증 대기 사업자
    public final static int RESULT_NO_REG_STORE = 404;          // 미인증 사업자

    public final static int LOGIN_KAKAO = 101;
    public final static int LOGIN_FACEBOOK = 102;

    private final int EXIST_ID = 601;
    private final int NONEXIST_ID = 602;

    private NetworkService networkService;
    private SessionCallback callback;
    private CallbackManager callbackManager;
    private JSONObject json;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        SharedPreferencesService.getInstance().load(this);
        networkService = ApplicationController.getInstance().getNetworkService();


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            return;
        } else {
            super.onActivityResult(requestCode, resultCode, data);
            callbackManager.onActivityResult(requestCode, resultCode, data);
            return;
        }
    }

    @OnClick(R.id.facebook_login_btn)
    public void facebookLoginClick(final View view) {
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this,
                Arrays.asList("public_profile", "email"));
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

            @Override
            public void onSuccess(final LoginResult result) {
                GraphRequest request;
                request = GraphRequest.newMeRequest(result.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {

                    @Override
                    public void onCompleted(final JSONObject user, GraphResponse response) {
                        if (response.getError() != null) {
                        } else {
                            json = user;
                            final Profile profile = Profile.getCurrentProfile();
                            final Call<CheckId> getCheckId;

                            getCheckId = networkService.checkId(String.valueOf(profile.getId()));
                            getCheckId.enqueue(new Callback<CheckId>() {
                                @Override
                                public void onResponse(Call<CheckId> call, Response<CheckId> response) {
                                    if (response.isSuccessful()) {
                                        if (response.body().getStatus().equals(NETWORK_SUCCESS)) {
                                            if (response.body().getIdFlag() == EXIST_ID) {
                                                UserInfo userInfo = new UserInfo();
                                                try {
                                                    userInfo.setEmail(json.get("email").toString());
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                                userInfo.setUid(profile.getId());
                                                userInfo.setCategory(LOGIN_FACEBOOK);
                                                userInfo.setDeviceToken(SharedPreferencesService.getInstance().getPrefStringData("fcm_token"));


                                                Call<Login> loginCall2 = networkService.userLogin(userInfo);

                                                loginCall2.enqueue(new Callback<Login>() {
                                                    @Override
                                                    public void onResponse(Call<Login> call, Response<Login> response) {
                                                        if (response.isSuccessful()) {
                                                            SharedPreferencesService.getInstance().setPrefData(AUTH_TOKEN, response.body().getResultData().getToken());
                                                            SharedPreferencesService.getInstance().setPrefData(USER_NAME, response.body().getResultData().getName());
                                                            int userStatus = response.body().getResultData().getCategory();
                                                            SharedPreferencesService.getInstance().setPrefData(USER_STATUS, userStatus);

                                                            Intent loginResult = new Intent();
                                                            loginResult.putExtra(LOGIN_RESULT, userStatus);
                                                            setResult(RESULT_OK, loginResult);
                                                            finish();
                                                        }
                                                    }

                                                    @Override
                                                    public void onFailure(Call<Login> call, Throwable t) {
                                                        LogUtil.d(getApplicationContext(), t.toString());
                                                    }
                                                });
                                            } else {
                                                Intent intent = new Intent(getApplicationContext(), SignUpMainActivity.class);
                                                TempLogin tempLogin = null;
                                                try {
                                                    tempLogin = new TempLogin(json.get("email").toString(), profile.getId(), profile.getProfilePictureUri(200, 200).toString(), LOGIN_FACEBOOK);
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                                intent.putExtra("tempLoginInfo", new Gson().toJson(tempLogin).toString());
                                                startActivity(intent);
                                                finish();
                                            }
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<CheckId> call, Throwable t) {
                                    LogUtil.d(getApplicationContext(), t.toString());
                                }
                            });
                        }
                    }
                });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender,birthday");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onError(FacebookException error) {
                Log.e("testtttt", "Error: " + error);
            }

            @Override
            public void onCancel() {
            }
        });

    }


    @OnClick(R.id.kakao_login_btn)
    public void kakaoLoginClick(View view) {

        callback = new SessionCallback();
        Session.getCurrentSession().addCallback(callback);
        kakaoBtn.performClick();

    }

    public class SessionCallback implements ISessionCallback {


        @Override
        public void onSessionOpened() {
            requestMe();
        }

        @Override
        public void onSessionOpenFailed(KakaoException exception) {
            if (exception != null) {
                LogUtil.d("kakao_session_error", exception.toString());
            }
            setContentView(R.layout.activity_login);
        }

        protected void requestMe() {
            UserManagement.requestMe(new MeResponseCallback() {
                @Override
                public void onFailure(ErrorResult errorResult) {
                    String message = "failed to get user inof. msg =" + errorResult;
                    Log.v("fail", message);

                    ErrorCode result = ErrorCode.valueOf(errorResult.getErrorMessage());
                    if (result == ErrorCode.CLIENT_ERROR_CODE) {
                        finish();
                    } else {
                        redirectLoginActivity();
                    }
                }

                @Override
                public void onSessionClosed(ErrorResult errorResult) {
                    redirectLoginActivity();
                }

                @Override
                public void onNotSignedUp() {
                    showSignup();
                }

                @Override
                public void onSuccess(final UserProfile result) {

                    checkUid(result);
                }
            });
        }

        protected void showSignup() {
            redirectLoginActivity();
        }


        public void checkUid(final UserProfile userInfo) {
            Call<CheckId> checkIdCall = networkService.checkId(String.valueOf(userInfo.getId()));

            checkIdCall.enqueue(new Callback<CheckId>() {
                @Override
                public void onResponse(Call<CheckId> call, Response<CheckId> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getStatus().equals(NETWORK_SUCCESS)) {
                            if (response.body().getIdFlag() == EXIST_ID)
                                startLogin(userInfo.getEmail(), userInfo.getId());
                            else if (response.body().getIdFlag() == NONEXIST_ID)
                                redirectMainActivity(userInfo.getProfileImagePath(), userInfo.getId(), userInfo.getEmail());
                        }
                    }
                }

                @Override
                public void onFailure(Call<CheckId> call, Throwable t) {
                    LogUtil.d(getApplicationContext(), t.toString());
                }
            });
        }

        public void startLogin(String email, long uid) {
            UserInfo userInfo = new UserInfo();
            userInfo.setEmail(email);
            userInfo.setUid(String.valueOf(uid));
            userInfo.setCategory(LOGIN_KAKAO);
            userInfo.setDeviceToken(SharedPreferencesService.getInstance().getPrefStringData("fcm_token"));

            Call<Login> loginCall = networkService.userLogin(userInfo);

            loginCall.enqueue(new Callback<Login>() {
                @Override
                public void onResponse(Call<Login> call, Response<Login> response) {
                    if (response.isSuccessful()) {
                        SharedPreferencesService.getInstance().setPrefData(AUTH_TOKEN, response.body().getResultData().getToken());
                        SharedPreferencesService.getInstance().setPrefData(USER_NAME, response.body().getResultData().getName());
                        int userStatus = response.body().getResultData().getCategory();
                        Intent loginResult = new Intent();
                        SharedPreferencesService.getInstance().setPrefData(USER_STATUS, userStatus);
                        loginResult.putExtra(LOGIN_RESULT, userStatus);
                        setResult(RESULT_OK, loginResult);
                        finish();
                    }
                }

                @Override
                public void onFailure(Call<Login> call, Throwable t) {
                    LogUtil.d(getApplicationContext(), t.toString());
                }
            });
        }

        public void redirectMainActivity(String profileImage, long kakaoId, String email) {
            Intent intent = new Intent(getApplicationContext(), SignUpMainActivity.class);
            TempLogin tempLogin = new TempLogin(email, String.valueOf(kakaoId), profileImage, LOGIN_KAKAO);
            intent.putExtra("tempLoginInfo", new Gson().toJson(tempLogin).toString());
            startActivity(intent);
            finish();
        }

        protected void redirectLoginActivity() {
            final Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
            finish();
        }
    }

    private void testLogin(String email, int category) {
        UserInfo userInfo = new UserInfo();
        userInfo.setEmail(email);
        userInfo.setUid(String.valueOf(111));
        userInfo.setCategory(category);
        userInfo.setDeviceToken(SharedPreferencesService.getInstance().getPrefStringData("fcm_token"));

        Call<Login> loginCall = networkService.userLogin(userInfo);

        loginCall.enqueue(new Callback<Login>() {
            @Override
            public void onResponse(Call<Login> call, Response<Login> response) {
                if (response.isSuccessful()) {
                    SharedPreferencesService.getInstance().setPrefData(AUTH_TOKEN, response.body().getResultData().getToken());
                    SharedPreferencesService.getInstance().setPrefData(USER_NAME, response.body().getResultData().getName());
                    int userStatus = response.body().getResultData().getCategory();
                    Intent loginResult = new Intent();
                    SharedPreferencesService.getInstance().setPrefData(USER_STATUS, userStatus);
                    loginResult.putExtra(LOGIN_RESULT, userStatus);
                    setResult(RESULT_OK, loginResult);
                    finish();
                }
            }

            @Override
            public void onFailure(Call<Login> call, Throwable t) {
                LogUtil.d(getApplicationContext(), t.toString());
            }
        });
    }
}
