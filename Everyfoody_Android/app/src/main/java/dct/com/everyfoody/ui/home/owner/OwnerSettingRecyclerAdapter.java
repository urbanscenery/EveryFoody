package dct.com.everyfoody.ui.home.owner;

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

public class OwnerSettingRecyclerAdapter extends RecyclerView.Adapter {
    private List<Integer> ownerSettingList;
    private Context mContext;

    public void refreshAdapter(List<Integer> ownerSettingList) {
        this.ownerSettingList = ownerSettingList;
        notifyDataSetChanged();
    }

    public OwnerSettingRecyclerAdapter(List<Integer> ownerSettingList, Context mContext) {
        this.ownerSettingList = ownerSettingList;
        this.mContext = mContext;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_push_list_rcv_item, parent, false);
        return new OwnerSettingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((OwnerSettingViewHolder) holder).bindView(ownerSettingList.get(position));
    }

    @Override
    public int getItemCount() {
        return ownerSettingList != null ? ownerSettingList.size() : 0;
    }


    public class OwnerSettingViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.push_item_name)
        TextView storeName;
        @BindView(R.id.push_item_switch)
        SwitchCompat pushSwitch;

        private NetworkService networkService;

        private final int TOGGLE_TURN = 701;
        private final int TOGGLE_REVIEW = 702;
        private final int TOGGLE_BOOKMARK = 703;
        private int kind;

        public OwnerSettingViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            networkService = ApplicationController.getInstance().getNetworkService();
            SharedPreferencesService.getInstance().load(mContext);
        }

        public void bindView(int status) {
            switch (getAdapterPosition()) {
                case 0:
                    storeName.setText("순번 추가시");
                    kind = TOGGLE_TURN;
                    checkToggle(status);
                    break;
                case 1:
                    storeName.setText("후기 등록시");
                    kind = TOGGLE_REVIEW;
                    checkToggle(status);
                    break;
                case 2:
                    storeName.setText("즐겨찾기 추가시");
                    kind = TOGGLE_BOOKMARK;
                    checkToggle(status);
                    break;
            }

        }

        private CompoundButton.OnCheckedChangeListener changeListener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (pushSwitch.isChecked()) {
                    switchChecked();
                } else if (!pushSwitch.isChecked()) {
                    switchUnChecked();
                }
            }
        };


        public void checkToggle(int status) {
            if (status == TOGGLE_CHECKED)
                pushSwitch.setChecked(true);
            else if (status == TOGGLE_UNCHECKED)
                pushSwitch.setChecked(false);

            pushSwitch.setOnCheckedChangeListener(changeListener);

        }

        private void switchChecked() {
            Call<BaseModel> checkedCall = networkService.checkedToggleOwner(SharedPreferencesService.getInstance().getPrefStringData(AUTH_TOKEN), kind);

            checkedCall.enqueue(new Callback<BaseModel>() {
                @Override
                public void onResponse(Call<BaseModel> call, Response<BaseModel> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getStatus().equals(NETWORK_SUCCESS)) {
                        }
                    }
                }

                @Override
                public void onFailure(Call<BaseModel> call, Throwable t) {
                    LogUtil.d(mContext, t.toString());
                }
            });
        }

        private void switchUnChecked() {
            Call<BaseModel> unCheckedCall = networkService.unCheckedToggleOwner(SharedPreferencesService.getInstance().getPrefStringData(AUTH_TOKEN), kind);

            unCheckedCall.enqueue(new Callback<BaseModel>() {
                @Override
                public void onResponse(Call<BaseModel> call, Response<BaseModel> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getStatus().equals(NETWORK_SUCCESS)) {
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
