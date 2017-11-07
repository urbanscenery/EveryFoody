package dct.com.everyfoody.ui.notification;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import dct.com.everyfoody.R;
import dct.com.everyfoody.model.Notification;

/**
 * Created by jyoung on 2017. 10. 4..
 */

public class NotiRecyclerAdapter extends RecyclerView.Adapter {
    private List<Notification.Noti> notiList;

    public void refreshAdapter(List<Notification.Noti> notiList){
        this.notiList = notiList;
        notifyDataSetChanged();
    }

    public NotiRecyclerAdapter(List<Notification.Noti> notiList) {
        this.notiList = notiList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_notification_rcv_item, parent, false);
        return new NotiListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        try {
            ((NotiListViewHolder)holder).bindView(notiList.get(position));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return notiList != null ? notiList.size() : 0;
    }


    public class NotiListViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.noti_content)TextView content;
        @BindView(R.id.noti_time)TextView time;

        public NotiListViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bindView(Notification.Noti notiItem) throws ParseException {
            content.setText(notiItem.getNoticeContent());
            time.setText(notiItem.getNoticeTime());

            String resultTime = notiItem.getNoticeTime();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = format.parse(resultTime);

            time.setText(calculateTime(date).toString());
        }



    }

    public static String calculateTime(Date date)
    {

        long curTime = System.currentTimeMillis();
        long regTime = date.getTime();
        long diffTime = (curTime - regTime) / 1000;

        String msg = null;

        if (diffTime < TIME_MAXIMUM.SEC)
        {
            // sec
            msg = "방금 전";
        }
        else if ((diffTime /= TIME_MAXIMUM.SEC) < TIME_MAXIMUM.MIN)
        {
            // min
            System.out.println(diffTime);

            msg = diffTime + "분 전";
        }
        else if ((diffTime /= TIME_MAXIMUM.MIN) < TIME_MAXIMUM.HOUR)
        {
            // hour
            msg = (diffTime ) + "시간 전";
        }
        else if ((diffTime /= TIME_MAXIMUM.HOUR) < TIME_MAXIMUM.DAY)
        {
            // day
            msg = (diffTime ) + "일 전";
        }
        else if ((diffTime /= TIME_MAXIMUM.DAY) < TIME_MAXIMUM.MONTH)
        {
            // day
            msg = (diffTime ) + "달 전";
        }
        else
        {
            msg = (diffTime) + "년 전";
        }

        return msg;
    }

    private static class TIME_MAXIMUM
    {
        public static final int SEC = 60;
        public static final int MIN = 60;
        public static final int HOUR = 24;
        public static final int DAY = 30;
        public static final int MONTH = 12;
    }
}
