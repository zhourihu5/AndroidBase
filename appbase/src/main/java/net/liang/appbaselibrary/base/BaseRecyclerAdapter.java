package net.liang.appbaselibrary.base;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import net.liang.appbaselibrary.R;

import java.util.List;

/**
 * Created by lianghuiyong on 2016/9/20.
 * Description: RecyclerView Adapter
 */
public abstract class BaseRecyclerAdapter<T> extends BaseQuickAdapter<T, BindingViewHolder> {

    private RecyclerView recyclerView;
    private Context context;
    private View netErrorView;
    private View notDataView;
    private int firstPageNo = 1;
    private int pageSize = 10;

    public int getFirstPageNo() {
        return firstPageNo;
    }

    public void setFirstPageNo(int firstPageNo) {
        this.firstPageNo = firstPageNo;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public BaseRecyclerAdapter(Context context, RecyclerView recyclerView, List<T> data) {
        super(data);
        this.recyclerView = recyclerView;
        this.context = context;
        setBaseView();
    }

    public BaseRecyclerAdapter(Context context, RecyclerView recyclerView,int layoutIdRes, List<T> data) {
        super(layoutIdRes,data);
        this.recyclerView = recyclerView;
        this.context = context;
        setBaseView();
    }


    private void setBaseView() {
        netErrorView = LayoutInflater.from(context)
                .inflate(R.layout.recycler_item_neterror, (ViewGroup) recyclerView.getParent(), false);

        notDataView = LayoutInflater.from(context)
                .inflate(R.layout.recycler_item_nodata, (ViewGroup) recyclerView.getParent(), false);

        //setEmptyView(notDataView);

        BaseCustomLoadMoreView customLoadMoreView = new BaseCustomLoadMoreView();
        customLoadMoreView.setLoadMoreEndGone(false);
        setLoadMoreView(customLoadMoreView);
        setAutoLoadMoreSize(3);
    }

    public void showNoDataView() {
        if (isEmptyView(notDataView)) {
            setEmptyView(notDataView);
            notifyItemChanged(0);
        }
    }

    public void showNetWorkErrorView() {
        if (isEmptyView(netErrorView)) {
            setEmptyView(netErrorView);
            notifyItemChanged(0);
        }
    }

    @Override
    public void addData(List<T> newData) {
        if (newData != null) {
            super.addData(newData);
        }
    }

    public void showList(List<T> listData, int pageNo) {
        if (listData == null) {
            if (pageNo == getFirstPageNo()){
                showNoDataView();
            }
            return;
        }

        if (pageNo == getFirstPageNo()){
            setNewData(listData);
        }else {
            addData(listData);
            loadMoreComplete();
        }

        if (listData.size() < getPageSize()) {
            loadMoreEnd();
        }
    }

    private boolean isEmptyView(View view) {
        return getEmptyView() != view;
    }

    @Override
    protected View getItemView(int layoutResId, ViewGroup parent) {
        ViewDataBinding binding = DataBindingUtil.inflate(mLayoutInflater, layoutResId, parent, false);
        if (binding == null) {
            return super.getItemView(layoutResId, parent);
        }
        View view = binding.getRoot();
        view.setTag(R.id.BaseQuickAdapter_databinding_support, binding);
        return view;
    }

    @Override
    protected BindingViewHolder createBaseViewHolder(View view) {
        return new BindingViewHolder(view);
    }

}
