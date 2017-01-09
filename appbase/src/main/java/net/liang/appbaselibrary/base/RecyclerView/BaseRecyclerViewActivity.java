package net.liang.appbaselibrary.base.RecyclerView;

import android.graphics.Color;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.socks.library.KLog;

import net.liang.appbaselibrary.R;
import net.liang.appbaselibrary.base.BaseAppCompatActivity;
import net.liang.appbaselibrary.base.mvp.MvpPresenter;
import net.liang.appbaselibrary.data.RecyclerDataRepository;
import net.liang.appbaselibrary.data.RecyclerDataSource;
import net.liang.appbaselibrary.data.local.LocalRecyclerDataSource;
import net.liang.appbaselibrary.utils.SPUtils;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created on 2016/10/23.
 * By lianghuiyong@outlook.com
 * @param <T> 是获取过来的数据类型
 * @param <S> 是请求的数据类型
 */

public abstract class BaseRecyclerViewActivity<T, S> extends BaseAppCompatActivity implements BaseRecyclerViewContract.View<T, S>, RecyclerDataSource<T, S> {
    protected abstract BaseRecyclerAdapter addRecyclerAdapter();

    protected BaseRecyclerAdapter adapter;
    protected SwipeRefreshLayout swipeRefresh;
    protected RecyclerView recyclerView;
    private BaseRecyclerViewContract.Presenter mPresenter;
    private int pageNo = 1;

    public int getPageNo() {
        return pageNo;
    }

    @Override
    public void init() {
        mPresenter = new BaseRecyclerViewPresenter(this,
                new RecyclerDataRepository(this, LocalRecyclerDataSource.getInstance()));

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);

        adapter = adapter == null ? addRecyclerAdapter() : adapter;

        swipeRefresh.setColorSchemeColors(Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pageNo = 1;
                mPresenter.upData();
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        adapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                pageNo++;
                mPresenter.upData();
            }
        });

        swipeRefresh.setRefreshing(true);
        mPresenter.upData();

        SPUtils.getInstance(this).putString("hhh","123456");
        KLog.e(SPUtils.getInstance(this).getString("hhh","111"));
    }

    @Override
    public void onSuccess(T t) {
        swipeRefresh.setRefreshing(false);
    }

    @Override
    protected MvpPresenter getPresenter() {
        return mPresenter;
    }

    @Override
    public void showNetworkFail(String err) {
        swipeRefresh.setRefreshing(false);
        if (pageNo == adapter.getFirstPageNo()){
            adapter.showNetWorkErrorView();
        }
    }
}
