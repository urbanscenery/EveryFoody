package dct.com.everyfoody.ui.detail;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dct.com.everyfoody.R;
import dct.com.everyfoody.base.BaseDialog;
import dct.com.everyfoody.base.BaseModel;
import dct.com.everyfoody.base.OrangeThemeActivity;
import dct.com.everyfoody.base.util.BundleBuilder;
import dct.com.everyfoody.base.util.LogUtil;
import dct.com.everyfoody.base.util.SharedPreferencesService;
import dct.com.everyfoody.base.util.ToastMaker;
import dct.com.everyfoody.global.ApplicationController;
import dct.com.everyfoody.model.StoreInfo;
import dct.com.everyfoody.request.NetworkService;
import dct.com.everyfoody.ui.detail.edit.EditActivity;
import dct.com.everyfoody.ui.detail.location.MapActivity;
import dct.com.everyfoody.ui.detail.normal.NormalFragment;
import dct.com.everyfoody.ui.detail.review.ReviewActivity;
import dct.com.everyfoody.ui.login.LoginActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static dct.com.everyfoody.ui.login.LoginActivity.AUTH_TOKEN;
import static dct.com.everyfoody.ui.login.LoginActivity.NETWORK_SUCCESS;
import static dct.com.everyfoody.ui.login.LoginActivity.RESULT_GUEST;
import static dct.com.everyfoody.ui.login.LoginActivity.RESULT_NON_AUTH_OWNER;
import static dct.com.everyfoody.ui.login.LoginActivity.RESULT_NO_REG_STORE;
import static dct.com.everyfoody.ui.login.LoginActivity.RESULT_OWNER;
import static dct.com.everyfoody.ui.login.LoginActivity.USER_STATUS;

public class DetailActivity extends OrangeThemeActivity {
    @BindView(R.id.detail_toolbar)
    Toolbar detailToolbar;
    @BindView(R.id.detail_main_image)
    ImageView mainImage;
    @BindView(R.id.booking_count)
    TextView bookingCount;
    @BindView(R.id.booking)
    TextView booking;
    @BindView(R.id.review_btn)
    LinearLayout reviewBtn;

    public final static String TAG = DetailActivity.class.getSimpleName();
    public final static int EDIT_COMPLETE = 70;
    public final static int CHECK_MAP = 71;
    public final static int EXIST_RESERVATION = 603;
    public final static int NON_EXIST_RESERVATION = 604;
    public final static int EXIST_BOOKMARK = 605;
    public final static int NON_EXIST_BOOKMARK = 606;


    private int storeId;
    private float openStatus;
    private NetworkService networkService;
    private StoreInfo storeInfo;
    private int bookmarkFlag;
    private int userStatus;
    private BaseDialog baseDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
        SharedPreferencesService.getInstance().load(this);
        networkService = ApplicationController.getInstance().getNetworkService();

        userStatus = SharedPreferencesService.getInstance().getPrefIntegerData(USER_STATUS);
        switch (userStatus) {
            case RESULT_GUEST:
                getInitData();
                getStoreInfo();
                break;
            case RESULT_OWNER:
            case RESULT_NON_AUTH_OWNER:
            case RESULT_NO_REG_STORE:
                getMyStoreInfo();
                break;
            default:
                getInitData();
                getStoreInfo();
                break;
        }

    }

    private void getMyStoreInfo() {
        final Call<StoreInfo> getMyStoreInfo = networkService.getMyStoreInfo(SharedPreferencesService.getInstance().getPrefStringData(AUTH_TOKEN));

        getMyStoreInfo.enqueue(new Callback<StoreInfo>() {
            @Override
            public void onResponse(Call<StoreInfo> call, Response<StoreInfo> response) {
                if(response.isSuccessful()){
                    if(response.body().getStatus().equals(NETWORK_SUCCESS)){
                        storeInfo = response.body();
                        networkAfter();
                        reviewBtn.setVisibility(View.GONE);
                        booking.setText("후기");
                    }
                }
            }

            @Override
            public void onFailure(Call<StoreInfo> call, Throwable t) {
                LogUtil.d(getApplicationContext(), t.toString());
            }
        });

    }

    private void getInitData() {
        Intent getData = getIntent();
        storeId = getData.getExtras().getInt("storeId");
        openStatus = getData.getExtras().getFloat("openStatus");
        networkService = ApplicationController.getInstance().getNetworkService();
    }

    private void getStoreInfo() {
        String token = SharedPreferencesService.getInstance().getPrefStringData(AUTH_TOKEN);
        if(token.equals(""))
            token = "nonLoginUser";

        Call<StoreInfo> getStoreInfo = networkService.getStoreInfo(token, storeId);

        getStoreInfo.enqueue(new Callback<StoreInfo>() {
            @Override
            public void onResponse(Call<StoreInfo> call, Response<StoreInfo> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals(NETWORK_SUCCESS)) {
                        storeInfo = response.body();
                        networkAfter();
                        bookmarkFlag = storeInfo.getDetailInfo().getBasicInfo().getBookmarkCheck();
                        bookingCount.setText("대기인원 "+storeInfo.getDetailInfo().getBasicInfo().getReservationCount()+"명");
                        if(storeInfo.getDetailInfo().getBasicInfo().getReservationCheck()==EXIST_RESERVATION){
                            booking.setText("순번 대기중");
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<StoreInfo> call, Throwable t) {
                LogUtil.d(getApplicationContext(), "error : " + t.toString());
            }
        });

    }

    private void networkAfter() {
        setToolbar();
        Gson gson = new Gson();
        String info = gson.toJson(storeInfo);

        replaceFragment(NormalFragment.getInstance(), BundleBuilder.create().with("storeInfo", info).build());
    }

    private void setToolbar() {
        Glide.with(this).load(storeInfo.getDetailInfo().getBasicInfo().getStoreImage()).into(mainImage);
        detailToolbar.setTitle(storeInfo.getDetailInfo().getBasicInfo().getStoreName());
        detailToolbar.setTitleTextColor(getResources().getColor(R.color.colorPrimary));
        setSupportActionBar(detailToolbar);
        detailToolbar.setNavigationIcon(getResources().getDrawable(R.drawable.back_white));
        detailToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }


    public void replaceFragment(Fragment fragment, @Nullable Bundle bundle){
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        fragment.setArguments(bundle);
        transaction.replace(R.id.detail_content, fragment);
        transaction.commit();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_detail_map) {
            if(openStatus != -1) {
                Intent mapIntent = new Intent(this, MapActivity.class);
                mapIntent.putExtra("storeId", storeId);
                mapIntent.putExtra("reservationCheck", storeInfo.getDetailInfo().getBasicInfo().getReservationCheck());
                startActivityForResult(mapIntent, CHECK_MAP);
            }
            else
                ToastMaker.makeShortToast(getApplicationContext(), "오픈하지 않은 가게입니다.");
        } else if (id == R.id.menu_detail_bookmark_on || id == R.id.menu_detail_bookmark_off) {
            if (SharedPreferencesService.getInstance().getPrefIntegerData(USER_STATUS) != RESULT_GUEST) {
                ToastMaker.makeShortToast(getApplicationContext(), "로그인이 필요한 기능입니다.");
                Intent needLogin = new Intent(this, LoginActivity.class);
                startActivity(needLogin);
            } else {
                bookmark();
            }
        } else if (id == R.id.menu_detail_edit) {
            Intent editIntent = new Intent(this, EditActivity.class);
            Gson gson = new Gson();
            String info = gson.toJson(storeInfo);
            editIntent.putExtra("info", info);
            startActivityForResult(editIntent, EDIT_COMPLETE);
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem map = menu.findItem(R.id.menu_detail_map);
        MenuItem bookmarkOn = menu.findItem(R.id.menu_detail_bookmark_on);
        MenuItem bookmarkOff = menu.findItem(R.id.menu_detail_bookmark_off);
        MenuItem edit = menu.findItem(R.id.menu_detail_edit);

        if (SharedPreferencesService.getInstance().getPrefIntegerData(USER_STATUS) == RESULT_GUEST) {
            edit.setVisible(false);
            if (bookmarkFlag == NON_EXIST_BOOKMARK) {
                bookmarkOn.setVisible(false);
                bookmarkOff.setVisible(true);
            } else if (bookmarkFlag == EXIST_BOOKMARK) {
                bookmarkOn.setVisible(true);
                bookmarkOff.setVisible(false);
            }
        } else if (SharedPreferencesService.getInstance().getPrefIntegerData(USER_STATUS) == RESULT_OWNER) {
            map.setVisible(false);
            bookmarkOff.setVisible(false);
            bookmarkOn.setVisible(false);
        } else {

            bookmarkOn.setVisible(false);
            edit.setVisible(false);
        }

        return super.onPrepareOptionsMenu(menu);
    }

    @OnClick(R.id.review_btn)
    public void reviewClick(View view) {
        Intent reviewIntent = new Intent(this, ReviewActivity.class);
        reviewIntent.putExtra("storeId", storeId);
        startActivity(reviewIntent);
    }

    @OnClick(R.id.booking)
    public void bookingClick(View view) {
        if (SharedPreferencesService.getInstance().getPrefIntegerData(USER_STATUS) == RESULT_GUEST) {
            if(openStatus != -1) {
                if (storeInfo.getDetailInfo().getBasicInfo().getReservationCheck() == NON_EXIST_RESERVATION)
                    reserve();
                else
                    ToastMaker.makeShortToast(getApplicationContext(), "      순번을 기다리고 있습니다!!\n 예약내역에서 취소가 가능합니다");
            }
            else
                ToastMaker.makeShortToast(getApplicationContext(), "오픈하지 않은 가게입니다.");
        }else if(SharedPreferencesService.getInstance().getPrefIntegerData("user_status") == RESULT_OWNER){
            Intent reviewIntent = new Intent(this, ReviewActivity.class);
            reviewIntent.putExtra("storeId", storeId);
            startActivity(reviewIntent);
        }
        else {
            ToastMaker.makeShortToast(getApplicationContext(), "로그인이 필요한 기능입니다.");
            Intent needLogin = new Intent(this, LoginActivity.class);
            startActivity(needLogin);
        }
    }

    private void reserve() {
        baseDialog = new BaseDialog(this, reserveYes, "순번을 뽑으시겠습니까?");
        baseDialog.show();
    }

    public View.OnClickListener reserveYes = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Call<BaseModel> reserveCall = networkService.userReseve(SharedPreferencesService.getInstance().getPrefStringData(AUTH_TOKEN), storeId);

            reserveCall.enqueue(new Callback<BaseModel>() {
                @Override
                public void onResponse(Call<BaseModel> call, Response<BaseModel> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getStatus().equals(NETWORK_SUCCESS)) {
                            booking.setText("순번 대기중");
                            storeInfo.getDetailInfo().getBasicInfo().setReservationCheck(EXIST_RESERVATION);
                            getStoreInfo();
                            baseDialog.dismiss();
                        }
                    }
                }

                @Override
                public void onFailure(Call<BaseModel> call, Throwable t) {
                    LogUtil.d(getApplicationContext(), t.toString());
                }
            });
        }
    };

    private void bookmark() {

        Call<BaseModel> bookmarkCall = networkService.userBookmark(SharedPreferencesService.getInstance().getPrefStringData(AUTH_TOKEN), storeId);

        bookmarkCall.enqueue(new Callback<BaseModel>() {
            @Override
            public void onResponse(Call<BaseModel> call, Response<BaseModel> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals(NETWORK_SUCCESS)) {

                        if (bookmarkFlag == EXIST_BOOKMARK)
                            bookmarkFlag = NON_EXIST_BOOKMARK;
                        else
                            bookmarkFlag = EXIST_BOOKMARK;

                        supportInvalidateOptionsMenu();
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            if(requestCode == EDIT_COMPLETE){
                getMyStoreInfo();
            }else if(requestCode == CHECK_MAP){
                getStoreInfo();
            }
        }
    }
}
