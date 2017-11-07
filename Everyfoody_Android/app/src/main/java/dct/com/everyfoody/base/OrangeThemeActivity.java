package dct.com.everyfoody.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import dct.com.everyfoody.R;

/**
 * Created by jyoung on 2017. 10. 7..
 */

public class OrangeThemeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.OrangeTheme);
    }
}
