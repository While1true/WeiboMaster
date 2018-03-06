package com.master.weibomaster.Util;

import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;

import com.nestrefreshlib.Adpater.Impliment.SAdapter;
import com.nestrefreshlib.RefreshViews.AdapterHelper.RefreshHeaderAndFooterAdapterWrap;
import com.nestrefreshlib.RefreshViews.RefreshLayout;

import java.util.List;

/**
 * Created by 不听话的好孩子 on 2018/3/6.
 */

public class RefreshDifferUtils {

    public static void differUpdate(List<SAdapter.DifferCallback.differ> newlist, RefreshLayout layout){
        RecyclerView view = layout.getmScroll();
        RecyclerView.Adapter adapter = view.getAdapter();
        if(!(adapter instanceof RefreshHeaderAndFooterAdapterWrap)){
            throw new IllegalArgumentException("不支持非RefreshHeaderAndFooterAdapterWrap的");
        }
        List<SAdapter.DifferCallback.differ> beanlist =((SAdapter) ((RefreshHeaderAndFooterAdapterWrap) adapter).getWrapAdapter()).getBeanlist();
        SAdapter.DifferCallback.differ header=new SAdapter.DifferCallback.differ() {
            @Override
            public String firstCondition() {
                return "";
            }

            @Override
            public String secondCondition() {
                return "";
            }
        };
        newlist.add(0,header);
        beanlist.add(0,header);

        SAdapter.DifferCallback callback = new SAdapter.DifferCallback(beanlist, newlist);
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(callback, false);
        newlist.remove(0);
        beanlist.remove(0);
        ((SAdapter) ((RefreshHeaderAndFooterAdapterWrap) adapter).getWrapAdapter()).setList(newlist);
        diffResult.dispatchUpdatesTo(adapter);
    }
}
