package dct.com.everyfoody.ui.bookmark;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import dct.com.everyfoody.R;
import dct.com.everyfoody.base.WhiteThemeActivity;
import dct.com.everyfoody.base.util.LogUtil;
import dct.com.everyfoody.base.util.SharedPreferencesService;
import dct.com.everyfoody.global.ApplicationController;
import dct.com.everyfoody.model.MainList;
import dct.com.everyfoody.request.NetworkService;
import dct.com.everyfoody.ui.detail.DetailActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static dct.com.everyfoody.ui.detail.location.MapActivity.DEFAULT_LAT;
import static dct.com.everyfoody.ui.detail.location.MapActivity.DEFAULT_LNG;
import static dct.com.everyfoody.ui.login.LoginActivity.AUTH_TOKEN;
import static dct.com.everyfoody.ui.login.LoginActivity.NETWORK_SUCCESS;

public class BookmarkActivity extends WhiteThemeActivity {
    @BindView(R.id.bookmark_toolbar)
    Toolbar bookmarkToolbar;
    @BindView(R.id.bookmark_rcv)
    RecyclerView bookmarkRecycler;
    @BindView(R.id.warning_layout)
    View warningLayout;

    private NetworkService networkService;
    private List<MainList.TruckList> bookmarkList;
    private BookmarkRecyclerAdapter bookmarkRecyclerAdapter;
    private NonDataWarning nonDataWarning;
    private double lat, lng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmark);
        ButterKnife.bind(this);
        networkService = ApplicationController.getInstance().getNetworkService();
        SharedPreferencesService.getInstance().load(this);
        setToolbar();
        setRecycler();
        getBookmarkList();
    }


    private void getBookmarkList() {
        if (TextUtils.isEmpty(SharedPreferencesService.getInstance().getPrefStringData("lat"))) {
            lat = DEFAULT_LAT;
            lng = DEFAULT_LNG;
        } else {
            lat = Double.valueOf(SharedPreferencesService.getInstance().getPrefStringData("lat"));
            lng = Double.valueOf(SharedPreferencesService.getInstance().getPrefStringData("lng"));
        }

        Call<MainList> bookmarkListCall = networkService.getBookmarkList(SharedPreferencesService.getInstance().getPrefStringData(AUTH_TOKEN), lat, lng);

        bookmarkListCall.enqueue(new Callback<MainList>() {
            @Override
            public void onResponse(Call<MainList> call, Response<MainList> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals(NETWORK_SUCCESS)) {
                        bookmarkList = response.body().getData().getTruckLists();
                        bookmarkRecyclerAdapter.refreshAdapter(bookmarkList);

                        if (bookmarkList.size() == 0)
                            ifemptyData();
                    }
                }
            }

            @Override
            public void onFailure(Call<MainList> call, Throwable t) {
                LogUtil.d(getApplicationContext(), t.toString());
            }
        });

    }

    private void setRecycler() {
        bookmarkList = new ArrayList<MainList.TruckList>();
        bookmarkRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        bookmarkRecyclerAdapter = new BookmarkRecyclerAdapter(bookmarkList, onClickListener);
        bookmarkRecycler.setAdapter(bookmarkRecyclerAdapter);
    }

    private void setToolbar() {
        bookmarkToolbar.setTitle("");
        setSupportActionBar(bookmarkToolbar);
        bookmarkToolbar.setNavigationIcon(getResources().getDrawable(R.drawable.back));
        bookmarkToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            final int tempPosition = bookmarkRecycler.getChildPosition(view);
            Intent detailIntent = new Intent(view.getContext(), DetailActivity.class);
            detailIntent.putExtra("storeId", bookmarkList.get(tempPosition).getStoreID());
            startActivity(detailIntent);
        }
    };

    public static class NonDataWarning {
        @BindView(R.id.non_data_warning_text)
        public TextView warningText;
    }

    private void ifemptyData() {
        nonDataWarning = new NonDataWarning();
        ButterKnife.bind(nonDataWarning, warningLayout);
        bookmarkRecycler.setVisibility(View.INVISIBLE);
        warningLayout.setVisibility(View.VISIBLE);
        nonDataWarning.warningText.setText("새로운 알림이 없습니다.");
    }
}
