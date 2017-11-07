package dct.com.everyfoody.ui.detail.edit;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dct.com.everyfoody.R;
import dct.com.everyfoody.base.BaseModel;
import dct.com.everyfoody.base.util.LogUtil;
import dct.com.everyfoody.base.util.SharedPreferencesService;
import dct.com.everyfoody.base.util.ToastMaker;
import dct.com.everyfoody.global.ApplicationController;
import dct.com.everyfoody.model.StoreInfo;
import dct.com.everyfoody.request.NetworkService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static dct.com.everyfoody.ui.detail.edit.EditActivity.MENU_EDIT;
import static dct.com.everyfoody.ui.login.LoginActivity.AUTH_TOKEN;
import static dct.com.everyfoody.ui.login.LoginActivity.NETWORK_SUCCESS;

/**
 * Created by jyoung on 2017. 10. 4..
 */

public class EditMenuRecyclerAdapter extends RecyclerView.Adapter {
    private List<StoreInfo.MenuInfo> menuInfoList;
    private Context mContext;

    public EditMenuRecyclerAdapter(List<StoreInfo.MenuInfo> menuInfoList, Context mContext) {
        this.menuInfoList = menuInfoList;
        this.mContext = mContext;
    }

    public void refreshAdapter(List<StoreInfo.MenuInfo> menuInfo) {
        this.menuInfoList = menuInfo;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_menu_edit_rcv_item, parent, false);
        return new MenuViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((MenuViewHolder) holder).bindView(menuInfoList.get(position));
    }

    @Override
    public int getItemCount() {
        return menuInfoList != null ? menuInfoList.size() : 0;
    }


    public class MenuViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.edit_menu_image)
        ImageView menuImage;
        @BindView(R.id.edit_menu_name)
        TextView menuName;
        @BindView(R.id.edit_menu_price)
        TextView menuPrice;

        private NetworkService networkService;


        public MenuViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            networkService = ApplicationController.getInstance().getNetworkService();
            SharedPreferencesService.getInstance().load(itemView.getContext());
        }

        public void bindView(StoreInfo.MenuInfo menuInfo) {
            Glide.with(menuImage.getContext())
                    .load(menuInfo.getMenuImageURL())
                    .into(menuImage);

            menuName.setText(menuInfo.getMenuTitle());
            menuPrice.setText(menuInfo.getMenuPrice()+"원");
        }


        @OnClick(R.id.edit_edit_icon)
        public void onClickEdit(View view){
            Gson gson = new Gson();

            Intent editIntent = new Intent(mContext, EditMenuActivity.class);
            editIntent.putExtra("addORedit", MENU_EDIT);
            editIntent.putExtra("menuItem", gson.toJson(menuInfoList.get(getAdapterPosition())));
            ((Activity)mContext).startActivityForResult(editIntent, MENU_EDIT);
        }

        @OnClick(R.id.edit_delete_icon)
        public void onClickDelete(final View view){
            final int tempPosition = getAdapterPosition();

            Call<BaseModel> deleteCall = networkService.deleteMenu(SharedPreferencesService.getInstance().getPrefStringData(AUTH_TOKEN), menuInfoList.get(tempPosition).getMenuID());

            deleteCall.enqueue(new Callback<BaseModel>() {
                @Override
                public void onResponse(Call<BaseModel> call, Response<BaseModel> response) {
                    if(response.isSuccessful()){
                        if(response.body().getStatus().equals(NETWORK_SUCCESS)){
                            menuInfoList.remove(tempPosition);
                            notifyDataSetChanged();
                            ToastMaker.makeShortToast(view.getContext(), "메뉴가 삭제되었습니다.");
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
