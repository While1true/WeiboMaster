package com.master.weibomaster.Holder;

import android.arch.lifecycle.LifecycleOwner;
import android.content.Context;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;

import com.master.weibomaster.Model.Base;
import com.master.weibomaster.Rx.DataObserver;
import com.master.weibomaster.Util.ActivityUtils;
import com.nestrefreshlib.Adpater.Base.ItemHolder;
import com.nestrefreshlib.RefreshViews.AdapterHelper.AdapterScrollListener;
import com.nestrefreshlib.RefreshViews.AdapterHelper.StateAdapter;
import com.nestrefreshlib.State.DefaultStateListener;
import com.nestrefreshlib.State.Interface.StateEnum;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;

/**
 * Created by 不听话的好孩子 on 2018/3/22.
 */

public abstract class RecyclerviewPageLoading<T> extends DataObserver<List<T>> implements AdapterScrollListener.Callback {
    private int pagenum = 1;
    private int pagesize = 8;
    private boolean nomore = false;
    private boolean loading = false;
    private List<T> list = new ArrayList<>();
    private StateAdapter stateAdapter = new StateAdapter(list);

    public RecyclerviewPageLoading(RecyclerView recyclerView) {
        this(recyclerView, new LinearLayoutManager(recyclerView.getContext()), new DividerItemDecoration(recyclerView.getContext(),LinearLayout.VERTICAL));
    }

    public RecyclerviewPageLoading(RecyclerView recyclerView, RecyclerView.LayoutManager layoutManager, RecyclerView.ItemDecoration... itemDecorations) {
        super(ActivityUtils.getTopActivity());
        stateAdapter.setStateListener(new DefaultStateListener() {
            @Override
            public void netError(Context context) {
                stateAdapter.showLoading();
                pagenum = 1;
                Go();
            }
        });
        recyclerView.setLayoutManager(layoutManager);
        if (itemDecorations != null) {
            for (RecyclerView.ItemDecoration itemDecoration : itemDecorations) {
                recyclerView.addItemDecoration(itemDecoration);
            }
        }
        recyclerView.addOnScrollListener(new AdapterScrollListener(this));
        recyclerView.setAdapter(stateAdapter);
    }

    @Override
    public void call() {
        if (!nomore && !loading) {
            pagenum++;
            Go();
        }
    }

    public RecyclerviewPageLoading addType(ItemHolder<?> holder) {
        stateAdapter.addType(holder);
        return this;
    }

    public RecyclerviewPageLoading AddLifeOwner(LifecycleOwner owner) {
        stateAdapter.addLifeOwener(owner);
        return this;
    }

    public RecyclerviewPageLoading Go() {
        loading = true;
        getObservable().subscribe(this);
        return this;
    }

    public RecyclerviewPageLoading RestAndGo(){
        nomore=false;
        loading=false;
        pagenum=1;
        Go();
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
