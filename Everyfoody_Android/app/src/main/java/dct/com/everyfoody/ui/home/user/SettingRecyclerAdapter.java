package dct.com.everyfoody.ui.home.user;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import dct.com.everyfoody.R;
import dct.com.everyfoody.base.BaseModel;
import dct.com.everyfoody.base.util.LogUtil;
import dct.com.everyfoody.base.util.SharedPreferencesService;
import dct.com.everyfoody.global.ApplicationController;
import dct.com.everyfoody.model.SideMenu;
import dct.com.everyfoody.request.NetworkService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static dct.com.everyfoody.ui.home.user.MainActivity.TOGGLE_CHECKED;
import static dct.com.everyfoody.ui.home.user.MainActivity.TOGGLE_UNCHECKED;
import static dct.com.everyfoody.ui.login.LoginActivity.AUTH_TOKEN;
import static dct.com.everyfoody.ui.login.LoginActivity.NETWORK_SUCCESS;

/**
 * Created by jyoung on 2017. 10. 4..
 */

public class SettingRecyclerAdapter extends RecyclerView.Adapter {
    private List<SideMenu.BookMark> bookMarkList;
    private Context mContext;

    public void refreshAdapter(List<SideMenu.BookMark> bookMarkList){
        this.bookMarkList = bookMarkList;
        notifyDataSetChanged();
    }

    public SettingRecyclerAdapter(List<SideMenu.BookMark> bookMarkList, Context mContext) {
        this.bookMarkList = bookMarkList;
        this.mContext = mContext;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_push_list_rcv_item, parent, false);
        return new SettingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((SettingViewHolder)holder).bindView(bookMarkList.get(position));
    }

    @Override
    public int getItemCount() {
        return bookMarkList != null ? bookMarkList.size() : 0;
    }


    public class SettingViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.push_item_name)TextView storeName;
        @BindView(R.id.push_item_switch)SwitchCompat pushSwitch;

        private NetworkService networkService;

        public SettingViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            networkService = ApplicationController.getInstance().getNetworkService();
            SharedPreferencesService.getInstance().load(mContext);

        }

        public void bindView(SideMenu.BookMark sideItem){
            storeName.setText(sideItem.getStoreName());

            if(sideItem.getToggle() == TOGGLE_CHECKED)
            pushSwitch.setChecked(true);
            else if(sideItem.getToggle() == TOGGLE_UNCHECKED)
                pushSwitch.setChecked(false);


            pushSwitch.setOnCheckedChangeListener(changeListener);
        }

        private CompoundButton.OnCheckedChangeListener changeListener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (pushSwitch.isChecked()) {
                    switchChecked();
                }
                else if(!pushSwitch.isChecked()){
                    switchUnChecked();
                }
            }
        };

        private void switchChecked(){
            Call<BaseModel> checkedCall = networkService.checkedToggle(SharedPreferencesService.getInstance().getPrefStringData(AUTH_TOKEN), bookMarkList.get(getAdapterPosition()).getStoreId());

            checkedCall.enqueue(new Callback<BaseModel>() {
                @Override
                public void onResponse(Call<BaseModel> call, Response<BaseModel> response) {
                    if(response.isSuccessful()){
                        if(response.body().getStatus().equals(NETWORK_SUCCESS)){
                        }
                   }
                }

                @Override
                public void onFailure(Call<BaseModel> call, Throwable t) {
                    LogUtil.d(mContext, t.toString());
                }
            });
        }

        private void switchUnChecked(){
            Call<BaseModel> unCheckedCall = networkService.unCheckedToggle(SharedPreferencesService.getInstance().getPrefStringData(AUTH_TOKEN), bookMarkList.get(getAdapterPosition()).getStoreId());

            unCheckedCall.enqueue(new Callback<BaseModel>() {
                @Override
                public void onResponse(Call<BaseModel> call, Response<BaseModel> response) {
                    if(response.isSuccessful()){
                        if(response.body().getStatus().equals(NETWORK_SUCCESS)){
                        }
                    }
                }

                @Override
                public void onFailure(Call<BaseModel> call, Throwable t) {
                    LogUtil.d(mContext, t.toString());
                }
            });
        }
    }
}
