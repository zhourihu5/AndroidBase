package net.liang.appbaselibrary.data.remote;

import android.support.annotation.Nullable;

import net.liang.appbaselibrary.data.RecyclerDataSource;

import io.reactivex.Observable;

/**
 * Created by lianghuiyong@outlook.com
 * Date on 2016/11/23
 */

public class RemoteRecyclerDataSource<T> implements RecyclerDataSource <T>{
    @Nullable
    private static RemoteRecyclerDataSource INSTANCE;


    public static RemoteRecyclerDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new RemoteRecyclerDataSource();
        }
        return INSTANCE;
    }

    @Override
    public Observable<T> onListGetData(int pageNo) {
        return null;
    }
}
