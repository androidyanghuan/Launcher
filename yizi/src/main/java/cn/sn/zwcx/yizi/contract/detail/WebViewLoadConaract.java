package cn.sn.zwcx.yizi.contract.detail;

/**
 * Created by on 2018/1/9 9:53.
 * Created by author YangHuan
 * e-mail:435025168@qq.com
 */

public interface WebViewLoadConaract {
    abstract class WebViewLoadPresenter extends BaseWebViewLoadContract.BaseWebViewLoadPresenter<IWebViewLoadModel,IWebViewLoadView>{
        /** 加载url */
        public abstract void loadUrl(String url);
    }

    interface IWebViewLoadModel extends BaseWebViewLoadContract.IBaseWebViewLoadModel{}
    interface IWebViewLoadView extends BaseWebViewLoadContract.IBaseWebViewLoadView{
        /** 显示Url详情 */
        void  showUrlDetail(String url);
    }
}
