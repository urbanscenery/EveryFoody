package dct.com.everyfoody.ui.reservation;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import dct.com.everyfoody.R;
import dct.com.everyfoody.base.WhiteThemeActivity;
import dct.com.everyfoody.base.util.LogUtil;
import dct.com.everyfoody.base.util.SharedPreferencesService;
import dct.com.everyfoody.global.ApplicationController;
import dct.com.everyfoody.model.Reservation;
import dct.com.everyfoody.request.NetworkService;
import dct.com.everyfoody.ui.bookmark.BookmarkActivity;
import dct.com.everyfoody.ui.detail.DetailActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static dct.com.everyfoody.ui.login.LoginActivity.AUTH_TOKEN;
import static dct.com.everyfoody.ui.login.LoginActivity.NETWORK_SUCCESS;

public class ReservationActivity extends WhiteThemeActivity {
    @BindView(R.id.reservation_toolbar)Toolbar reservationToolbar;
    @BindView(R.id.reservation_rcv)RecyclerView reserveRecycler;
    @BindView(R.id.warning_layout)View warningLayout;

    private ReserveRecyclerAdapter reserveRecyclerAdapter;
    private List<Reservation.Store> reservationList;
    private NetworkService networkService;
    private BookmarkActivity.NonDataWarning nonDataWarning;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation);
        ButterKnife.bind(this);
        reservationList = new ArrayList<>();
        networkService = ApplicationController.getInstance().getNetworkService();
        SharedPreferencesService.getInstance().load(this);
        setToolbar();
        setRecycler();
        getReservationList();

    }

    private void ifemptyData(){
        nonDataWarning = new BookmarkActivity.NonDataWarning();
        ButterKnife.bind(nonDataWarning, warningLayout);
        reserveRecycler.setVisibility(View.INVISIBLE);
        warningLayout.setVisibility(View.VISIBLE);
        nonDataWarning.warningText.setText("예약내역이 없습니다.");
    }
    private void setRecycler(){
        reserveRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        reserveRecyclerAdapter = new ReserveRecyclerAdapter(reservationList, onClickListener, this);
        reserveRecycler.setAdapter(reserveRecyclerAdapter);
    }

    private void setToolbar(){
        reservationToolbar.setTitle("");
        setSupportActionBar(reservationToolbar);
        reservationToolbar.setNavigationIcon(getResources().getDrawable(R.drawable.back));
        reservationToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void getReservationList(){
        Call<Reservation> reservationCall = networkService.getReservationList(SharedPreferencesService.getInstance().getPrefStringData(AUTH_TOKEN));

        reservationCall.enqueue(new Callback<Reservation>() {
            @Override
            public void onResponse(Call<Reservation> call, Response<Reservation> response) {
                if(response.isSuccessful()){
                    if(response.body().getStatus().equals(NETWORK_SUCCESS)){
                        reservationList = response.body().getStore();
                        reserveRecyclerAdapter.refreshAdapter(reservationList);

                        if(reservationList.size() == 0)
                            ifemptyData();
                        else {
                            reserveRecycler.setVisibility(View.VISIBLE);
                            warningLayout.setVisibility(View.INVISIBLE);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<Reservation> call, Throwable t) {
                LogUtil.d(getApplicationContext(), t.toString());
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.complete_delete, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id){
            case R.id.menu_delete:
                Gson gson = new Gson();
                String list = gson.toJson(reservationList);
                Intent cancelIntent = new Intent(getApplicationContext(), ReserveCancelActivity.class);
                cancelIntent.putExtra("reservationList", list);
                startActivity(cancelIntent);
                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem completeIc = menu.findItem(R.id.menu_complete);

        completeIc.setVisible(false);


        return super.onPrepareOptionsMenu(menu);
    }

    public View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            final int tempPosition = reserveRecycler.getChildPosition(view);
            Intent detailIntent = new Intent(view.getContext(), DetailActivity.class);
            detailIntent.putExtra("storeId", reservationList.get(tempPosition).getStoreID());
            startActivity(detailIntent);
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        getReservationList();
    }
}
