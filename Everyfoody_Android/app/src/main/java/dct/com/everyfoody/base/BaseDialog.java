package dct.com.everyfoody.base;

/**
 * Created by jyoung on 2017. 8. 12..
 */

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dct.com.everyfoody.R;
import dct.com.everyfoody.base.util.SharedPreferencesService;
import dct.com.everyfoody.global.ApplicationController;
import dct.com.everyfoody.request.NetworkService;


public class BaseDialog extends Dialog {

    private NetworkService networkService;
    private View.OnClickListener onClickListener;
    private String dialogContent;

    @BindView(R.id.dialog_yes)TextView yesBtn;
    @BindView(R.id.dialog_text)TextView dialogText;

    public BaseDialog(Context context, View.OnClickListener onClickListener, String dialogContent) {
        super(context, android.R.style.Theme_Translucent_NoTitleBar);
        this.onClickListener = onClickListener;
        this.dialogContent = dialogContent;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        lpWindow.dimAmount = 0.8f;
        getWindow().setAttributes(lpWindow);
        setContentView(R.layout.dialog_reservation);
        ButterKnife.bind(this);
        networkService = ApplicationController.getInstance().getNetworkService();
        SharedPreferencesService.getInstance().load(getContext());

        dialogText.setText(dialogContent);
        yesBtn.setOnClickListener(onClickListener);
    }

    @OnClick(R.id.dialog_no)
    void noClick(View view){
        this.dismiss();
    }


}


