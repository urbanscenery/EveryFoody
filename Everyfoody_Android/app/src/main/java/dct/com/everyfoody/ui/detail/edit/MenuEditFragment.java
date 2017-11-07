package dct.com.everyfoody.ui.detail.edit;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dct.com.everyfoody.R;
import dct.com.everyfoody.base.util.LogUtil;
import dct.com.everyfoody.base.util.SharedPreferencesService;
import dct.com.everyfoody.global.ApplicationController;
import dct.com.everyfoody.model.Menu;
import dct.com.everyfoody.model.StoreInfo;
import dct.com.everyfoody.request.NetworkService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static dct.com.everyfoody.ui.detail.edit.EditActivity.MENU_ADD;
import static dct.com.everyfoody.ui.login.LoginActivity.AUTH_TOKEN;
import static dct.com.everyfoody.ui.login.LoginActivity.NETWORK_SUCCESS;

/**
 * Created by jyoung on 2017. 10. 4..
 */

public class MenuEditFragment extends Fragment {
    @BindView(R.id.edit_menu_rcv)RecyclerView menuEditRecycler;

    private List<StoreInfo.MenuInfo> menuInfoList;
    private EditMenuRecyclerAdapter editMenuRecyclerAdapter;
    private NetworkService networkService;

    public MenuEditFragment() {
    }

    public static MenuEditFragment getInstance(Bundle bundle){
        MenuEditFragment menuEditFragment = new MenuEditFragment();
        menuEditFragment.setArguments(bundle);
        return menuEditFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu_info_edit, null);
        ButterKnife.bind(this, view);
        networkService = ApplicationController.getInstance().getNetworkService();
        SharedPreferencesService.getInstance().load(getContext());

//        if(getArguments() != null){
//            Gson gson = new Gson();
//            menuInfoList = gson.fromJson(getArguments().getString("menu"), StoreInfo.class).getDetailInfo().getMenuInfo();
//        }

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initInfo();
        getMenuList();
    }

    private void initInfo(){
        menuEditRecycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        editMenuRecyclerAdapter = new EditMenuRecyclerAdapter(menuInfoList, getContext());
        menuEditRecycler.setAdapter(editMenuRecyclerAdapter);
    }

    private void getMenuList(){
        Call<Menu> menuCall = networkService.getMenuList(SharedPreferencesService.getInstance().getPrefStringData(AUTH_TOKEN));

        menuCall.enqueue(new Callback<Menu>() {
            @Override
            public void onResponse(Call<Menu> call, Response<Menu> response) {
                if(response.isSuccessful()){
                    if(response.body().getStatus().equals(NETWORK_SUCCESS)){
                        editMenuRecyclerAdapter.refreshAdapter(response.body().getMenuItem().getMenuInfoList());
                    }
                }
            }

            @Override
            public void onFailure(Call<Menu> call, Throwable t) {
                LogUtil.d(getContext(), t.toString());
            }
        });
    }

    @OnClick(R.id.menu_item_add_btn)
    public void onClickMenuAdd(View view){
        Intent addIntent =new Intent(getContext(), EditMenuActivity.class);
        addIntent.putExtra("addORedit", MENU_ADD);
        getActivity().startActivityForResult(addIntent, MENU_ADD);
    }

}
