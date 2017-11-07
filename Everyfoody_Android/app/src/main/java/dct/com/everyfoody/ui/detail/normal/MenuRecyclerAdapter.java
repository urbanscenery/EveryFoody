package dct.com.everyfoody.ui.detail.normal;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import dct.com.everyfoody.R;
import dct.com.everyfoody.model.StoreInfo;

/**
 * Created by jyoung on 2017. 10. 4..
 */

public class MenuRecyclerAdapter extends RecyclerView.Adapter {
    private List<StoreInfo.MenuInfo> menuInfoList;

    public void refreshAdapter(List<StoreInfo.MenuInfo> menuInfo) {
        this.menuInfoList = menuInfo;
    }

    public MenuRecyclerAdapter(List<StoreInfo.MenuInfo> menuInfoList) {
        this.menuInfoList = menuInfoList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_menu_rcv_item, parent, false);
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
        @BindView(R.id.menu_image)
        ImageView menuImage;
        @BindView(R.id.menu_name)
        TextView menuName;
        @BindView(R.id.menu_price)
        TextView menuPrice;


        public MenuViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bindView(StoreInfo.MenuInfo menuInfo) {
            Glide.with(menuImage.getContext())
                    .load(menuInfo.getMenuImageURL())
                    .into(menuImage);

            menuName.setText(menuInfo.getMenuTitle());
            menuPrice.setText(menuInfo.getMenuPrice()+"원");
        }


    }
}
