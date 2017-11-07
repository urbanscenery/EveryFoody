package dct.com.everyfoody.ui.notification;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import dct.com.everyfoody.R;
import dct.com.everyfoody.base.WhiteThemeActivity;
import dct.com.everyfoody.base.util.LogUtil;
import dct.com.everyfoody.base.util.SharedPreferencesService;
import dct.com.everyfoody.global.ApplicationController;
import dct.com.everyfoody.model.Notification;
import dct.com.everyfoody.request.NetworkService;
import dct.com.everyfoody.ui.bookmark.BookmarkActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static dct.com.everyfoody.base.util.FirebaseMessagingService.getLauncherClassName;
import static dct.com.everyfoody.ui.login.LoginActivity.AUTH_TOKEN;
import static dct.com.everyfoody.ui.login.LoginActivity.NETWORK_SUCCESS;

public class NotifyActivity extends WhiteThemeActivity implements SwipeRefreshLayout.OnRefreshListener {
    @BindView(R.id.notification_toolbar)
    Toolbar notiToolbar;
    @BindView(R.id.notification_rcv)
    RecyclerView notiRecycler;
    @BindView(R.id.noti_srl)
    SwipeRefreshLayout notiRefreshLayout;
    @BindView(R.id.warning_layout)
    View warningLayout;

    private NetworkService networkService;
    private NotiRecyclerAdapter notiRecyclerAdapter;
    private List<Notification.Noti> notiList;
    private BookmarkActivity.NonDataWarning nonDataWarning;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notify);
        ButterKnife.bind(this);
        networkService = ApplicationController.getInstance().getNetworkService();
        SharedPreferencesService.getInstance().load(this);
        setToolbar();
        setRecycler();
        getNotiList();
        setRefresh();
    }

    private void setRefresh() {
        notiRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorAccent));
        notiRefreshLayout.setOnRefreshListener(this);
    }

    private void ifemptyData() {
        nonDataWarning = new BookmarkActivity.NonDataWarning();
        ButterKnife.bind(nonDataWarning, warningLayout);
        notiRecycler.setVisibility(View.INVISIBLE);
        warningLayout.setVisibility(View.VISIBLE);
        nonDataWarning.warningText.setText("등록된 후기가 없습니다.");
    }

    private void getNotiList() {
        Call<Notification> notificationCall = networkService.getNotiList(SharedPreferencesService.getInstance().getPrefStringData(AUTH_TOKEN));

        notificationCall.enqueue(new Callback<Notification>() {
            @Override
            public void onResponse(Call<Notification> call, Response<Notification> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals(NETWORK_SUCCESS)) {
                        notiList = response.body().getNotiList();
                        notiRecyclerAdapter.refreshAdapter(notiList);

                        if (notiList.size() == 0)
                            ifemptyData();
                        else {
                            notiRecycler.setVisibility(View.VISIBLE);
                            warningLayout.setVisibility(View.INVISIBLE);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<Notification> call, Throwable t) {
                LogUtil.d(getApplicationContext(), t.toString());
            }
        });


    }

    private void setRecycler() {
        notiList = new ArrayList<Notification.Noti>();
        notiRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        notiRecyclerAdapter = new NotiRecyclerAdapter(notiList);
        notiRecycler.setAdapter(notiRecyclerAdapter);
    }

    private void setToolbar() {
        notiToolbar.setTitle("");
        setSupportActionBar(notiToolbar);
        notiToolbar.setNavigationIcon(getResources().getDrawable(R.drawable.back));
        notiToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (SharedPreferencesService.getInstance().getPrefIntegerData("badgeCount") > 0)
            SharedPreferencesService.getInstance().setPrefData("badgeCount", 0);

        Intent badgeIntent = new Intent("android.intent.action.BADGE_COUNT_UPDATE");
        badgeIntent.putExtra("badge_count", SharedPreferencesService.getInstance().getPrefIntegerData("badgeCount"));
        badgeIntent.putExtra("badge_count_package_name", getPackageName());
        badgeIntent.putExtra("badge_count_class_name", getLauncherClassName(getApplicationContext()));
        sendBroadcast(badgeIntent);
    }

    @Override
    public void onRefresh() {
        getNotiList();
        notiRefreshLayout.setRefreshing(false);
    }
}
