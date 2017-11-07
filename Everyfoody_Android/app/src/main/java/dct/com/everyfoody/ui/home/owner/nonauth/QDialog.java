package dct.com.everyfoody.ui.home.owner.nonauth;

/**
 * Created by jyoung on 2017. 8. 12..
 */

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.WindowManager;

import butterknife.ButterKnife;
import dct.com.everyfoody.R;


public class QDialog extends Dialog {

    public QDialog(Context context) {
        super(context, android.R.style.Theme_Translucent_NoTitleBar);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        lpWindow.dimAmount = 0.8f;
        getWindow().setAttributes(lpWindow);
        setContentView(R.layout.dialog_question);
        ButterKnife.bind(this);
    }


}


