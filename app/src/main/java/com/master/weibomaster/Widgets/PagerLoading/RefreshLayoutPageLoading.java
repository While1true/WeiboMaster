package com.master.weibomaster.Widgets.PagerLoading;

import android.arch.lifecycle.LifecycleOwner;
import android.content.Context;
import android.support.v7.widget.InnerDecorate;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;

import com.master.weibomaster.Model.Base;
import com.master.weibomaster.Rx.DataObserver;
import com.master.weibomaster.Util.ActivityUtils;
import com.nestrefreshlib.Adpater.Base.ItemHolder;
import com.nestrefreshlib.RefreshViews.AdapterHelper.AdapterScrollListener;
import com.nestrefreshlib.RefreshViews.AdapterHelper.StateAdapter;
import com.nestrefreshlib.RefreshViews.RefreshLayout;
import com.nestrefreshlib.RefreshViews.RefreshListener;
import com.nestrefreshlib.State.DefaultStateListener;
import com.nestrefreshlib.State.Interface.StateEnum;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;


/**
 * Created by 不听话的好孩子 on 2018/3/22.
 */

public abstract class RefreshLayoutPageLoading<T> extends DataObserver<List<T>> implements AdapterScrollListener.Callback {
    private int pagenum = 1;
    private int pagesize = 8;
    private boolean nomore = false;
    private boolean loading = false;
    private List<T> list = new ArrayList<>();
    private RefreshLayout refreshLayout;
    private StateAdapter stateAdapter = new StateAdapter(list);

    public RefreshLayoutPageLoading(RefreshLayout refreshLayout) {
        this(refreshLayout, new LinearLayoutManager(refreshLayout.getContext()), new InnerDecorate(refreshLayout.getContext(), LinearLayout.VERTICAL));
    }

    public RefreshLayoutPageLoading(RefreshLayout refreshLayout, RecyclerView.LayoutManager layoutManager, RecyclerView.ItemDecoration... itemDecorations) {
        super(ActivityUtils.getTopActivity());
        this.refreshLayout = refreshLayout;
        RecyclerView scroll = refreshLayout.getmScroll();
        if (!(scroll instanceof RecyclerView)) {
            throw new UnsupportedOperationException("子view必須是Recyclerview");
        }
        stateAdapter.setStateListener(new DefaultStateListener() {
            @Override
            public void netError(Context context) {
                stateAdapter.showLoading();
                pagenum = 1;
                Go();
            }
        });
        scroll.setLayoutManager(layoutManager);
        if (itemDecorations != null) {
            for (RecyclerView.ItemDecoration itemDecoration : itemDecorations) {
                scroll.addItemDecoration(itemDecoration);
            }
        }
        scroll.addOnScrollListener(new AdapterScrollListener(this));
        scroll.setAdapter(stateAdapter);
        refreshLayout.setListener(new RefreshListener() {
            @Override
            public void Refreshing() {
                pagenum = 1;
                Go();
            }

            @Override
            public void Loading() {

            }
        });
    }

    @Override
    public void call() {
        if (!nomore && !loading) {
            pagenum++;
            Go();
        }
    }

    public RefreshLayoutPageLoading addType(ItemHolder<?> holder) {
        stateAdapter.addType(holder);
        return this;
    }

    public RefreshLayoutPageLoading AddLifeOwner(LifecycleOwner owner) {
        stateAdapter.addLifeOwener(owner);
        return this;
    }

    public RefreshLayoutPageLoading Go() {
        loading = true;
        getObservable().subscribe(this);
        return this;
    }

    @Override
    public void OnNEXT(List<T> bean) {
        if (pagenum == 1) {
            list.clear();
        }
        list.addAll(bean);
        if (pagenum == 1 && bean.isEmpty()) {
            nomore = true;
            stateAdapter.showEmpty();
        } else {
            if (bean.size() < pagesize) {
                nomore = true;
                stateAdapter.showState(StateEnum.SHOW_NOMORE, (pagenum == 1) ? "" : "这是底线了");
            } else {
                stateAdapter.showState(StateEnum.SHOW_NOMORE, "正在加载中...");
            }
            refreshLayout.NotifyCompleteRefresh0();
        }
    }

    @Override
    public void onComplete() {
        super.onComplete();
        loading = false;
    }

    @Override
    public void OnERROR(String error) {
        super.OnERROR(error);
        if (pagenum == 1)
            stateAdapter.ShowError();
        if (pagenum > 1)
            pagenum--;
        refreshLayout.NotifyCompleteRefresh0();
    }

    public int getPagenum() {
        return pagenum;
    }

    public void setPagenum(int pagenum) {
        this.pagenum = pagenum;
    }

    public int getPagesize() {
        return pagesize;
    }

    public void setPagesize(int pagesize) {
        this.pagesize = pagesize;
    }

    public StateAdapter getStateAdapter() {
        return stateAdapter;
    }

    public abstract Observable<Base<List<T>>> getObservable();
}
