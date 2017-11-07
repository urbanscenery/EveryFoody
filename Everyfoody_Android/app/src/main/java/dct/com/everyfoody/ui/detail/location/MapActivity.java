package dct.com.everyfoody.ui.detail.location;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dct.com.everyfoody.R;
import dct.com.everyfoody.base.BaseDialog;
import dct.com.everyfoody.base.BaseModel;
import dct.com.everyfoody.base.WhiteThemeActivity;
import dct.com.everyfoody.base.util.LogUtil;
import dct.com.everyfoody.base.util.SharedPreferencesService;
import dct.com.everyfoody.base.util.ToastMaker;
import dct.com.everyfoody.global.ApplicationController;
import dct.com.everyfoody.model.OpenLocation;
import dct.com.everyfoody.model.ResLocation;
import dct.com.everyfoody.model.StoreInfo;
import dct.com.everyfoody.request.NetworkService;
import dct.com.everyfoody.ui.login.LoginActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static dct.com.everyfoody.ui.detail.DetailActivity.EXIST_RESERVATION;
import static dct.com.everyfoody.ui.detail.DetailActivity.NON_EXIST_RESERVATION;
import static dct.com.everyfoody.ui.home.owner.OwnerHomeActivity.STORE_OPEN;
import static dct.com.everyfoody.ui.login.LoginActivity.AUTH_TOKEN;
import static dct.com.everyfoody.ui.login.LoginActivity.NETWORK_SUCCESS;
import static dct.com.everyfoody.ui.login.LoginActivity.RESULT_GUEST;
import static dct.com.everyfoody.ui.login.LoginActivity.RESULT_NON_AUTH_OWNER;
import static dct.com.everyfoody.ui.login.LoginActivity.RESULT_NO_REG_STORE;
import static dct.com.everyfoody.ui.login.LoginActivity.RESULT_OWNER;
import static dct.com.everyfoody.ui.login.LoginActivity.USER_NAME;
import static dct.com.everyfoody.ui.login.LoginActivity.USER_STATUS;


public class MapActivity extends WhiteThemeActivity implements OnMapReadyCallback {
    @BindView(R.id.map_toolbar)
    Toolbar mapToolbar;
    @BindView(R.id.map_booking_count)
    TextView bookingCount;
    @BindView(R.id.map_booking)
    TextView mapBottomSheetText;
    @BindView(R.id.search_view)
    MaterialSearchView searchView;
    @BindView(R.id.map_text)
    TextView mapText;
    @BindView(R.id.map_my_loc)
    ImageView myloc;

    private MapFragment mapFragment;
    private LocationManager locationManager;
    private Marker myLocation;
    private NetworkService networkService;
    private double mLatitude, mLongitude;
    private int storeId;
    private int userStatus;
    private int reservationCheck;
    private GoogleMap googleMap;
    private BaseDialog reserveDialog;
    private StoreInfo storeInfo;

    public final static double DEFAULT_LAT = 37.566535;
    public final static double DEFAULT_LNG = 126.977969;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        ButterKnife.bind(this);
        networkService = ApplicationController.getInstance().getNetworkService();
        SharedPreferencesService.getInstance().load(this);
        userStatus = SharedPreferencesService.getInstance().getPrefIntegerData(USER_STATUS);
        switch (userStatus) {
            case RESULT_GUEST:
                startMapGuest();
                break;
            case RESULT_OWNER:
            case RESULT_NON_AUTH_OWNER:
            case RESULT_NO_REG_STORE:
                startMapOwner();
                break;
            default:
                startMapGuest();
                break;
        }
        setToolbar();
    }


    private void startMapGuest() {
        Intent getData = getIntent();
        storeId = getData.getExtras().getInt("storeId");
        reservationCheck = getData.getExtras().getInt("reservationCheck");
        if (reservationCheck == EXIST_RESERVATION)
            mapBottomSheetText.setText("순번 대기중");
        getLocation();
    }

    private void startMapOwner() {
        mapBottomSheetText.setText("위치선정 완료");
        mLatitude = DEFAULT_LAT;
        mLongitude = DEFAULT_LNG;
        setMap();
    }

    private void getLocation() {
        Call<ResLocation> locationCall = networkService.getLocation(SharedPreferencesService.getInstance().getPrefStringData(AUTH_TOKEN), storeId);

        locationCall.enqueue(new Callback<ResLocation>() {
            @Override
            public void onResponse(Call<ResLocation> call, Response<ResLocation> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals(NETWORK_SUCCESS)) {
                        mLatitude = response.body().getLocation().getStoreLatitude();
                        mLongitude = response.body().getLocation().getStoreLongitude();

                        bookingCount.setText("대기인원 " + response.body().getLocation().getReservationCount() + "명");

                        setMap();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResLocation> call, Throwable t) {
                LogUtil.d(getApplicationContext(), t.toString());
            }
        });
    }

    private void setToolbar() {
        mapToolbar.setTitle("");
        setSupportActionBar(mapToolbar);
        mapToolbar.setNavigationIcon(getResources().getDrawable(R.drawable.back));
        mapToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                returnDetail();
            }
        });
    }

    private void setMap() {
        mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(final GoogleMap map) {
        googleMap = map;
        LatLng position = new LatLng(mLatitude, mLongitude);

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 14));

        myLocation = map.addMarker(new MarkerOptions()
                .position(position)
                .title("내 위치")
                .draggable(true)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.me)));


        if (userStatus == RESULT_OWNER) {
            map.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
                @Override
                public void onMarkerDragStart(Marker marker) {

                }

                @Override
                public void onMarkerDrag(Marker marker) {
                }

                @Override
                public void onMarkerDragEnd(Marker marker) {
                    LatLng dragPosition = marker.getPosition();
                    mLatitude = dragPosition.latitude;
                    mLongitude = dragPosition.longitude;
                    map.moveCamera(CameraUpdateFactory.newLatLng(dragPosition));
                }
            });
        } else {
            MarkerOptions mymarker = new MarkerOptions()
                    .position(position);
            mymarker.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker2));
            if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                map.setMyLocationEnabled(true);
            }
            CircleOptions circle1KM = new CircleOptions().center(position)
                    .radius(1000)
                    .strokeWidth(0f)
                    .fillColor(Color.parseColor("#33ff6e00"));

            map.addMarker(mymarker);
            map.addCircle(circle1KM);
            map.setMyLocationEnabled(false);
            map.getUiSettings().setMyLocationButtonEnabled(false);

        }
    }

    private void reserve() {
        reserveDialog = new BaseDialog(this, reserveYes, "순번을 뽑으시겠습니까?");
        reserveDialog.show();
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
                            mapBottomSheetText.setText("순번 대기중");
                            getStoreInfo();
                            reservationCheck = EXIST_RESERVATION;
                            reserveDialog.dismiss();
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

    private void getStoreInfo() {
        String token = SharedPreferencesService.getInstance().getPrefStringData(AUTH_TOKEN);
        if (token.equals(""))
            token = "nonLoginUser";

        Call<StoreInfo> getStoreInfo = networkService.getStoreInfo(token, storeId);

        getStoreInfo.enqueue(new Callback<StoreInfo>() {
            @Override
            public void onResponse(Call<StoreInfo> call, Response<StoreInfo> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals(NETWORK_SUCCESS)) {
                        storeInfo = response.body();
                        bookingCount.setText("대기인원 " + storeInfo.getDetailInfo().getBasicInfo().getReservationCount() + "명");
                        if (storeInfo.getDetailInfo().getBasicInfo().getReservationCheck() == EXIST_RESERVATION) {
                            mapBottomSheetText.setText("순번 대기중");
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<StoreInfo> call, Throwable t) {
                LogUtil.d(getApplicationContext(), t.toString());
            }
        });

    }

    @OnClick(R.id.map_booking_group)
    public void onClickMapBottomSheet(View view) {
        if (mapBottomSheetText.getText().toString().equals("위치선정 완료")) {
            if (mLongitude != 0) {
                final OpenLocation openLocation = new OpenLocation();
                openLocation.setLatitude(mLatitude);
                openLocation.setLongitude(mLongitude);

                Call<BaseModel> openStore = networkService.openStore(SharedPreferencesService.getInstance().getPrefStringData(AUTH_TOKEN), openLocation);

                openStore.enqueue(new Callback<BaseModel>() {
                    @Override
                    public void onResponse(Call<BaseModel> call, Response<BaseModel> response) {
                        if (response.isSuccessful()) {
                            if (response.body().getStatus().equals(NETWORK_SUCCESS)) {
                                ToastMaker.makeShortToast(getApplicationContext(), SharedPreferencesService.getInstance().getPrefStringData(USER_NAME)+"님 오늘도 대박나세요!");
                                SharedPreferencesService.getInstance().setPrefData("store_status", STORE_OPEN);
                                finish();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<BaseModel> call, Throwable t) {
                        LogUtil.d(getApplicationContext(), t.toString());
                    }
                });
            } else
                ToastMaker.makeShortToast(getApplicationContext(), "내 위치 버튼을 눌러 위치를 지정하세요!");
        } else {
            if (SharedPreferencesService.getInstance().getPrefIntegerData(USER_STATUS) == RESULT_GUEST) {
                if (reservationCheck == NON_EXIST_RESERVATION)
                    reserve();
                else
                    ToastMaker.makeShortToast(getApplicationContext(), "      순번을 기다리고 있습니다!!\n 예약내역에서 취소가 가능합니다");
            } else {
                ToastMaker.makeShortToast(getApplicationContext(), "로그인이 필요한 기능입니다.");
                Intent needLogin = new Intent(this, LoginActivity.class);
                startActivity(needLogin);
            }
        }
    }

    @OnClick(R.id.map_my_loc)
    public void onClickMyLoc(View view) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(locationManager.getBestProvider(new Criteria(), true), 3000, 0, locationListener);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);

        ToastMaker.makeShortToast(getApplicationContext(), "현재 위치를 조회중입니다.");
    }

    private LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            mLatitude = latLng.latitude;
            mLongitude = latLng.longitude;
            myLocation.setPosition(latLng);

            CameraPosition.Builder builder = new CameraPosition.Builder();
            builder.zoom(15);
            builder.target(latLng);

            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(builder.build()));
            locationManager.removeUpdates(this);

        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {
        }

        @Override
        public void onProviderEnabled(String s) {
        }

        @Override
        public void onProviderDisabled(String s) {
        }
    };

    private void returnDetail() {
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        returnDetail();
    }


}
