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
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import dct.com.everyfoody.R;
import dct.com.everyfoody.base.OrangeThemeActivity;
import dct.com.everyfoody.model.Reservation;
import dct.com.everyfoody.ui.bookmark.BookmarkActivity;

public class ReserveCancelActivity extends OrangeThemeActivity {
    @BindView(R.id.reservation_cancel_toolbar)
    Toolbar reservationToolbar;
    @BindView(R.id.reservation_cancel_rcv)
    RecyclerView reserveRecycler;
    @BindView(R.id.warning_layout)
    View warningLayout;

    private BookmarkActivity.NonDataWarning nonDataWarning;
    private ReserveRecyclerAdapter reserveRecyclerAdapter;
    private List<Reservation.Store> reservationList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserve_cancel);
        ButterKnife.bind(this);
        reservationList = new ArrayList<>();
        setToolbar();
        setRecycler();
        getReservationList();

    }

    private void ifemptyData() {
        nonDataWarning = new BookmarkActivity.NonDataWarning();
        ButterKnife.bind(nonDataWarning, warningLayout);
        reserveRecycler.setVisibility(View.INVISIBLE);
        warningLayout.setVisibility(View.VISIBLE);
        nonDataWarning.warningText.setText("예약내역이 없습니다.");
    }

    private void setRecycler() {
        reserveRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        reserveRecyclerAdapter = new ReserveRecyclerAdapter(reservationList, onClickListener, getApplicationContext(), this);
        reserveRecycler.setAdapter(reserveRecyclerAdapter);
    }

    private void setToolbar() {
        reservationToolbar.setTitle("");
        setSupportActionBar(reservationToolbar);
        reservationToolbar.setNavigationIcon(getResources().getDrawable(R.drawable.back_white));
        reservationToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void getReservationList() {
        Intent getList = getIntent();

        reservationList = new Gson().fromJson(getList.getExtras().getString("reservationList"), new TypeToken<List<Reservation.Store>>() {
        }.getType());

        if (reservationList.size() == 0)
            ifemptyData();
        else {
            reserveRecycler.setVisibility(View.VISIBLE);
            warningLayout.setVisibility(View.INVISIBLE);
        }

        reserveRecyclerAdapter.refreshAdapter(reservationList);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.complete_delete, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.menu_complete:
                finish();
                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem deleteIc = menu.findItem(R.id.menu_delete);

        deleteIc.setVisible(false);

        return super.onPrepareOptionsMenu(menu);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
        }
    };
}
