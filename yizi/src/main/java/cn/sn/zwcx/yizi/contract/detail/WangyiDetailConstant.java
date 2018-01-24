package cn.sn.zwcx.yizi.contract.detail;

import cn.sn.zwcx.yizi.contract.home.tabs.WangyiContract;
import cn.sn.zwcx.yizi.model.bean.wangyi.WangyiNewsDetailBean;
import cn.sn.zwcx.yizi.presenter.detail.BaseWebViewLoadPresenter;
import io.reactivex.Observable;
import okhttp3.ResponseBody;

/**
 * Created by on 2018/1/11 17:42.
 * Created by author YangHuan
 * e-mail:435025168@qq.com
 * 网易新闻详情页接口
 */

public interface WangyiDetailConstant {

    abstract class WangyiDetailPresenter extends BaseWebViewLoadPresenter<IWangyiDetialModel,IWangyiDetailView> {

        /**
         * 加载新闻详情
         * @param url
         */
        public abstract void loadNewsDetailWithUrl(String url);

        /**
         * 加载新闻详情
         * 排版显示不好看，直接使用loadNewsDetailWithUrl
         * @param id
         */
        public abstract void loadNewsDetailWithId(String id);

    }

    interface IWangyiDetialModel extends BaseWebViewLoadContract.IBaseWebViewLoadModel{
        /**
         * 获取日报详情
         * @param id
         * @return
         */
        Observable<ResponseBody> getNewsDetail(String id);
    }

    interface IWangyiDetailView extends BaseWebViewLoadContract.IBaseWebViewLoadView{
        /**
         * 显示新闻详细内容
         * @param bean
         */
        void showNewsDetail(WangyiNewsDetailBean bean);

        /**
         * 显示新闻详细内容
         * @param url
         */
        void showNewsDetail(String url);
    }
}
