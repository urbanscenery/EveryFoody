package dct.com.everyfoody.base.util;

/**
 * Created by jyoung on 2017. 8. 12..
 */

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import dct.com.everyfoody.R;

import static com.facebook.FacebookSdk.getApplicationContext;


public class CustomPDialog extends Dialog {

    @BindView(R.id.mini_truck)ImageView miniTruck;

    public CustomPDialog(Context context) {
        super(context, android.R.style.Theme_Translucent_NoTitleBar);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        lpWindow.dimAmount = 0.8f;
        getWindow().setAttributes(lpWindow);
        setContentView(R.layout.dialog_move_truck);
        ButterKnife.bind(this);

        setAnimation();
    }

    private void setAnimation(){
        Animation truckAnim = AnimationUtils.loadAnimation
                (getApplicationContext(),
                        R.anim.mini_truck_move);

        miniTruck.startAnimation(truckAnim);
    }


}


