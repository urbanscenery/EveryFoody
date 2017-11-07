package dct.com.everyfoody.ui.detail.review;

/**
 * Created by jyoung on 2017. 8. 12..
 */

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.RatingBar;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dct.com.everyfoody.R;
import dct.com.everyfoody.base.util.SharedPreferencesService;
import dct.com.everyfoody.global.ApplicationController;
import dct.com.everyfoody.request.NetworkService;


public class ReviewDialog extends Dialog {

    private NetworkService networkService;
    private View.OnClickListener onClickListener;

    @BindView(R.id.review_yes)TextView reviewYes;
    @BindView(R.id.regiter_review_rating)RatingBar ratingBar;

    public ReviewDialog(Context context, View.OnClickListener onClickListener) {
        super(context, android.R.style.Theme_Translucent_NoTitleBar);
        this.onClickListener = onClickListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        lpWindow.dimAmount = 0.8f;
        getWindow().setAttributes(lpWindow);
        setContentView(R.layout.dialog_review);
        ButterKnife.bind(this);
        networkService = ApplicationController.getInstance().getNetworkService();
        SharedPreferencesService.getInstance().load(getContext());

        reviewYes.setOnClickListener(onClickListener);
    }

    @OnClick(R.id.review_no)
    void noClick(View view){
        this.dismiss();
    }


}


