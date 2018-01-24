package cn.sn.zwcx.yizi.contract.detail;

import cn.sn.zwcx.yizi.contract.home.tabs.ZhihuContract;
import cn.sn.zwcx.yizi.model.bean.zhihu.ZhihuDailyDetailBean;
import cn.sn.zwcx.yizi.presenter.detail.BaseWebViewLoadPresenter;
import io.reactivex.Observable;

/**
 * Created by on 2018/1/10 16:57.
 * Created by author YangHuan
 * e-mail:435025168@qq.com
 * 知乎日报详情页接口
 */

public interface ZhihuDetailContract {
    abstract class ZhihuDetailPresenter extends BaseWebViewLoadPresenter<IZhihuDetailModel,IZhihuDetailView>{
        /**
         * 加载日报详情
         * @param id
         */
        public abstract void loadDailyDetail(String id);
    }

    interface IZhihuDetailModel extends BaseWebViewLoadContract.IBaseWebViewLoadModel{
        /**
         * 获取日报详情
         * @param id 日报id
         * @return Observable
         */
        Observable<ZhihuDailyDetailBean> getDailyDetail(String id);
    }

    interface IZhihuDetailView extends BaseWebViewLoadContract.IBaseWebViewLoadView{
        /**
         * 显示日报详细内容
         * @param bean ZhihuDailyDetailBean
         */
        void showDailyDetail(ZhihuDailyDetailBean bean);
    }

}
