package cn.sn.zwcx.yizi.contract.detail;

import cn.sn.zwcx.yizi.presenter.detail.BaseWebViewLoadPresenter;

/**
 * Created by on 2018/1/23 14:39.
 * Created by author YangHuan
 * e-mail:435025168@qq.com
 */

public interface GankIoDetailContract {

    abstract class GankIoDetailPresenter extends BaseWebViewLoadPresenter<IGankIoDetailModel,IGankIoDetailView>{
        /**
         * 加载GankIo详情
         * @param url url
         */
        public abstract void loadGankIoDetail(String url);
    }

    interface IGankIoDetailModel extends BaseWebViewLoadContract.IBaseWebViewLoadModel{}

    interface IGankIoDetailView extends BaseWebViewLoadContract.IBaseWebViewLoadView{
        /**
         * 显示GankIo详细内容
         * @param url url
         */
        void showGankIoDetail(String url);
    }

}
