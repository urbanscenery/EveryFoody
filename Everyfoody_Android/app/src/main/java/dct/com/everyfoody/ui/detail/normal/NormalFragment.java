package dct.com.everyfoody.ui.detail.normal;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dct.com.everyfoody.R;
import dct.com.everyfoody.model.StoreInfo;

/**
 * Created by jyoung on 2017. 10. 4..
 */

public class NormalFragment extends Fragment {
    @BindView(R.id.store_info_tag)TextView hashTag;
    @BindView(R.id.opening_hours)TextView opeingTime;
    @BindView(R.id.break_hours)TextView breakTime;
    @BindView(R.id.phone_number)TextView phoneNum;
    @BindView(R.id.menu_rcv)RecyclerView menuRecycler;

    private StoreInfo storeInfo;
    private String infoJson;
    private List<StoreInfo.MenuInfo> menuInfoList;

    public NormalFragment() {
    }

    public static NormalFragment getInstance(){
        return new NormalFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_normal_info, null);
        ButterKnife.bind(this, view);

        getBundle();
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ViewCompat.setNestedScrollingEnabled(menuRecycler, false);
        initData();


    }

    private void initData(){
        hashTag.setText(storeInfo.getDetailInfo().getBasicInfo().getStoreHashtag());
        opeingTime.setText(storeInfo.getDetailInfo().getBasicInfo().getStoreOpentime());
        breakTime.setText(storeInfo.getDetailInfo().getBasicInfo().getStoreBreaktime());
        phoneNum.setText(storeInfo.getDetailInfo().getBasicInfo().getStorePhone());

        menuRecycler.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        menuRecycler.setAdapter(new MenuRecyclerAdapter(menuInfoList));

    }

    private void getBundle(){
        Gson gson = new Gson();
        infoJson = getArguments().getString("storeInfo");
        storeInfo = gson.fromJson(infoJson, StoreInfo.class);
        menuInfoList = new ArrayList<>();
        menuInfoList = storeInfo.getDetailInfo().getMenuInfo();
    }

    @OnClick(R.id.facebook_link)
    public void onClickFacebook(View view){
        if(!TextUtils.isEmpty(storeInfo.getDetailInfo().getBasicInfo().getStoreFacebookURL())) {
            Intent facebookIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(storeInfo.getDetailInfo().getBasicInfo().getStoreFacebookURL()));
            startActivity(facebookIntent);
        }
    }

    @OnClick(R.id.twitter_link)
    public void onClickTwitter(View view){
        if(!TextUtils.isEmpty(storeInfo.getDetailInfo().getBasicInfo().getStoreTwitterURL())) {
            Intent twitterIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(storeInfo.getDetailInfo().getBasicInfo().getStoreTwitterURL()));
            startActivity(twitterIntent);
        }
    }

    @OnClick(R.id.instagram_link)
    public void onClickInstagram(View view){
        if(!TextUtils.isEmpty(storeInfo.getDetailInfo().getBasicInfo().getStoreInstagramURL())) {
            Intent instagramIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(storeInfo.getDetailInfo().getBasicInfo().getStoreInstagramURL()));
            startActivity(instagramIntent);
        }
    }

}
