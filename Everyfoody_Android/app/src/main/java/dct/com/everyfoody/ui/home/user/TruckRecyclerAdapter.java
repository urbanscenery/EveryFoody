package dct.com.everyfoody.ui.home.user;

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
import dct.com.everyfoody.model.MainList;

/**
 * Created by jyoung on 2017. 10. 4..
 */

public class TruckRecyclerAdapter extends RecyclerView.Adapter {
    private List<MainList.TruckList> truckLists;
    private View.OnClickListener onClickListener;

    public void refreshAdapter(List<MainList.TruckList> truckLists){
        this.truckLists = truckLists;
        notifyDataSetChanged();
    }

    public TruckRecyclerAdapter(List<MainList.TruckList> truckLists, View.OnClickListener onClickListener) {
        this.truckLists = truckLists;
        this.onClickListener = onClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_main_rcv_item, parent, false);
        view.setOnClickListener(onClickListener);
        return new MainTruckListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((MainTruckListViewHolder)holder).bindView(truckLists.get(position));
    }

    @Override
    public int getItemCount() {
        return truckLists != null ? truckLists.size() : 0;
    }


    public class MainTruckListViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.main_list_item_image)
        ImageView mainListImage;
        @BindView(R.id.food_truck_name)
        TextView foodTruckName;
        @BindView(R.id.food_truck_distance)
        TextView foodTruckDistance;
        @BindView(R.id.food_truck_booker_count)
        TextView foodTruckBookerCount;


        public MainTruckListViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bindView(MainList.TruckList truckList){
            Glide.with(mainListImage.getContext()).load(truckList.getStoreImage()).into(mainListImage);
            foodTruckName.setText(truckList.getStoreName());
            if(truckList.getStoreDistance() == -1) {
                foodTruckDistance.setText("오픈하지 않음");
                foodTruckBookerCount.setText("대기인원 0명");
            }
            else {
                foodTruckDistance.setText(truckList.getStoreDistance() + truckList.getStoreDistanceUnit());
                foodTruckBookerCount.setText("대기인원 " + truckList.getReservationCount() + "명");
            }
        }


    }
}
