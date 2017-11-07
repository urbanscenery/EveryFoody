package dct.com.everyfoody.base;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import dct.com.everyfoody.R;

/**
 * Created by jyoung on 2017. 10. 7..
 */

public class WhiteThemeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.WhiteTheme);

        View view = getWindow().getDecorView();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (view != null) {
                // 23 버전 이상
                view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }
        }else if (Build.VERSION.SDK_INT >= 21) {
            // 21 버전 이상
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary));
        }
    }
}
