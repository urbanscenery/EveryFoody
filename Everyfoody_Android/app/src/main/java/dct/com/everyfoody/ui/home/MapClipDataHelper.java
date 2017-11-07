package dct.com.everyfoody.ui.home;

import android.util.SparseArray;

import dct.com.everyfoody.R;


/**
 * Created by LEECM on 2017-10-05.
 */

public class MapClipDataHelper {

    private static SparseArray<String[]> locationTextInfo;
    private static SparseArray<Integer[]> locationImageInfo;

    public static void initialize() {
        locationTextInfo = new SparseArray<>();
        locationImageInfo = new SparseArray<>();
/*

        //지역 이름 정보
        locationTextInfo.append(1, new String[]{"도봉", "강북", "성북", "노원"});
        locationTextInfo.append(2, new String[]{"동대문", "중랑", "성동", "광진"});
        locationTextInfo.append(3, new String[]{"강동", "송파"});
        locationTextInfo.append(4, new String[]{"서초", "강남"});
        locationTextInfo.append(5, new String[]{"동작", "관악", "금천"});
        locationTextInfo.append(6, new String[]{"강서", "양천", "영등포", "구로"});
        locationTextInfo.append(7, new String[]{"은평", "마포", "서대문"});
        locationTextInfo.append(8, new String[]{"종로", "중구", "용산"});

        //클릭시 바꿔줄 Drawable Resource 정보
        locationImageInfo.append(1, new Integer[]{R.drawable.area1, R.drawable.area1_click});
        locationImageInfo.append(2, new Integer[]{R.drawable.area2, R.drawable.area2_click});
        locationImageInfo.append(3, new Integer[]{R.drawable.area3, R.drawable.area3_click});
        locationImageInfo.append(4, new Integer[]{R.drawable.area4, R.drawable.area4_click});
        locationImageInfo.append(5, new Integer[]{R.drawable.area5, R.drawable.area5_click});
        locationImageInfo.append(6, new Integer[]{R.drawable.area6, R.drawable.area6_click});
        locationImageInfo.append(7, new Integer[]{R.drawable.area7, R.drawable.area7_click});
        locationImageInfo.append(8, new Integer[]{R.drawable.area8, R.drawable.area8_click});

*/

        /**
         * 순서 바뀐 부분
         * */
        //지역 이름 정보
        locationTextInfo.append(4, new String[]{"도봉", "강북", "성북", "노원"});
        locationTextInfo.append(5, new String[]{"동대문", "중랑", "성동", "광진"});
        locationTextInfo.append(8, new String[]{"강동", "송파"});
        locationTextInfo.append(7, new String[]{"서초", "강남"});
        locationTextInfo.append(6, new String[]{"동작", "관악", "금천"});
        locationTextInfo.append(1, new String[]{"강서", "양천", "영등포", "구로"});
        locationTextInfo.append(2, new String[]{"은평", "마포", "서대문"});
        locationTextInfo.append(3, new String[]{"종로", "중구", "용산"});

        //클릭시 바꿔줄 Drawable Resource 정보
        locationImageInfo.append(4, new Integer[]{R.drawable.area1, R.drawable.area1_click});
        locationImageInfo.append(5, new Integer[]{R.drawable.area2, R.drawable.area2_click});
        locationImageInfo.append(8, new Integer[]{R.drawable.area3, R.drawable.area3_click});
        locationImageInfo.append(7, new Integer[]{R.drawable.area4, R.drawable.area4_click});
        locationImageInfo.append(6, new Integer[]{R.drawable.area5, R.drawable.area5_click});
        locationImageInfo.append(1, new Integer[]{R.drawable.area6, R.drawable.area6_click});
        locationImageInfo.append(2, new Integer[]{R.drawable.area7, R.drawable.area7_click});
        locationImageInfo.append(3, new Integer[]{R.drawable.area8, R.drawable.area8_click});

    }

    public static String[] getLocationTextInfo(int key) {
        return locationTextInfo.get(key);
    }

    public static int getMapImage(int key, boolean isDefault) {
        Integer[] drawables = locationImageInfo.get(key);

        if(isDefault)
            return drawables[0];
        else
            return drawables[1];
    }
}
