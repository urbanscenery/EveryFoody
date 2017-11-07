package dct.com.everyfoody.base.util;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by jyoung on 2017. 10. 27..
 */

public class RecyclerViewDivider extends RecyclerView.ItemDecoration {
    private final int divHeight;

    public RecyclerViewDivider(int divHeight) {
        this.divHeight = divHeight;
    }


    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.top = divHeight;
    }
}
