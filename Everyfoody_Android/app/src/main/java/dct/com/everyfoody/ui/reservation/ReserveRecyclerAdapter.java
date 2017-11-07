package dct.com.everyfoody.ui.reservation;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import dct.com.everyfoody.R;
import dct.com.everyfoody.base.BaseDialog;
import dct.com.everyfoody.base.BaseModel;
import dct.com.everyfoody.base.util.LogUtil;
import dct.com.everyfoody.base.util.SharedPreferencesService;
import dct.com.everyfoody.global.ApplicationController;
import dct.com.everyfoody.model.Reservation;
import dct.com.everyfoody.request.NetworkService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static dct.com.everyfoody.ui.login.LoginActivity.AUTH_TOKEN;
import static dct.com.everyfoody.ui.login.LoginActivity.NETWORK_SUCCESS;
import static dct.com.everyfoody.ui.notification.NotiRecyclerAdapter.calculateTime;


/**
 * Created by jyoung on 2017. 10. 4..
 */

public class ReserveRecyclerAdapter extends RecyclerView.Adapter {
    private List<Reservation.Store> reservationList;
    private View.OnClickListener onClickListener;
    private Context context;
    private Activity activity;
    private int holderType;

    private final int NORMAL = 800;
    private final int CANCEL = 801;

    public void refreshAdapter(List<Reservation.Store> reservationList) {
        this.reservationList = reservationList;
        notifyDataSetChanged();
    }

    public ReserveRecyclerAdapter(List<Reservation.Store> reservationList, View.OnClickListener onClickListener, Activity activity) {
        this.reservationList = reservationList;
        this.onClickListener = onClickListener;
        this.activity = activity;
        holderType = NORMAL;
    }

    public ReserveRecyclerAdapter(List<Reservation.Store> reservationList, View.OnClickListener onClickListener, Context context, Activity activity) {
        this.reservationList = reservationList;
        this.onClickListener = onClickListener;
        this.context = context;
        this.activity = activity;
        holderType = CANCEL;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_reservation_rcv_item, parent, false);
        view.setOnClickListener(onClickListener);
        return new ReserveViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        try {
            if (holderType == NORMAL)
                ((ReserveViewHolder) holder).normalBindView(reservationList.get(position));

            else
                ((ReserveViewHolder) holder).cancelBindView(reservationList.get(position));
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return reservationList != null ? reservationList.size() : 0;
    }

    public class ReserveViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.reservation_item_image)
        ImageView reservationItemImage;
        @BindView(R.id.reservation_truck_name)
        TextView reservationTruckName;
        @BindView(R.id.reservation_count)
        TextView reservationCount;
        @BindView(R.id.reservation_time)
        TextView reservationTime;
        @BindView(R.id.reservation_delete)
        ImageView reservationDelete;

        private NetworkService networkService;
        private BaseDialog cancelReservationDialog;

        public ReserveViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            networkService = ApplicationController.getInstance().getNetworkService();
            SharedPreferencesService.getInstance().load(context);
            cancelReservationDialog = new BaseDialog(activity, cancelClickListener, "예약내역을 취소하시겠습니까?");

        }

        public void normalBindView(Reservation.Store reservationItem) throws ParseException {
            Glide.with(reservationItemImage.getContext()).load(reservationItem.getStoreImageURL()).into(reservationItemImage);
            reservationTruckName.setText(reservationItem.getStoreName());
            reservationCount.setText(reservationItem.getReservationCount() + "");
            String resultTime = reservationItem.getReservationTime();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = format.parse(resultTime);

            reservationTime.setText(calculateTime(date).toString());
        }

        public void cancelBindView(final Reservation.Store reservationItem) throws ParseException {
            Glide.with(reservationItemImage.getContext()).load(reservationItem.getStoreImageURL()).into(reservationItemImage);
            reservationTruckName.setText(reservationItem.getStoreName());
            reservationCount.setText(reservationItem.getReservationCount() + "");

            String resultTime = reservationItem.getReservationTime();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = format.parse(resultTime);

            reservationTime.setText(calculateTime(date).toString());

            reservationDelete.setVisibility(View.VISIBLE);
            reservationDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    cancelReservationDialog.show();
                }
            });
        }

        private View.OnClickListener cancelClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Call<BaseModel> cancelCall = networkService.userReseve(SharedPreferencesService.getInstance().getPrefStringData(AUTH_TOKEN), reservationList.get(getAdapterPosition()).getStoreID());

                cancelCall.enqueue(new Callback<BaseModel>() {
                    @Override
                    public void onResponse(Call<BaseModel> call, Response<BaseModel> response) {
                        if (response.isSuccessful()) {
                            if (response.body().getStatus().equals(NETWORK_SUCCESS)) {
                                reservationList.remove(getAdapterPosition());
                                notifyDataSetChanged();
                                cancelReservationDialog.dismiss();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<BaseModel> call, Throwable t) {
                        LogUtil.d(context, t.toString());
                    }
                });
            }
        };
    }
}
