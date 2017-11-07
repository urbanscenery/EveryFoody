package dct.com.everyfoody.ui.detail.edit;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;

import com.google.gson.Gson;

import dct.com.everyfoody.base.util.BundleBuilder;
import dct.com.everyfoody.model.StoreInfo;

/**
 * Created by jyoung on 2017. 8. 1..
 */

public class EditPagerAdapter extends FragmentStatePagerAdapter {
    private SparseArray<Fragment> mainPager = new SparseArray<Fragment>();
    private StoreInfo storeInfo;
    String basic , menu;

    public EditPagerAdapter(FragmentManager fm, StoreInfo storeInfo) {
        super(fm);
        this.storeInfo = storeInfo;
        Gson gson = new Gson();
        basic = gson.toJson(storeInfo.getDetailInfo().getBasicInfo());
        menu = gson.toJson(storeInfo);

    }

    public void refreshPageApater(StoreInfo storeInfo){
        this.storeInfo = storeInfo;
        this.notifyDataSetChanged();
    }

    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return NormalEditFragment.getInstance(BundleBuilder.create().with("basic", basic).build());
            case 1:
                return MenuEditFragment.getInstance(BundleBuilder.create().with("menu", menu).build());
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        mainPager.put(position, fragment);
        return fragment;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        mainPager.remove(position);
        super.destroyItem(container, position, object);
    }

    public Fragment getRegisteredFragment(int position) {
        return mainPager.get(position);
    }
}
