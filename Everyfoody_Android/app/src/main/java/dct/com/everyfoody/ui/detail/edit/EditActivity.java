package dct.com.everyfoody.ui.detail.edit;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.gson.Gson;
import com.yalantis.ucrop.UCrop;

import butterknife.BindView;
import butterknife.ButterKnife;
import dct.com.everyfoody.R;
import dct.com.everyfoody.base.BaseModel;
import dct.com.everyfoody.base.WhiteThemeActivity;
import dct.com.everyfoody.base.util.LogUtil;
import dct.com.everyfoody.base.util.SharedPreferencesService;
import dct.com.everyfoody.base.util.ToastMaker;
import dct.com.everyfoody.global.ApplicationController;
import dct.com.everyfoody.model.StoreInfo;
import dct.com.everyfoody.request.NetworkService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static dct.com.everyfoody.ui.detail.edit.NormalEditFragment.THUMBNAIL_CROP;
import static dct.com.everyfoody.ui.login.LoginActivity.AUTH_TOKEN;
import static dct.com.everyfoody.ui.login.LoginActivity.NETWORK_SUCCESS;

public class EditActivity extends WhiteThemeActivity {
    @BindView(R.id.edit_toolbar)Toolbar editToolbar;
    @BindView(R.id.edit_viewpager)ViewPager editViewPager;
    @BindView(R.id.edit_tablayout)TabLayout editTab;

    private EditPagerAdapter editPagerAdapter;
    private StoreInfo storeInfo;
    private NetworkService networkService;

    public final static int MENU_EDIT = 501;
    public final static int MENU_ADD = 502;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        ButterKnife.bind(this);
        networkService = ApplicationController.getInstance().getNetworkService();
        SharedPreferencesService.getInstance().load(this);

        getStoreInfo();
        setToolbar();
        setLayout();

    }

    private void getStoreInfo(){
        Intent getInfo = getIntent();
        Gson gson = new Gson();
        storeInfo = gson.fromJson(getInfo.getExtras().getString("info"), StoreInfo.class);
    }

    private void setToolbar(){
        editToolbar.setTitle("");
        setSupportActionBar(editToolbar);
        editToolbar.setNavigationIcon(getResources().getDrawable(R.drawable.back));
        editToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    private void setLayout() {
        editPagerAdapter = new EditPagerAdapter(getSupportFragmentManager(), storeInfo);
        editViewPager.setAdapter(editPagerAdapter);
        editTab.setupWithViewPager(editViewPager);
        editTab.getTabAt(0).setText("기본정보");
        editTab.getTabAt(1).setText("메뉴정보");
    }

    private void getMyStoreInfo() {
        final Call<StoreInfo> getMyStoreInfo = networkService.getMyStoreInfo(SharedPreferencesService.getInstance().getPrefStringData(AUTH_TOKEN));

        getMyStoreInfo.enqueue(new Callback<StoreInfo>() {
            @Override
            public void onResponse(Call<StoreInfo> call, Response<StoreInfo> response) {
                if(response.isSuccessful()){
                    if(response.body().getStatus().equals(NETWORK_SUCCESS)){
                        storeInfo = response.body();
                        editPagerAdapter.notifyDataSetChanged();
                        editTab.getTabAt(0).setText("기본정보");
                        editTab.getTabAt(1).setText("메뉴정보");
                        editViewPager.setCurrentItem(1);
                    }
                }
            }

            @Override
            public void onFailure(Call<StoreInfo> call, Throwable t) {
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

        if(id == R.id.menu_complete){
            Fragment fragment = editPagerAdapter.getRegisteredFragment(0);
            storeInfo.getDetailInfo().setBasicInfo(((NormalEditFragment)fragment).getEditInfo());

            Call<BaseModel> basicEditCall = networkService.modifyBasicInfo(SharedPreferencesService.getInstance().getPrefStringData(AUTH_TOKEN),
                    storeInfo.getDetailInfo().getBasicInfo());

            basicEditCall.enqueue(new Callback<BaseModel>() {
                @Override
                public void onResponse(Call<BaseModel> call, Response<BaseModel> response) {
                    if(response.isSuccessful()){
                        if(response.body().getStatus().equals(NETWORK_SUCCESS)){
                            ToastMaker.makeShortToast(getApplicationContext(), "정보가 수정되었습니다.");
                            Intent intent = new Intent();
                            setResult(RESULT_OK, intent);
                            finish();
                        }
                    }
                }

                @Override
                public void onFailure(Call<BaseModel> call, Throwable t) {
                    LogUtil.d(getApplicationContext(), t.toString());
                }
            });
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem deleteIc = menu.findItem(R.id.menu_delete);
        deleteIc.setVisible(false);

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == UCrop.REQUEST_CROP) {
                {
                    Fragment fragment = editPagerAdapter.getRegisteredFragment(0);
                    if (fragment != null) {
                        ((NormalEditFragment) fragment).onActivityResult(requestCode, resultCode, data);
                    }
                }
            }
            else if(requestCode == THUMBNAIL_CROP){
                Fragment fragment = editPagerAdapter.getRegisteredFragment(0);
                if (fragment != null) {
                    ((NormalEditFragment) fragment).onActivityResult(requestCode, resultCode, data);
                }
            }
            else if(requestCode == MENU_EDIT || requestCode == MENU_ADD){
                getMyStoreInfo();
                editViewPager.setCurrentItem(1);
            }
        }
    }
}
