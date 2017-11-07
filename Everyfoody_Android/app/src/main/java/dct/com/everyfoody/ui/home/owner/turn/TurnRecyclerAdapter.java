package dct.com.everyfoody.ui.home.owner.turn;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import dct.com.everyfoody.R;
import dct.com.everyfoody.model.Turn;

import static dct.com.everyfoody.ui.notification.NotiRecyclerAdapter.calculateTime;

/**
 * Created by jyoung on 2017. 10. 4..
 */

public class TurnRecyclerAdapter extends RecyclerView.Adapter {
    private List<Turn.TurnInfo> turnInfoList;
    private Context context;

    public void refreshAdapter(List<Turn.TurnInfo> turnInfoList) {
        this.turnInfoList = turnInfoList;
        notifyDataSetChanged();
    }

    public TurnRecyclerAdapter(List<Turn.TurnInfo> turnInfoList, Context context) {
        this.turnInfoList = turnInfoList;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_turn_rcv_item, parent, false);
        return new TurnItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        try {
            ((TurnItemViewHolder) holder).bindView(turnInfoList.get(position));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return turnInfoList != null ? turnInfoList.size() : 0;
    }


    public class TurnItemViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.turn_index)
        TextView turnIndex;
        @BindView(R.id.turn_user)
        TextView turnUser;
        @BindView(R.id.turn_time)
        TextView turnTime;
        @BindView(R.id.turn_border)
        RelativeLayout turnBorder;


        public TurnItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bindView(Turn.TurnInfo turnInfoItem) throws ParseException {
            turnIndex.setText((getAdapterPosition() + 1) + "");
            String resultTime = turnInfoItem.getReservationTime();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = format.parse(resultTime);

            turnTime.setText(calculateTime(date).toString());
            turnUser.setText(turnInfoItem.getUserNickname() + getPhoneNum(turnInfoItem.getUserPhone()));
            if (getAdapterPosition() % 2 == 1) {
                turnBorder.setBackgroundResource(R.drawable.owner_home_background2);
                turnUser.setTextColor(context.getResources().getColor(R.color.colorAccent));
                turnIndex.setTextColor(context.getResources().getColor(R.color.colorAccent));
                turnTime.setTextColor(context.getResources().getColor(R.color.colorAccent));

            }
        }

        private String getPhoneNum(String phone) {
            String tempPhone[] = phone.split("-");
            return "(" + tempPhone[2] + ")";
        }
    }
}
