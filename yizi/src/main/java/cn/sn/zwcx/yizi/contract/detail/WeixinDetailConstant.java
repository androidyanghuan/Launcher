package cn.sn.zwcx.yizi.contract.detail;

import cn.sn.zwcx.yizi.presenter.detail.BaseWebViewLoadPresenter;

/**
 * Created by on 2018/1/15 15:09.
 * Created by author YangHuan
 * e-mail:435025168@qq.com
 */

public interface WeixinDetailConstant {

    abstract class WeixinDetailPresenter extends BaseWebViewLoadPresenter<IWeixinDetialModel,IWeixinDetailView>{
        /**
         * 加载微信精选详情
         * @param url
         */
        public abstract void loadWeixinChoiceDetail(String url);
    }

    interface IWeixinDetialModel extends BaseWebViewLoadContract.IBaseWebViewLoadModel{

    }

    interface IWeixinDetailView extends BaseWebViewLoadContract.IBaseWebViewLoadView{
        /**
         * 显示微信精选详细内容
         * @param url
         */
        void showWeixinChoiceDetail(String url);
    }

}
