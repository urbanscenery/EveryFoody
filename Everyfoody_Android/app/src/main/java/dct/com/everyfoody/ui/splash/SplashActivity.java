package dct.com.everyfoody.ui.splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import dct.com.everyfoody.R;
import dct.com.everyfoody.base.util.SharedPreferencesService;
import dct.com.everyfoody.ui.home.owner.OwnerHomeActivity;
import dct.com.everyfoody.ui.home.owner.nonauth.NonAuthOwnerActivity;
import dct.com.everyfoody.ui.home.user.MainActivity;

import static dct.com.everyfoody.ui.login.LoginActivity.RESULT_GUEST;
import static dct.com.everyfoody.ui.login.LoginActivity.RESULT_NON_AUTH_OWNER;
import static dct.com.everyfoody.ui.login.LoginActivity.RESULT_NO_REG_STORE;
import static dct.com.everyfoody.ui.login.LoginActivity.RESULT_OWNER;
import static dct.com.everyfoody.ui.login.LoginActivity.USER_STATUS;

public class SplashActivity extends AppCompatActivity {
    @BindView(R.id.truck_image)ImageView truckImage;
    @BindView(R.id.food_icon)ImageView foodIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        SharedPreferencesService.getInstance().load(this);
        setAnimation();
        appStart();
    }

    private void appStart(){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                finish();
                switch (SharedPreferencesService.getInstance().getPrefIntegerData(USER_STATUS)){
                    case RESULT_NON_AUTH_OWNER:case RESULT_NO_REG_STORE:
                        startActivity(new Intent(getApplicationContext(), NonAuthOwnerActivity.class));
                        break;
                    case RESULT_OWNER:
                        startActivity(new Intent(getApplicationContext(), OwnerHomeActivity.class));
                        break;
                    case RESULT_GUEST:default:
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        break;
                }
            }
        }, 1900);
    }

    private void setAnimation(){
        Animation truckAnim = AnimationUtils.loadAnimation
                (getApplicationContext(),
                        R.anim.truck_move);
        Animation iconAnim = AnimationUtils.loadAnimation
                (getApplicationContext(),
                        R.anim.fade_in);

        truckImage.startAnimation(truckAnim);
        foodIcon.startAnimation(iconAnim);
        iconAnim.setStartOffset(1000);
    }
}
