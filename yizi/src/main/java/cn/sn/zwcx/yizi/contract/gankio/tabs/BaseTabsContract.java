package cn.sn.zwcx.yizi.contract.gankio.tabs;

import java.util.List;

import cn.sn.zwcx.sdk.base.BasePresenter;
import cn.sn.zwcx.sdk.base.IBaseFragment;
import cn.sn.zwcx.sdk.base.IBaseModel;
import io.reactivex.Observable;

/**
 * Created by on 2018/1/10 8:38.
 * Created by author YangHuan
 * e-mail:435025168@qq.com
 */

public interface BaseTabsContract {

    abstract class BaseTabsPresenter<M extends IBaseTabsModel,V extends IBaseTabsView,T> extends BasePresenter<M,V>{

        /**
         * 加载最新的List
         */
        public abstract void loadLatestList();

        /**
         * 加载更多List
         */
        public abstract void loadMoreList();

        /**
         * Item点击事件
         * @param position
         * @param item
         */
        public abstract void onItemClick(int position,T item);

    }

    interface IBaseTabsModel extends IBaseModel{
        /**
         * 记录item已阅到数据库
         * @param key (item.id 值作为key)
         * @return
         */
        Observable<Boolean> recordItemIsRead(String key);
    }

    interface IBaseTabsView<L> extends IBaseFragment{
        /**
         * 更新界面list
         * @param list
         */
        void updateContentList(List<L> list);

        /**
         * 点击事件后，刷新item
         * @param position
         */
        void itemNotifyChanged(int position);

        /**
         * 显示网络错误
         */
        void showNetworkError();

        /**
         * 显示加载更多错误
         */
        void showLoadMoreError();

        /**
         * 显示没有更多数据
         */
        void showNoMoreDate();

    }

}
