package dct.com.everyfoody.ui.home.owner.turn;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import dct.com.everyfoody.R;
import dct.com.everyfoody.base.BaseDialog;
import dct.com.everyfoody.base.BaseModel;
import dct.com.everyfoody.base.WhiteThemeActivity;
import dct.com.everyfoody.base.util.LogUtil;
import dct.com.everyfoody.base.util.SharedPreferencesService;
import dct.com.everyfoody.global.ApplicationController;
import dct.com.everyfoody.model.Turn;
import dct.com.everyfoody.request.NetworkService;
import dct.com.everyfoody.ui.bookmark.BookmarkActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static dct.com.everyfoody.ui.login.LoginActivity.AUTH_TOKEN;
import static dct.com.everyfoody.ui.login.LoginActivity.NETWORK_SUCCESS;

public class TurnActivity extends WhiteThemeActivity implements SwipeRefreshLayout.OnRefreshListener {
    @BindView(R.id.turn_toolbar)Toolbar turnToolbar;
    @BindView(R.id.turn_rcv)RecyclerView turnRecycler;
    @BindView(R.id.warning_layout)View warningLayout;
    @BindView(R.id.turn_srl)SwipeRefreshLayout trunRefreshLayout;

    private BookmarkActivity.NonDataWarning nonDataWarning;
    private List<Turn.TurnInfo> turnInfoList;
    private NetworkService networkService;
    private TurnRecyclerAdapter turnRecyclerAdapter;
    private BaseDialog nextTurnDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_turn);
        ButterKnife.bind(this);
        networkService = ApplicationController.getInstance().getNetworkService();
        SharedPreferencesService.getInstance().load(this);
        turnInfoList = new ArrayList<>();
        setToolbar();
        setRecycler();
        getTurnList();
        setRefresh();

    }
    private void setRefresh(){
        trunRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorAccent));
        trunRefreshLayout.setOnRefreshListener(this);
    }

    private void ifemptyData(){
        nonDataWarning = new BookmarkActivity.NonDataWarning();
        ButterKnife.bind(nonDataWarning, warningLayout);
        turnRecycler.setVisibility(View.INVISIBLE);
        warningLayout.setVisibility(View.VISIBLE);
        nonDataWarning.warningText.setText("순번내역이 없습니다.");
    }

    private void setRecycler(){
        turnRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        turnRecyclerAdapter = new TurnRecyclerAdapter(turnInfoList, getApplicationContext());
        turnRecycler.setAdapter(turnRecyclerAdapter);
    }

    private void getTurnList(){
        Call<Turn> turnCall = networkService.getTurnList(SharedPreferencesService.getInstance().getPrefStringData(AUTH_TOKEN));

        turnCall.enqueue(new Callback<Turn>() {
            @Override
            public void onResponse(Call<Turn> call, Response<Turn> response) {
                if(response.isSuccessful()){
                    if(response.body().getStatus().equals(NETWORK_SUCCESS)){
                        turnInfoList = response.body().getTurnList();
                        turnRecyclerAdapter.refreshAdapter(turnInfoList);

                        if(turnInfoList.size() == 0)
                            ifemptyData();

                    }
                }
            }

            @Override
            public void onFailure(Call<Turn> call, Throwable t) {
                LogUtil.d(getApplicationContext(), t.toString());
            }
        });
    }

    private void setToolbar(){
        turnToolbar.setTitle("");
        setSupportActionBar(turnToolbar);
        turnToolbar.setNavigationIcon(getResources().getDrawable(R.drawable.back));
        turnToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.turn, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.turn_decrease) {
            nextTurnDialog = new BaseDialog(this, nextTurnListener, "순번을 제거하시겠습니까?");
            nextTurnDialog.show();
        }

        return super.onOptionsItemSelected(item);
    }

    private View.OnClickListener nextTurnListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            nextGuest();
        }
    };

    private void nextGuest(){
        Call<BaseModel> nextCall = networkService.nextGuset(SharedPreferencesService.getInstance().getPrefStringData(AUTH_TOKEN));

        nextCall.enqueue(new Callback<BaseModel>() {
            @Override
            public void onResponse(Call<BaseModel> call, Response<BaseModel> response) {
                if(response.isSuccessful()){
                    if(response.body().getStatus().equals(NETWORK_SUCCESS)){
                        turnInfoList.remove(0);
                        turnRecyclerAdapter.refreshAdapter(turnInfoList);
                        nextTurnDialog.dismiss();
                    }
                }
            }

            @Override
            public void onFailure(Call<BaseModel> call, Throwable t) {
                LogUtil.d(getApplicationContext(), t.toString());
            }
        });
    }

    @Override
    public void onRefresh() {
        getTurnList();
        trunRefreshLayout.setRefreshing(false);
    }
}
